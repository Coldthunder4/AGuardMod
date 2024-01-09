package net.coldthunder4.cellguard.item;

import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.item.custom.GuardCoreItem;
import net.coldthunder4.cellguard.item.custom.GuardItem;
import net.coldthunder4.cellguard.item.custom.IronBlockClusterItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CellGuard.MOD_ID);

    public static final RegistryObject<Item> GUARD_ITEM = ITEMS.register("guard_item",
            () -> new GuardItem(new Item.Properties().tab(ModCreativeModeTab.GUARD_TAB).stacksTo(1)));

    public static final RegistryObject<Item> IRON_BLOCK_CLUSTER = ITEMS.register("iron_block_cluster_item",
            () -> new IronBlockClusterItem(new Item.Properties().tab(ModCreativeModeTab.GUARD_TAB).stacksTo(64)));

    public static final RegistryObject<Item> GUARD_CORE = ITEMS.register("guard_core_item",
            () -> new GuardCoreItem(new Item.Properties().tab(ModCreativeModeTab.GUARD_TAB).stacksTo(64)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
