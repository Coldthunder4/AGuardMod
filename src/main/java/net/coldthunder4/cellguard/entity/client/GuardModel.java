package net.coldthunder4.cellguard.entity.client;

import net.coldthunder4.cellguard.CellGuard;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public class GuardModel extends LivingEntityRenderer {

    private static final ResourceLocation TEXTURES = new ResourceLocation(CellGuard.MOD_ID, "textures/entities/guard.png");

    public GuardModel(EntityRendererProvider.Context context, EntityModel entityModel, float p_174291_) {
        super(context, entityModel, p_174291_);
    }

    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return TEXTURES;
    }
}
