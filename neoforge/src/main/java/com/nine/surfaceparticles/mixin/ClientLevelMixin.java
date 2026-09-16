package com.nine.surfaceparticles.mixin;

import com.nine.surfaceparticles.core.emit.BreakParticleHandler;
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
        if (BreakParticleHandler.handleDestroy((ClientLevel) (Object) this, pos, state)) {
            ci.cancel();
        }
    }

    @Inject(
            method = "addBreakingParticles(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/level/block/state/BlockState;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sp$addBreakingParticles(BlockPos pos, Direction direction, BlockState state, CallbackInfo ci) {
        if (BreakParticleHandler.handleHit((ClientLevel) (Object) this, pos, direction, state)) {
            ci.cancel();
        }
    }
}
