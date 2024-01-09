package net.coldthunder4.cellguard.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.coldthunder4.cellguard.CellGuard;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.coldthunder4.cellguard.entity.CellGuardContainer;
import net.coldthunder4.cellguard.entity.custom.GuardEntity;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class CellGuardInventoryScreen extends AbstractContainerScreen<CellGuardContainer> {
    private static final ResourceLocation GUARD_GUI_TEXTURES = new ResourceLocation(CellGuard.MOD_ID, "textures/container/inventory.png");

    private final GuardEntity cellGuard;
    private Player player;
    private float mousePosX;
    private float mousePosY;
    private boolean buttonPressed;
    public CellGuardInventoryScreen(CellGuardContainer container, Inventory playerInventory, GuardEntity cellGuard) {
        super(container, playerInventory, cellGuard.getDisplayName());
        this.cellGuard = cellGuard;
        this.titleLabelX = 80;
        this.inventoryLabelX = 100;
        this.player = playerInventory.player;
    }

    @Override
    protected void renderBg(PoseStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUARD_GUI_TEXTURES);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
        InventoryScreen.renderEntityInInventory(i + 51, j + 75, 30, (float) (i + 51) - this.mousePosX, (float) (j + 75 - 50) - this.mousePosY, this.cellGuard);
    }

    @Override
    protected void renderLabels(PoseStack matrixStack, int x, int y) {
        super.renderLabels(matrixStack, x, y);
        int health = Mth.ceil(cellGuard.getHealth());
        int armor = cellGuard.getArmorValue();
        Component guardHealthText = Component.translatable("guardinventory.health", health);
        Component guardArmorText = Component.translatable("guardinventory.armor", armor);
        this.font.draw(matrixStack, guardHealthText, 80.0F, 20.0F, 4210752);
        this.font.draw(matrixStack, guardArmorText, 80.0F, 30.0F, 4210752);
    }

    @Override
    public void render(PoseStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.mousePosX = (float) mouseX;
        this.mousePosY = (float) mouseY;
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

}
