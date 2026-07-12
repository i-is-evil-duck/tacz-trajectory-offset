package com.j3ly.tacz0ing;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class TrajectoryData {
    private static final String NBT_KEY = "tacz0ing_trajectory_offset";

    public static final float MAX_OFFSET = 5.0f;
    public static final float MIN_OFFSET = -5.0f;
    public static final float STEP = 0.01f;

    public static float getOffset(Player player) {
        CompoundTag data = player.getPersistentData();
        if (data.contains(NBT_KEY)) {
            return data.getFloat(NBT_KEY);
        }
        return 0.0f;
    }

    public static void setOffset(Player player, float offset) {
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        player.getPersistentData().putFloat(NBT_KEY, offset);
    }

    public static void adjustOffset(Player player, float delta) {
        float current = getOffset(player);
        setOffset(player, current + delta);
    }

    public static void clearOffset(Player player) {
        player.getPersistentData().remove(NBT_KEY);
    }

    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {
        float offset = event.getOriginal().getPersistentData().getFloat(NBT_KEY);
        if (offset != 0.0f) {
            event.getEntity().getPersistentData().putFloat(NBT_KEY, offset);
        }
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        clearOffset((Player) event.getEntity());
    }
}
