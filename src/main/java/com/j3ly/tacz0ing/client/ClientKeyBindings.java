package com.j3ly.tacz0ing.client;

import com.j3ly.tacz0ing.TaczTrajectoryOffset;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TaczTrajectoryOffset.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientKeyBindings {

    public static KeyMapping lockKey;
    public static KeyMapping resetKey;

    @SubscribeEvent
    public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        String category = "key.tacz0ing.category";

        lockKey = new KeyMapping(
            "key.tacz0ing.lock",
            GLFW.GLFW_KEY_APOSTROPHE,
            category
        );

        resetKey = new KeyMapping(
            "key.tacz0ing.reset",
            GLFW.GLFW_KEY_SEMICOLON,
            category
        );

        event.register(lockKey);
        event.register(resetKey);
    }
}
