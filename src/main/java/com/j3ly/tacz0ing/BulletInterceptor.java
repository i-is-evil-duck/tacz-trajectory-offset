package com.j3ly.tacz0ing;

import com.tacz.guns.entity.EntityKineticBullet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = TaczTrajectoryOffset.MOD_ID)
public class BulletInterceptor {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) return;

        Entity entity = event.getEntity();
        if (!(entity instanceof EntityKineticBullet bullet)) return;

        Entity owner = bullet.getOwner();
        if (!(owner instanceof ServerPlayer player)) return;

        float offset = TrajectoryData.getOffset(player);
        if (offset == 0.0f) return;

        Vec3 motion = bullet.getDeltaMovement();
        double speed = motion.length();
        if (speed < 0.001) {
            LOGGER.debug("Bullet spawned for {} with offset {} but velocity not yet set", player.getName().getString(), offset);
            return;
        }

        double horizontalSpeed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);
        double currentPitch = Math.atan2(-motion.y, horizontalSpeed);
        double yaw = Math.atan2(motion.z, motion.x);

        double newPitch = currentPitch - (double) offset * Math.PI / 180.0;
        double newHorizontalSpeed = speed * Math.cos(newPitch);
        double newVy = -speed * Math.sin(newPitch);

        bullet.setDeltaMovement(new Vec3(
            newHorizontalSpeed * Math.cos(yaw),
            newVy,
            newHorizontalSpeed * Math.sin(yaw)
        ));

        LOGGER.debug("Applied offset {} to bullet for {}", offset, player.getName().getString());
    }
}
