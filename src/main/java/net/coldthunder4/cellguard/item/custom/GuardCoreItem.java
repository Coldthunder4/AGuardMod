package net.coldthunder4.cellguard.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.EnchantedGoldenAppleItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GuardCoreItem extends Item {
    public GuardCoreItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(Component.literal("The Core to a Powerful Defender"));
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
