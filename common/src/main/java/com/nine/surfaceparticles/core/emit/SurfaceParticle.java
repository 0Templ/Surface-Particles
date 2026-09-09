package com.nine.surfaceparticles.core.emit;

import com.nine.surfaceparticles.core.surface.Surface;
import com.nine.surfaceparticles.core.texture.SurfaceTexture;
import com.nine.surfaceparticles.core.texture.SurfaceTextures;
import com.nine.surfaceparticles.core.tuning.ParticleTuning;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class SurfaceParticle extends TerrainParticle {

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
                SurfaceTextures.top(surface.state(), level, surface.pos()), Direction.UP);
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
        this(level, x, y, z, xSpeed, ySpeed, zSpeed, state, pos, texture, null);
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
            SurfaceTexture texture,
            Direction shadedFace
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, state, pos);

        this.setSprite(texture.sprite());
        this.layer = SingleQuadParticle.Layer.bySprite(texture.sprite());

        float shade = ParticleTuning.shadeFor(shadedFace) * ParticleTuning.DUST_DIM;
        int color = texture.color();
        if (color == -1) {
            this.rCol = shade;
            this.gCol = shade;
            this.bCol = shade;
        } else {
            this.rCol = shade * (color >> 16 & 0xFF) / 255.0F;
            this.gCol = shade * (color >> 8 & 0xFF) / 255.0F;
            this.bCol = shade * (color & 0xFF) / 255.0F;
        }
    }

    @Override
    public SingleQuadParticle.Layer getLayer() {
        return this.layer;
    }
}
