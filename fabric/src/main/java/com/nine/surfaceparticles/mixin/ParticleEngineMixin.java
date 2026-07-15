package com.nine.surfaceparticles.mixin;

import com.nine.surfaceparticles.core.emit.BreakParticleEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {

    @Inject(method = "destroy", at = @At("HEAD"), cancellable = true)
    private void sp$destroy(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!BreakParticleEmitter.destroyEnabled()) {
            return;
        }
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
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
        ClientLevel level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }
        BreakParticleEmitter.crackEffect(level, pos, direction);
        ci.cancel();
    }
}
