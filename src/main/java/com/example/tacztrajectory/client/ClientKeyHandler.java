package com.example.tacztrajectory.client;

import com.example.tacztrajectory.TaczTrajectoryOffset;
import com.example.tacztrajectory.TrajectoryData;
import com.example.tacztrajectory.network.PacketHandler;
import com.example.tacztrajectory.network.SetTrajectoryOffsetPacket;
import com.tacz.guns.api.item.IGun;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(modid = TaczTrajectoryOffset.MOD_ID, value = Dist.CLIENT)
public class ClientKeyHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;

        // Only process when holding a TACZ gun
        ItemStack heldItem = mc.player.getMainHandItem();
        if (!(heldItem.getItem() instanceof IGun)) return;

        int key = event.getKey();
        int action = event.getAction();

        if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_REPEAT) return;

        float delta = 0.0f;
        boolean reset = false;

        if (key == GLFW.GLFW_KEY_UP || key == GLFW.GLFW_KEY_RIGHT) {
            delta = TrajectoryData.STEP;
        } else if (key == GLFW.GLFW_KEY_DOWN || key == GLFW.GLFW_KEY_LEFT) {
            delta = -TrajectoryData.STEP;
        } else if (key == GLFW.GLFW_KEY_R && action == GLFW.GLFW_PRESS) {
            reset = true;
        } else {
            return;
        }

        if (reset) {
            TrajectoryData.setOffset(mc.player, 0.0f);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(0.0f, true));
        } else {
            TrajectoryData.adjustOffset(mc.player, delta);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(delta, false));
        }
    }
}
