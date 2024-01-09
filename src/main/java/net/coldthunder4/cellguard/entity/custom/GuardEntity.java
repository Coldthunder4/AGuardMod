package net.coldthunder4.cellguard.entity.custom;

import com.google.common.collect.Maps;
import com.ibm.icu.impl.CalType;
import net.coldthunder4.cellguard.CellGuardPacketHandler;
import net.coldthunder4.cellguard.entity.CellGuardContainer;
import net.coldthunder4.cellguard.entity.CellGuardLootTables;
import net.coldthunder4.cellguard.entity.ai.goals.CellGuardMeleeGoal;
import net.coldthunder4.cellguard.entity.ai.goals.CellGuardSetWatchBlockPosPacket;
import net.coldthunder4.cellguard.entity.ai.goals.ReturnToWatchBlock;
import net.coldthunder4.cellguard.networking.CellGuardOpenInventoryPacket;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;



public class GuardEntity extends PathfinderMob implements NeutralMob, ContainerListener {

    protected static final EntityDataAccessor<Optional<UUID>> OWNER_UNIQUE_ID = SynchedEntityData.defineId(GuardEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Optional<BlockPos>> CELLGUARD_POS = SynchedEntityData.defineId(GuardEntity.class, EntityDataSerializers.OPTIONAL_BLOCK_POS);

    private static final Map<EquipmentSlot, ResourceLocation> EQUIPMENT_SLOT_ITEMS = Util.make(Maps.newHashMap(), (slotItems) -> {
        slotItems.put(EquipmentSlot.MAINHAND, CellGuardLootTables.CELLGUARD_MAIN_HAND);
        slotItems.put(EquipmentSlot.HEAD, CellGuardLootTables.CELLGUARD_HELMET);
        slotItems.put(EquipmentSlot.CHEST, CellGuardLootTables.CELLGUARD_CHEST);
        slotItems.put(EquipmentSlot.LEGS, CellGuardLootTables.CELLGUARD_LEGGINGS);
        slotItems.put(EquipmentSlot.FEET, CellGuardLootTables.CELLGUARD_FEET);
    });
    public SimpleContainer cellGuardInventory = new SimpleContainer(6);
    private net.minecraftforge.common.util.LazyOptional<?> itemHandler;

    public GuardEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        xpReward = 0;
        setNoAi(false);
        String NAME = "Guard";
        setCustomName(Component.literal(NAME));
        setCustomNameVisible(true);


        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.WOODEN_SWORD));
        this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        this.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
        this.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
        this.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));

        this.setDropChance(EquipmentSlot.MAINHAND, 100);
        this.setDropChance(EquipmentSlot.OFFHAND, 100);
        this.setDropChance(EquipmentSlot.HEAD, 100);
        this.setDropChance(EquipmentSlot.CHEST, 100);
        this.setDropChance(EquipmentSlot.LEGS, 100);
        this.setDropChance(EquipmentSlot.FEET, 100);

        this.cellGuardInventory.addListener(this);
        this.itemHandler = net.minecraftforge.common.util.LazyOptional.of(() -> new net.minecraftforge.items.wrapper.InvWrapper(this.cellGuardInventory));
        this.setPersistenceRequired();



    }
    public static int slotToInventoryIndex(EquipmentSlot slot) {
        switch (slot) {
            case CHEST:
                return 1;
            case FEET:
                return 3;
            case HEAD:
                return 0;
            case LEGS:
                return 2;
            default:
                break;
        }
        return 0;
    }





    private final ItemStackHandler inventory = new ItemStackHandler(6) {
        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }


    };

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setPersistenceRequired();


        CellGuardPacketHandler.INSTANCE.sendToServer(new CellGuardSetWatchBlockPosPacket(this.getId()));


        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }


    // private final CombinedInvWrapper combined = new CombinedInvWrapper(inventory, new EntityHandsInvWrapper(this), new EntityArmorInvWrapper(this));


    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        int x = compound.getInt("WatchBlockX");
        int y = compound.getInt("WatchBlockY");
        int z = compound.getInt("WatchBlockZ");
        this.entityData.set(CELLGUARD_POS, Optional.of(new BlockPos(x, y, z)));
        ListTag listnbt = compound.getList("Inventory", 9);
        for (int i = 0; i < listnbt.size(); ++i) {
            CompoundTag compoundnbt = listnbt.getCompound(i);
            int j = compoundnbt.getByte("Slot") & 255;
            this.cellGuardInventory.setItem(j, ItemStack.of(compoundnbt));
        }

        if (compound.contains("ArmorItems", 9)) {
            ListTag armorItems = compound.getList("ArmorItems", 10);
            for (int i = 0; i < armorItems.size(); ++i) {
                int index = GuardEntity
                        .slotToInventoryIndex(Mob.getEquipmentSlotForItem(ItemStack.of(armorItems.getCompound(i))));
                this.cellGuardInventory.setItem(index, ItemStack.of(armorItems.getCompound(i)));
            }
        }
        if (compound.contains("HandItems", 9)) {
            ListTag handItems = compound.getList("HandItems", 10);
            for (int i = 0; i < handItems.size(); ++i) {
                int handSlot = i == 0 ? 5 : 4;
                this.cellGuardInventory.setItem(handSlot, ItemStack.of(handItems.getCompound(i)));
            }
        }
        if (!level.isClientSide) this.readPersistentAngerSaveData(this.level, compound);

        Tag inventoryCustom = compound.get("Inventory");
        if (inventoryCustom instanceof CompoundTag inventoryTag)
            inventory.deserializeNBT(inventoryTag);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        ListTag listnbt = new ListTag();
        for (int i = 0; i < this.cellGuardInventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.cellGuardInventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundnbt = new CompoundTag();
                compoundnbt.putByte("Slot", (byte) i);
                itemstack.save(compoundnbt);
                listnbt.add(compoundnbt);
            }
            compound.put("Inventory", listnbt);
        }

        compound.putInt("WatchBlockX", this.getWatchBlock().getX());
        compound.putInt("WatchBlockY", this.getWatchBlock().getY());
        compound.putInt("WatchBlockZ", this.getWatchBlock().getZ());

    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot pSlot) {
        switch (pSlot) {
            case HEAD:
                return this.cellGuardInventory.getItem(0);
            case CHEST:
                return this.cellGuardInventory.getItem(1);
            case LEGS:
                return this.cellGuardInventory.getItem(2);
            case FEET:
                return this.cellGuardInventory.getItem(3);
            case OFFHAND:
                return this.cellGuardInventory.getItem(4);
            case MAINHAND:
                return this.cellGuardInventory.getItem(5);
        }
        return ItemStack.EMPTY;
    }

    @Nullable
    public LivingEntity getOwner() {
        try {
            UUID uuid = this.getOwnerId();
            boolean coolGuy = uuid != null && this.level.getPlayerByUUID(uuid) != null;
            return uuid == null || (this.level.getPlayerByUUID(uuid) != null && this.level.getPlayerByUUID(uuid) == null) ? null : this.level.getPlayerByUUID(uuid);
        } catch (IllegalArgumentException illegalargumentexception) {
            return null;
        }
    }

    public boolean isOwner(LivingEntity entityIn) {
        return entityIn == this.getOwner();
    }

    @Nullable
    public UUID getOwnerId() {
        return this.entityData.get(OWNER_UNIQUE_ID).orElse(null);
    }

    public void setOwnerId(@Nullable UUID p_184754_1_) {
        this.entityData.set(OWNER_UNIQUE_ID, Optional.ofNullable(p_184754_1_));
    }


    /*public static int SpawnLocation(LevelAccessor world, BlockPos pos) {
        ReturnToWatchBlock watchBlock = ReturnToWatchBlock.;
    }/*


    /*@Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor worldIn, DifficultyInstance difficultyIn, MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag dataTag) {
        this.setPersistenceRequired();
        int watchBlockX = this.getBlockX();
        int watchBlockY = this.getBlockY();
        int watchBlockZ = this.getBlockZ();

        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
    }*/


    @Nullable
    public BlockPos getWatchBlock() {
        return this.entityData.get(CELLGUARD_POS).orElse(null);
    }

    @Nullable
    public void setWatchBlock(BlockPos position) {
        this.entityData.set(CELLGUARD_POS, Optional.ofNullable(position));
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
        if (source == DamageSource.LAVA)
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(OWNER_UNIQUE_ID, Optional.empty());
        this.entityData.define(CELLGUARD_POS, Optional.empty());
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource source, DifficultyInstance difficultyInstance) {

        for (EquipmentSlot equipmentslottype : EquipmentSlot.values()) {
            for (ItemStack stack : this.getItemsFromLootTable(equipmentslottype)) {
                this.setItemSlot(equipmentslottype, stack);
            }
        }

        this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 100.0F;
        this.handDropChances[EquipmentSlot.OFFHAND.getIndex()] = 100.0F;
    }

    public List<ItemStack> getItemsFromLootTable(EquipmentSlot slot) {
        if (EQUIPMENT_SLOT_ITEMS.containsKey(slot)) {
            LootTable loot = this.level.getServer().getLootTables().get(EQUIPMENT_SLOT_ITEMS.get(slot));
            LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerLevel) this.level)).withParameter(LootContextParams.THIS_ENTITY, this).withRandom(this.getRandom());
            return loot.getRandomItems(lootcontext$builder.create(CellGuardLootTables.SLOT));
        }
        return null;
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return false;
    }

    @Override
    protected void registerGoals() {

        this.targetSelector.addGoal(0, new HurtByTargetGoal(this){
            @Override
            public void start() {
                super.start();
                this.setUnseenMemoryTicks(6000);
            }
            /*@Override
            public HurtByTargetGoal setAlertOthers(Class<?>... p_26045_) {
                return super.setAlertOthers(p_26045_);
            }*/
        });

        this.goalSelector.addGoal(5, new CellGuardMeleeGoal(this, 1.2, true) {
            @Override
            protected int adjustedTickDelay(int p_186072_) {
                return super.adjustedTickDelay(2);
            }
        });
        this.goalSelector.addGoal(10, new ReturnToWatchBlock(this, 1.2));

        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Monster.class, true, true));
        //this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Slime.class, true, true));

        super.registerGoals();
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

        addEffect(new MobEffectInstance(MobEffects.REGENERATION, 1000000000, 3, false, false));


    }

    public static int health = 124;
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
        builder = builder.add(Attributes.MAX_HEALTH, health);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 1);
        builder = builder.add(Attributes.FOLLOW_RANGE, 128);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        builder = builder.add(Attributes.ATTACK_KNOCKBACK, 0.00000001);
        builder = builder.add(Attributes.ARMOR_TOUGHNESS, 0);
        builder = builder.add(ForgeMod.SWIM_SPEED.get(), 3);
        builder = builder.add(ForgeMod.ATTACK_RANGE.get(), 3);
        return builder;
    }

    @Override
    public void setItemSlot(EquipmentSlot slotIn, ItemStack stack) {
        super.setItemSlot(slotIn, stack);
        switch (slotIn) {
            case CHEST:
                if (this.cellGuardInventory.getItem(1).isEmpty())
                    this.cellGuardInventory.setItem(1, new ItemStack(Items.LEATHER_CHESTPLATE));
                break;
            case FEET:
                if (this.cellGuardInventory.getItem(3).isEmpty())
                    this.cellGuardInventory.setItem(3, new ItemStack(Items.LEATHER_BOOTS));
                break;
            case HEAD:
                if (this.cellGuardInventory.getItem(0).isEmpty())
                    this.cellGuardInventory.setItem(0, new ItemStack(Items.LEATHER_HELMET));
                break;
            case LEGS:
                if (this.cellGuardInventory.getItem(2).isEmpty())
                    this.cellGuardInventory.setItem(2, new ItemStack(Items.LEATHER_LEGGINGS));
                break;
            case MAINHAND:
                if (this.cellGuardInventory.getItem(5).isEmpty())
                this.cellGuardInventory.setItem(5, new ItemStack(Items.WOODEN_SWORD));
                break;
        }
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return 6000;
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



    @Override
    public void tick() {
        setCustomName(Component.literal( "Guard " + getHealth()));
        super.tick();

    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }


    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean configValues = true; //this.getOwnerId() != null && this.getOwnerId().equals(player.getUUID());
        boolean inventoryRequirements = !player.isSecondaryUseActive();
        if (inventoryRequirements) {
            if (this.getTarget() != player && this.isEffectiveAi() && configValues) {
                if (player instanceof ServerPlayer) {
                    this.openGui((ServerPlayer) player);
                    return InteractionResult.SUCCESS;
                }
            }
            return InteractionResult.CONSUME;
        }
        return super.mobInteract(player, hand);
    }

    public void openGui(ServerPlayer player) {
        this.setOwnerId(player.getUUID());
        if (player.containerMenu != player.inventoryMenu) {
            player.closeContainer();
        }
        player.nextContainerCounter();
        CellGuardPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), new CellGuardOpenInventoryPacket(player.containerCounter, this.cellGuardInventory.getContainerSize(), this.getId()));
        player.containerMenu = new CellGuardContainer(player.containerCounter, player.getInventory(), this.cellGuardInventory, this);
        player.initMenu(player.containerMenu);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.containerMenu));
    }

    @Override
    public boolean canBeSeenAsEnemy() {
        return true;
    }



    @Override
    public void containerChanged(Container p_18983_) {

    }



}
