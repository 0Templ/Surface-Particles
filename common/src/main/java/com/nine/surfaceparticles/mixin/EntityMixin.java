package com.nine.surfaceparticles.mixin;

import com.nine.surfaceparticles.core.emit.FootParticleEmitter;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Inject(method = "spawnSprintParticle", at = @At("HEAD"), cancellable = true)
    private void sp$spawnSprintParticle(CallbackInfo ci) {
        if (FootParticleEmitter.emitSprintParticle((Entity) (Object) this)) {
            ci.cancel();
        }
    }
}
