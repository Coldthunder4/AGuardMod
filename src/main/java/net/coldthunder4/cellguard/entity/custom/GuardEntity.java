package net.coldthunder4.cellguard.entity.custom;

import net.coldthunder4.cellguard.entity.ModEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.AttackDamageMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.EntityArmorInvWrapper;
import net.minecraftforge.items.wrapper.EntityHandsInvWrapper;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.UUID;

import static net.minecraft.world.item.alchemy.Potions.REGENERATION;


public class GuardEntity extends PathfinderMob implements NeutralMob {


    public GuardEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        xpReward = 0;
        setNoAi(false);
        String NAME = "Guard";
        setCustomName(Component.literal(NAME));
        setCustomNameVisible(true);


        setPersistenceRequired();

        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS));

        this.setDropChance(EquipmentSlot.MAINHAND, 100);
        this.setDropChance(EquipmentSlot.OFFHAND, 100);
        this.setDropChance(EquipmentSlot.HEAD, 100);
        this.setDropChance(EquipmentSlot.CHEST, 100);
        this.setDropChance(EquipmentSlot.LEGS, 100);
        this.setDropChance(EquipmentSlot.FEET, 100);





    }



/*
    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return null;
    }*/

    /*@Override
    public ItemStack getItemBySlot(EquipmentSlot equipmentSlot) {
        return ItemStack.EMPTY;
    }


    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {

    }*/


    private final ItemStackHandler inventory = new ItemStackHandler(14) {
        @Override
        public int getSlotLimit(int slot) {
            return 64;
        }


    };

    private final CombinedInvWrapper combined = new CombinedInvWrapper(inventory, new EntityHandsInvWrapper(this), new EntityArmorInvWrapper(this));

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.put("InventoryCustom", inventory.serializeNBT());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        Tag inventoryCustom = compound.get("InventoryCustom");
        if (inventoryCustom instanceof CompoundTag inventoryTag)
            inventory.deserializeNBT(inventoryTag);
    }


    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    /*@Override
    public HumanoidArm getMainArm() {
        return null;
    }*/

    @Override
    public SoundEvent getHurtSound(DamageSource ds) {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.damage"));
    }

    public static void init() {

    }

    @Override
    public SoundEvent getDeathSound() {
        return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.iron_golem.damage"));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getDirectEntity() instanceof ThrownPotion || source.getDirectEntity() instanceof AreaEffectCloud)
            return false;
        if (source == DamageSource.FALL)
            return false;
        if (source == DamageSource.CACTUS)
            return false;
        if (source == DamageSource.DROWN)
            return false;
        if (source == DamageSource.ANVIL)
            return false;
        if (source == DamageSource.DRAGON_BREATH)
            return false;
        /*if (source == DamageSource.WITHER)
            return false;
        if (source.getMsgId().equals("witherSkull"))
            return false;*/
        return super.hurt(source, amount);
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEFINED;
    }

    @Override
    protected void registerGoals() {



        this.targetSelector.addGoal(4, new HurtByTargetGoal(this){
            @Override
            public TargetGoal setUnseenMemoryTicks(int p_26147_) {
                return super.setUnseenMemoryTicks(6000);
            }


        });
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.2, true) {


            @Override
            protected int adjustedTickDelay(int p_186072_) {
                return super.adjustedTickDelay(2);
            }



            @Override
            public void start() {
                super.start();
            }

            @Override
            protected void checkAndPerformAttack(LivingEntity livingEntity, double p_25558_) {
                super.checkAndPerformAttack(livingEntity, p_25558_);

                this.mob.swing(InteractionHand.MAIN_HAND);
            }


            @Override
            protected double getAttackReachSqr(LivingEntity entity) {
                return (double) (12.0 + entity.getBbWidth() * entity.getBbWidth());


            }

        });


        this.goalSelector.addGoal(10, new RandomStrollGoal(this, 0));

        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Monster.class, true, true));
        this.goalSelector.addGoal(7, new FloatGoal(this));

        super.registerGoals();
    }

    @Override
    public float getAttackAnim(float v) {
        return super.getAttackAnim(v);

    }


    @Override
    public void onEnterCombat() {
        super.onEnterCombat();
        removeEffect(MobEffects.HEAL);
        removeEffect(MobEffects.REGENERATION);


    }

    @Override
    public void onLeaveCombat() {
        super.onLeaveCombat();

        addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100000, 3, false, false));


    }


    @Override
    public boolean doHurtTarget(Entity p_21372_) {
        return super.doHurtTarget(p_21372_);
    }

    /*@Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason,
                                        @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
        return retval;
    }*/

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
        builder = builder.add(Attributes.MAX_HEALTH, 124);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 128);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 0.00000001);
        builder = builder.add(Attributes.ARMOR_TOUGHNESS, 0);
        builder = builder.add(ForgeMod.SWIM_SPEED.get(), 1);
        builder = builder.add(ForgeMod.ATTACK_RANGE.get(), 3);
        return builder;
    }



    @Override
    public int getRemainingPersistentAngerTime() {
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int p_21673_) {

    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID p_21672_) {

    }

    @Override
    public void startPersistentAngerTimer() {

    }

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE,
            ServerBossEvent.BossBarOverlay.PROGRESS);


    @Override
    public void startSeenByPlayer(ServerPlayer player) {

        super.

    startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);

}
    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }
    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }


}