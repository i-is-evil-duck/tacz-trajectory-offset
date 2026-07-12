package com.example.tacztrajectory;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TrajectoryData {
    private static final Map<UUID, Float> playerOffsets = new HashMap<>();

    public static final float MAX_OFFSET = 45.0f;
    public static final float MIN_OFFSET = -45.0f;
    public static final float STEP = 1.0f;

    public static float getOffset(Player player) {
        return playerOffsets.getOrDefault(player.getUUID(), 0.0f);
    }

    public static void setOffset(Player player, float offset) {
        offset = Math.max(MIN_OFFSET, Math.min(MAX_OFFSET, offset));
        playerOffsets.put(player.getUUID(), offset);
    }

    public static void adjustOffset(Player player, float delta) {
        float current = getOffset(player);
        setOffset(player, current + delta);
    }

    public static void clearOffset(Player player) {
        playerOffsets.remove(player.getUUID());
    }

    public static void resetAll() {
        playerOffsets.clear();
    }

    @SubscribeEvent
    public void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        clearOffset((Player) event.getEntity());
    }
}
