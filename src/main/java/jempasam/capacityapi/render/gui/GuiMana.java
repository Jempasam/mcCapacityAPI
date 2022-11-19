package jempasam.capacityapi.render.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class GuiMana extends Gui{
	
	
	
	private static final Minecraft mc=Minecraft.getMinecraft();
	private static int mana=500;
	private static int maxmana=1000;
	private static long texttimer=0;
	private static long bartimer=0;
	private static int[] manacolor=new int[] {0x00ffff};
	private static final ResourceLocation BARS = new ResourceLocation("textures/gui/bars.png");
	
	
	
	public GuiMana() {
		super();
	}
	
	
	
	public static void setMana(int mana, int maxmana, int[] manacolor) {
		if(mana!=GuiMana.mana) displayBar();
		GuiMana.mana=mana;
		GuiMana.maxmana=maxmana;
		GuiMana.manacolor=manacolor;
	}
	
	public static void displayText() {
		bartimer=System.currentTimeMillis();
		texttimer=System.currentTimeMillis();
	}
	
	public static void displayBar() {
		bartimer=System.currentTimeMillis();
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderPreOverlay(RenderGameOverlayEvent.Post event) {
		renderManaBar(event.getResolution());
	}
	
	public void renderManaBar(ScaledResolution scaledRes) {
		int color=manacolor[(int)(System.currentTimeMillis()%(199*manacolor.length)/200)];
		float red = (float)(color >> 16 & 255) / 255.0F;
		float green = (float)(color >> 8 & 255) / 255.0F;
		float blue = (float)(color & 255) / 255.0F;
		GL11.glColor4f(red, green, blue, 0.0f);
		
		mc.mcProfiler.startSection("magicLvl");
		if(System.currentTimeMillis()-texttimer<3000) {
			String s = mana+"/" + maxmana;
		    int i1 = (scaledRes.getScaledWidth() - mc.fontRenderer.getStringWidth(s)) / 2;
		    int j1 = scaledRes.getScaledHeight() - 55;
		    mc.fontRenderer.drawString(s, i1, j1, color);
		}
		mc.mcProfiler.endSection();
		
		mc.mcProfiler.startSection("magicBar");
		if(System.currentTimeMillis()-bartimer<6000) {
			mc.getTextureManager().bindTexture(BARS);
			int j = 182;
			int k = (int)(mana * 183.0F / (maxmana+1));
			int l = scaledRes.getScaledHeight() - 45;
			int x = scaledRes.getScaledWidth()/2-91;
			this.drawTexturedModalRect(x, l, 0, 60, 182, 5);
			if (k > 0) this.drawTexturedModalRect(x, l, 0, 65, k, 5);
		}
		GL11.glColor3f(1f, 1f, 1f);
		mc.mcProfiler.endSection();
		mc.getTextureManager().bindTexture(ICONS);
	}

}
