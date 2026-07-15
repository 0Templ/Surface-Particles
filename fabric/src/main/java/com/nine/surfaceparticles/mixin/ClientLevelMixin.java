package com.nine.surfaceparticles.mixin;

import com.nine.surfaceparticles.core.emit.BreakParticleEmitter;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
    private void sp$addDestroyBlockEffect(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!BreakParticleEmitter.destroyEnabled()) {
            return;
        }
        BreakParticleEmitter.destroyEffects((ClientLevel) (Object) this, pos, state);
        ci.cancel();
    }

    @Inject(method = "addBreakingBlockEffect", at = @At("HEAD"), cancellable = true)
    private void sp$addBreakingBlockEffect(BlockPos pos, Direction direction, CallbackInfo ci) {
        if (!BreakParticleEmitter.hitEnabled()) {
            return;
        }
        BreakParticleEmitter.crackEffect((ClientLevel) (Object) this, pos, direction);
        ci.cancel();
    }
}
