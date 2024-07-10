package net.architects.itemdisplay.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TESRIndestructableAltarDisplay implements BlockEntityRenderer<IndestructableAltarDisplayTile> {

    public TESRIndestructableAltarDisplay(BlockEntityRendererProvider.Context dispatcher) {
    }

    public void renderItem(IndestructableAltarDisplayTile tile, ItemStack stack, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemRenderer renderer = Minecraft.getInstance().getItemRenderer();
        matrix.pushPose();

        matrix.translate(0.5D, 0.96D, 0.5D);
        matrix.scale(0.8F, 0.8F, 0.8F);

        if (!tile.getBlockState().getValue(IndestructibleAltarDisplayBlock.IS_REVERSE)) {
//            matrix.translate(0, 0D, 0);
//            matrix.scale(0.6F, 0.6F, 0.6F);
            switch (tile.getBlockState().getValue(IndestructibleAltarDisplayBlock.FACING)) {
                case WEST, EAST -> this.rotateItem(matrix, 90, 0f, 90f); // default values
                case NORTH, SOUTH -> this.rotateItem(matrix, 90, 180f, 0f);
            }
        } else {
            switch (tile.getBlockState().getValue(IndestructibleAltarDisplayBlock.FACING)) {
                case WEST, EAST -> this.rotateItem(matrix, 90f, 0f, 270f); // default values
                case NORTH, SOUTH -> this.rotateItem(matrix, 90f, 180f, 180f);
                //around global X, Z, Y
            }
        }

        renderer.renderStatic(stack,ItemDisplayContext.FIXED,combinedLight, combinedOverlay, matrix, buffer, null, 1);
        matrix.popPose();
    }

    private void rotateItem(PoseStack matrix, float a, float b, float c) {
        matrix.mulPose(Axis.XP.rotationDegrees(a));
        matrix.mulPose(Axis.YP.rotationDegrees(b));
        matrix.mulPose(Axis.ZP.rotationDegrees(c));
    }

    @Override
    public void render(IndestructableAltarDisplayTile tile, float partialTicks, PoseStack matrix, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        ItemStack displayItem = tile.getItem();
        if (!displayItem.isEmpty()) {
            this.renderItem(tile, displayItem, partialTicks, matrix, buffer, combinedLight, combinedOverlay);
        }
    }

    @Override
    public boolean shouldRenderOffScreen(IndestructableAltarDisplayTile p_112306_) {
        return true;
    }

    @Override
    public int getViewDistance() {
        return 256;
    }

}