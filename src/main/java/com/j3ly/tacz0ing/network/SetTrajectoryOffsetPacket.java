package com.j3ly.tacz0ing.network;

import com.j3ly.tacz0ing.TrajectoryData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetTrajectoryOffsetPacket {
    private final float pitchDelta;
    private final float yawDelta;
    private final boolean reset;

    public SetTrajectoryOffsetPacket(float pitchDelta, float yawDelta, boolean reset) {
        this.pitchDelta = pitchDelta;
        this.yawDelta = yawDelta;
        this.reset = reset;
    }

    public static void encode(SetTrajectoryOffsetPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.pitchDelta);
        buf.writeFloat(msg.yawDelta);
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
                    if (msg.pitchDelta != 0.0f) {
                        TrajectoryData.adjustPitchOffset(player, msg.pitchDelta);
                    }
                    if (msg.yawDelta != 0.0f) {
                        TrajectoryData.adjustYawOffset(player, msg.yawDelta);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
