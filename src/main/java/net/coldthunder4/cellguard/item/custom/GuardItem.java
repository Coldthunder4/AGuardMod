package net.coldthunder4.cellguard.item.custom;

import net.coldthunder4.cellguard.networking.ModPackets;
import net.coldthunder4.cellguard.networking.SpawnGuardPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.function.Consumer;

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









        }

        return super.use(level, player, hand);
    }
    @Override
    public void appendHoverText(ItemStack itemstack, Level world, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, world, list, flag);
        list.add(Component.literal("A Guard you can place"));
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 1;
    }


    @Override
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        return 1;
    }


    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isRepairable(ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {
        Player player = useOnContext.getPlayer();
        Level level = useOnContext.getLevel();
        BlockPos blockpos = useOnContext.getClickedPos();
        ItemStack itemstack = useOnContext.getItemInHand();
        if ( player instanceof ServerPlayer) {
        ModPackets.sendToServer(new SpawnGuardPacket());
        itemstack.hurtAndBreak(1, player, player1 -> player1.broadcastBreakEvent(useOnContext.getHand()));
        return InteractionResult.sidedSuccess(level.isClientSide());}
        return InteractionResult.FAIL;
    }
}