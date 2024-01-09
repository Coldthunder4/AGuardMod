package net.coldthunder4.cellguard;

import com.mojang.logging.LogUtils;
import net.coldthunder4.cellguard.block.ModBlocks;
import net.coldthunder4.cellguard.enchantment.ModEnchantmentsClass;
import net.coldthunder4.cellguard.entity.ModEntityTypes;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.coldthunder4.cellguard.item.ModItems;
import net.coldthunder4.cellguard.networking.ModPackets;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CellGuard.MOD_ID)
public class CellGuard
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "cellguard";
    private static final Logger LOGGER = LogUtils.getLogger();

    public CellGuard()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModItems.register(modEventBus);

        ModBlocks.register(modEventBus);

        ModEnchantmentsClass.ENCHANTMENTS.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        ModEntityTypes.ENTITY_TYPES.register(modEventBus);

        CellGuardPacketHandler.registerPackets();
    }

    public static boolean ownerChecker(Player player, GuardEntity cellGuard) {
        return true;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

        ModPackets.register();
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {

        }
    }
}
