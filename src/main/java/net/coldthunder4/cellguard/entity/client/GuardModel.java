package net.coldthunder4.cellguard.entity.client;

import net.coldthunder4.cellguard.CellGuard;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

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
