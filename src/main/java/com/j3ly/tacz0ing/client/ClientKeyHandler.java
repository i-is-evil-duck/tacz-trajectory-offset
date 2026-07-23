package com.j3ly.tacz0ing.client;

import com.j3ly.tacz0ing.ModConfig;
import com.j3ly.tacz0ing.TaczTrajectoryOffset;
import com.j3ly.tacz0ing.TrajectoryData;
import com.j3ly.tacz0ing.network.PacketHandler;
import com.j3ly.tacz0ing.network.SetTrajectoryLockPacket;
import com.j3ly.tacz0ing.network.SetTrajectoryOffsetPacket;
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

        ItemStack heldItem = mc.player.getMainHandItem();
        if (!(heldItem.getItem() instanceof IGun)) return;

        int key = event.getKey();
        int action = event.getAction();

        if (action != GLFW.GLFW_PRESS && action != GLFW.GLFW_REPEAT) return;

        boolean locked = TrajectoryData.isLocked(mc.player);

        if (key == GLFW.GLFW_KEY_UP) {
            if (locked) return;
            TrajectoryData.adjustPitchOffset(mc.player, TrajectoryData.STEP);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(TrajectoryData.STEP, 0.0f, false));
        } else if (key == GLFW.GLFW_KEY_DOWN) {
            if (locked) return;
            TrajectoryData.adjustPitchOffset(mc.player, -TrajectoryData.STEP);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(-TrajectoryData.STEP, 0.0f, false));
        } else if (key == GLFW.GLFW_KEY_LEFT) {
            if (locked) return;
            TrajectoryData.adjustYawOffset(mc.player, -TrajectoryData.STEP);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(0.0f, -TrajectoryData.STEP, false));
        } else if (key == GLFW.GLFW_KEY_RIGHT) {
            if (locked) return;
            TrajectoryData.adjustYawOffset(mc.player, TrajectoryData.STEP);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(0.0f, TrajectoryData.STEP, false));
        } else if (key == ModConfig.getLockKeyCode() && action == GLFW.GLFW_PRESS) {
            boolean newLocked = !locked;
            TrajectoryData.setLocked(mc.player, newLocked);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryLockPacket(newLocked));
        } else if (key == ModConfig.getResetKeyCode() && action == GLFW.GLFW_PRESS) {
            TrajectoryData.clearAllOffsets(mc.player);
            PacketHandler.INSTANCE.sendToServer(new SetTrajectoryOffsetPacket(0.0f, 0.0f, true));
        }
    }
}
