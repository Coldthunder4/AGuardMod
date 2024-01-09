package net.coldthunder4.cellguard.networking;

import net.coldthunder4.cellguard.CellGuard;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;

    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(CellGuard.MOD_ID, "spawnguard"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(SpawnGuardPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SpawnGuardPacket::new)
                .encoder(SpawnGuardPacket::toBytes)
                .consumerMainThread(SpawnGuardPacket::handle)
                .add();

    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer player) {

        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);

    }

}
