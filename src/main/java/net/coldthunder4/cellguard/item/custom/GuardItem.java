package net.coldthunder4.cellguard.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class GuardItem extends Item {
    public GuardItem() {
        super(new Item.Properties().tab(CreativeModeTab.TAB_COMBAT).stacksTo(1).rarity(Rarity.RARE));
    }


    public GuardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if(!level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
            //summon armor stand with no hitbox
            //
            //summon guard
            //set guard NBT to be the armor stand position or somehow link them so gaurd can return to it when idle
        }

        return super.use(level, player, hand);
    }
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("A Guard you can place"));
    }


}
//Output Guard Entity
//MAKE ENTITY FIRST!!!!
//Remove Item From Player Hand/Inventory