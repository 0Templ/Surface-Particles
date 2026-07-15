package com.nine.surfaceparticles.mixin;

import com.nine.surfaceparticles.core.emit.BreakParticleEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Inject(method = "destroy", at = @At("HEAD"), cancellable = true)
    private void sp$destroy(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!BreakParticleEmitter.destroyEnabled() || state.isAir()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            return;
        }
        if (IClientBlockExtensions.of(state).addDestroyEffects(state, level, pos, minecraft.particleEngine)) {
            ci.cancel();
            return;
        }
        BreakParticleEmitter.destroyEffects(level, pos, state);
        ci.cancel();
    }

    @Inject(method = "crack", at = @At("HEAD"), cancellable = true)
    private void sp$crack(BlockPos pos, Direction direction, CallbackInfo ci) {
        if (!BreakParticleEmitter.hitEnabled()) {
            return;
        }
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            return;
        }
        BlockState state = level.getBlockState(pos);
        if (state.getRenderShape() == RenderShape.INVISIBLE) {
            return;
        }
        HitResult hitResult = minecraft.hitResult;
        if (hitResult != null
                && IClientBlockExtensions.of(state).addHitEffects(state, level, hitResult, minecraft.particleEngine)) {
            ci.cancel();
            return;
        }
        BreakParticleEmitter.crackEffect(level, pos, direction);
        ci.cancel();
    }
}
