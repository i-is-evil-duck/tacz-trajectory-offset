package com.example.tacztrajectory.mixin;

import com.example.tacztrajectory.TrajectoryData;
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

        float offset = TrajectoryData.getOffset(player);
        if (offset == 0.0f) return;

        Entity self = (Entity) (Object) this;
        Vec3 motion = self.getDeltaMovement();
        double speed = motion.length();
        if (speed < 0.001) return;

        double horizontalSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        double currentPitch = Math.atan2(-motion.y, horizontalSpeed);
        double yaw = Math.atan2(motion.z, motion.x);

        double newPitch = currentPitch - (double) offset * Math.PI / 180.0;
        double newHorizontalSpeed = speed * Math.cos(newPitch);
        double newVy = -speed * Math.sin(newPitch);

        self.setDeltaMovement(new Vec3(
            newHorizontalSpeed * Math.cos(yaw),
            newVy,
            newHorizontalSpeed * Math.sin(yaw)
        ));
    }
}
