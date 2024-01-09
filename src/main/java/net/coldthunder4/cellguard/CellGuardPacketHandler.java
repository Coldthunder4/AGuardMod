package net.coldthunder4.cellguard;

import net.coldthunder4.cellguard.client.gui.CellGuardInventoryScreen;
import net.coldthunder4.cellguard.entity.CellGuardContainer;
import net.coldthunder4.cellguard.entity.ai.goals.CellGuardSetWatchBlockPosPacket;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.coldthunder4.cellguard.networking.CellGuardOpenInventoryPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.coldthunder4.cellguard.CellGuard;

public class CellGuardPacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(CellGuard.MOD_ID, "main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    public static void registerPackets() {
        int id = 0;
        INSTANCE.registerMessage(id++, CellGuardOpenInventoryPacket.class, CellGuardOpenInventoryPacket::encode, CellGuardOpenInventoryPacket::decode, CellGuardOpenInventoryPacket::handle);
        INSTANCE.registerMessage(id++, CellGuardSetWatchBlockPosPacket.class, CellGuardSetWatchBlockPosPacket::encode, CellGuardSetWatchBlockPosPacket::decode, CellGuardSetWatchBlockPosPacket::handle);
    }

    @SuppressWarnings("resource")
    @OnlyIn(Dist.CLIENT)
    public static void openGuardInventory(CellGuardOpenInventoryPacket packet) {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            Entity entity = player.level.getEntity(packet.getEntityId());
            if (entity instanceof GuardEntity cellGuard) {
                LocalPlayer clientplayerentity = Minecraft.getInstance().player;
                CellGuardContainer container = new CellGuardContainer(packet.getId(), player.getInventory(), cellGuard.cellGuardInventory, cellGuard);
                clientplayerentity.containerMenu = container;
                Minecraft.getInstance().setScreen(new CellGuardInventoryScreen(container, player.getInventory(), (GuardEntity) cellGuard));
            }
        }
    }
}
