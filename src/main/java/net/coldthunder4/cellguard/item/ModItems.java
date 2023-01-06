package net.coldthunder4.cellguard.item;

import net.coldthunder4.cellguard.CellGuard;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CellGuard.MOD_ID);

    public static final RegistryObject<Item> GUARD = ITEMS.register("guard", () -> new Item(new Item.Properties().tab(ModCreativeModeTab.GUARD_TAB)));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
