package jempasam.mcsam;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;

public class RenderUtils {
	private RenderUtils() {}
	
	public static void renderItem(ItemStack stack) {
		Minecraft mc=Minecraft.getMinecraft();
		if(!stack.isEmpty()) {
			GlStateManager.pushMatrix();
			mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
			mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, false);
			GlStateManager.enableRescaleNormal();
            GlStateManager.enableAlpha();
            GlStateManager.alphaFunc(516, 0.1F);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);            
            GlStateManager.translate((float)0, (float)0, 100.0F);
            GlStateManager.enableLighting();
            
            IBakedModel model=mc.getRenderItem().getItemModelWithOverrides(stack, null, null);
            model = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(model, TransformType.GUI, false);
            mc.getRenderItem().renderItem(stack, model);
            
            GlStateManager.disableAlpha();
            GlStateManager.disableRescaleNormal();
            GlStateManager.disableLighting();
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            mc.getTextureManager().getTexture(TextureMap.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
		}
	}
}
