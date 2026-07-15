package com.nine.surfaceparticles.core.texture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public final class SurfaceTextures {

    private SurfaceTextures() {
    }

    public record FaceTexture(SurfaceTexture texture, boolean tintedOverlay) {
    }

    public static SurfaceTexture top(BlockState state, ClientLevel level, BlockPos pos) {
        return face(state, level, pos, Direction.UP);
    }

    public static SurfaceTexture face(BlockState state, ClientLevel level, BlockPos pos, Direction direction) {
        return faceInfo(state, level, pos, direction).texture();
    }

    public static FaceTexture faceInfo(BlockState state, ClientLevel level, BlockPos pos, Direction direction) {
        Minecraft minecraft = Minecraft.getInstance();
        BlockStateModel model = minecraft.getModelManager().getBlockModelShaper().getBlockModel(state);

        List<BakedQuad> quads = faceQuads(model, state, pos, direction);
        if (quads.isEmpty()) {
            return new FaceTexture(particle(state, level, pos, model), false);
        }

        BakedQuad base = null;
        boolean anyTinted = false;
        for (BakedQuad quad : quads) {
            if (quad.tintIndex() >= 0) {
                anyTinted = true;
            } else if (base == null) {
                base = quad;
            }
        }
        boolean tintedOverlay = anyTinted && base != null;
        if (base == null) {
            base = quads.getFirst();
        }

        int color = -1;
        int tintIndex = base.tintIndex();
        if (tintIndex >= 0) {
            color = minecraft.getBlockColors().getColor(state, level, pos, tintIndex);
        }
        return new FaceTexture(new SurfaceTexture(base.sprite(), color), tintedOverlay);
    }

    public static SurfaceTexture particle(BlockState state, ClientLevel level, BlockPos pos) {
        BlockStateModel model = Minecraft.getInstance().getModelManager().getBlockModelShaper().getBlockModel(state);
        return particle(state, level, pos, model);
    }

    private static SurfaceTexture particle(BlockState state, ClientLevel level, BlockPos pos, BlockStateModel model) {
        int color = state.is(Blocks.GRASS_BLOCK)
                ? -1
                : Minecraft.getInstance().getBlockColors().getColor(state, level, pos, 0);
        return new SurfaceTexture(model.particleIcon(), color);
    }

    private static List<BakedQuad> faceQuads(BlockStateModel model, BlockState state, BlockPos pos, Direction direction) {
        RandomSource random = RandomSource.create();
        random.setSeed(state.getSeed(pos));
        List<BlockModelPart> parts = new ArrayList<>();
        model.collectParts(random, parts);
        for (BlockModelPart part : parts) {
            List<BakedQuad> culled = part.getQuads(direction);
            if (!culled.isEmpty()) {
                return culled;
            }
            List<BakedQuad> matching = new ArrayList<>();
            for (BakedQuad quad : part.getQuads(null)) {
                if (quad.direction() == direction) {
                    matching.add(quad);
                }
            }
            if (!matching.isEmpty()) {
                return matching;
            }
        }
        return List.of();
    }
}
