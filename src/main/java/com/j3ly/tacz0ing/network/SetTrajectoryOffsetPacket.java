package com.j3ly.tacz0ing.network;

import com.j3ly.tacz0ing.TrajectoryData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetTrajectoryOffsetPacket {
    private final float pitch;
    private final float yaw;
    private final boolean reset;

    public SetTrajectoryOffsetPacket(float pitch, float yaw, boolean reset) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.reset = reset;
    }

    public static void encode(SetTrajectoryOffsetPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.pitch);
        buf.writeFloat(msg.yaw);
        buf.writeBoolean(msg.reset);
    }

    public static SetTrajectoryOffsetPacket decode(FriendlyByteBuf buf) {
        return new SetTrajectoryOffsetPacket(buf.readFloat(), buf.readFloat(), buf.readBoolean());
    }

    public static void handle(SetTrajectoryOffsetPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                if (msg.reset) {
                    TrajectoryData.clearAllOffsets(player);
                } else {
                    TrajectoryData.setPitchOffset(player, msg.pitch);
                    TrajectoryData.setYawOffset(player, msg.yaw);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
