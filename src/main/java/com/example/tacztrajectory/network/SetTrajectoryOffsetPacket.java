package com.example.tacztrajectory.network;

import com.example.tacztrajectory.TrajectoryData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetTrajectoryOffsetPacket {
    private final float offsetDelta;
    private final boolean reset;

    public SetTrajectoryOffsetPacket(float offsetDelta, boolean reset) {
        this.offsetDelta = offsetDelta;
        this.reset = reset;
    }

    public static void encode(SetTrajectoryOffsetPacket msg, FriendlyByteBuf buf) {
        buf.writeFloat(msg.offsetDelta);
        buf.writeBoolean(msg.reset);
    }

    public static SetTrajectoryOffsetPacket decode(FriendlyByteBuf buf) {
        return new SetTrajectoryOffsetPacket(buf.readFloat(), buf.readBoolean());
    }

    public static void handle(SetTrajectoryOffsetPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                if (msg.reset) {
                    TrajectoryData.setOffset(player, 0.0f);
                } else {
                    TrajectoryData.adjustOffset(player, msg.offsetDelta);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
