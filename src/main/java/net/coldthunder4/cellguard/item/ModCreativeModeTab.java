package net.coldthunder4.cellguard.item;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {

    public static final CreativeModeTab GUARD_TAB = new CreativeModeTab("guardtab") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.GUARD_ITEM.get());
        }
    };
}
