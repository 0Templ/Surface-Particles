package com.nine.surfaceparticles.core.emit;

import com.nine.surfaceparticles.core.surface.Surface;
import com.nine.surfaceparticles.core.texture.SurfaceTexture;
import com.nine.surfaceparticles.core.texture.SurfaceTextures;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SurfaceParticle extends TerrainParticle {

    private static final float SHADE = 0.6F;

    private final SingleQuadParticle.Layer layer;
    public SurfaceParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            Surface surface
    ) {
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, surface.state(), surface.pos(),
                SurfaceTextures.top(surface.state(), level, surface.pos()));
    }

    public SurfaceParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed,
            BlockState state,
            BlockPos pos,
            SurfaceTexture texture
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, state, pos);

        this.setSprite(texture.sprite());
        this.layer = SingleQuadParticle.Layer.bySprite(texture.sprite());

        int color = texture.color();
        this.rCol = SHADE * (color >> 16 & 0xFF) / 255.0F;
        this.gCol = SHADE * (color >> 8 & 0xFF) / 255.0F;
        this.bCol = SHADE * (color & 0xFF) / 255.0F;
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }
}
