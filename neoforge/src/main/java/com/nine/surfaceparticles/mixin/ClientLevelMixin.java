package com.nine.surfaceparticles.mixin;

import com.nine.surfaceparticles.core.emit.BreakParticleEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "addDestroyBlockEffect", at = @At("HEAD"), cancellable = true)
    private void sp$addDestroyBlockEffect(BlockPos pos, BlockState state, CallbackInfo ci) {
        if (!BreakParticleEmitter.destroyEnabled() || state.isAir()) {
            return;
        }
        ClientLevel self = (ClientLevel) (Object) this;
        if (IClientBlockExtensions.of(state).addDestroyEffects(state, self, pos, Minecraft.getInstance().particleEngine)) {
            ci.cancel();
            return;
        }
        BreakParticleEmitter.destroyEffects(self, pos, state);
        ci.cancel();
    }

    @Inject(
            method = "addBreakingBlockEffect(Lnet/minecraft/core/BlockPos;Lnet/minecraft/core/Direction;Lnet/minecraft/world/phys/HitResult;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void sp$addBreakingBlockEffect(BlockPos pos, Direction direction, HitResult hitResult, CallbackInfo ci) {
        if (!BreakParticleEmitter.hitEnabled()) {
            return;
        }
        ClientLevel self = (ClientLevel) (Object) this;
        BlockState state = self.getBlockState(pos);
        if (state.getRenderShape() == RenderShape.INVISIBLE) {
            return;
        }
        if (IClientBlockExtensions.of(state).addHitEffects(state, self, hitResult, Minecraft.getInstance().particleEngine)) {
            ci.cancel();
            return;
        }
        BreakParticleEmitter.crackEffect(self, pos, direction);
        ci.cancel();
    }
}
