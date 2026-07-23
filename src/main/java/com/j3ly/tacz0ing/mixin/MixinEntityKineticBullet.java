package com.j3ly.tacz0ing.mixin;

import com.j3ly.tacz0ing.TrajectoryData;
import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = EntityKineticBullet.class, remap = false)
public class MixinEntityKineticBullet {

    @Inject(
        method = "shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFLorg/joml/Vector2d;)V",
        at = @At("RETURN")
    )
    private void taczTrajectory$modifyTrajectory(Entity pShooter, float pX, float pY, float pZ, float pVelocity, Vector2d vector2d, CallbackInfo ci) {
        if (!(pShooter instanceof ServerPlayer player)) return;

        float pitchOffset = TrajectoryData.getPitchOffset(player);
        float yawOffset = TrajectoryData.getYawOffset(player);
        if (pitchOffset == 0.0f && yawOffset == 0.0f) return;

        Entity self = (Entity) (Object) this;
        Vec3 motion = self.getDeltaMovement();
        double speed = motion.length();
        if (speed < 0.001) return;

        double horizontalSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        double currentPitch = Math.atan2(-motion.y, horizontalSpeed);
        double currentYaw = Math.atan2(motion.z, motion.x);

        double newPitch = currentPitch - (double) pitchOffset * Math.PI / 180.0;
        double newYaw = currentYaw + (double) yawOffset * Math.PI / 180.0;

        double newHorizontalSpeed = speed * Math.cos(newPitch);
        double newVy = -speed * Math.sin(newPitch);

        self.setDeltaMovement(new Vec3(
            newHorizontalSpeed * Math.cos(newYaw),
            newVy,
            newHorizontalSpeed * Math.sin(newYaw)
        ));
    }
}
