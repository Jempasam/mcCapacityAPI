package jempasam.capacityapi.gui;

import org.lwjgl.opengl.GL11;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

@SideOnly(Side.CLIENT)
public class GuiMana extends Gui{
	
	
	
	private static final Minecraft mc=Minecraft.getMinecraft();
	private static int mana=500;
	private static int maxmana=1000;
	private static long texttimer=0;
	private static long bartimer=0;
	private static Capacity[] capacities;
	private static int[] manacolor=new int[] {0x00ffff};
	private static final ResourceLocation BARS = new ResourceLocation("textures/gui/bars.png");
	public static final ResourceLocation MAGIC_GUI = new ResourceLocation(CapacityAPI.MODID, "textures/gui/selector.png");
	
	
	
	public GuiMana() {
		super();
	}
	
	
	
	public static void setMana(int mana, int maxmana, int[] manacolor) {
		if(mana!=GuiMana.mana) displayBar();
		GuiMana.mana=mana;
		GuiMana.maxmana=maxmana;
		GuiMana.manacolor=manacolor;
	}
	
	public static void setCapacities(Capacity[] capacities) {
		GuiMana.capacities=capacities;
	}
	
	public static void displayText() {
		bartimer=System.currentTimeMillis();
		texttimer=System.currentTimeMillis();
	}
	
	public static void displayBar() {
		bartimer=System.currentTimeMillis();
	}
	
	@SubscribeEvent
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
		    int i1 = 91 - mc.fontRenderer.getStringWidth(s)/2 + 5;
		    int j1 = 12;
		    mc.fontRenderer.drawString(s, i1, j1, color, true);
		}
		mc.mcProfiler.endSection();
		
		mc.mcProfiler.startSection("manatext");
		if(System.currentTimeMillis()-bartimer<6000) {
			mc.getTextureManager().bindTexture(BARS);
			int k = (int)(mana * 183.0F / (maxmana+1));
			int l = 5;
			int x =5;
			this.drawTexturedModalRect(x, l, 0, 60, 182, 5);
			if (k > 0) this.drawTexturedModalRect(x, l, 0, 65, k, 5);
		}
		mc.mcProfiler.endSection();
		
		/*mc.mcProfiler.startSection("magicselector");
		mc.getTextureManager().bindTexture(MAGIC_GUI);
		int x=5;
		int y=15;
		this.drawTexturedModalRect(x, y, 0, 0, 22, 62);
		for(int i=0; i<capacities.length; i++) {
			if(capacities[i]!=null) {
				capacities[i].drawIcon(CapacityContext.withPower(1), x+3, y+20*i+3, 16, 16);
			}
		}
		mc.mcProfiler.endSection();*/
		
		GL11.glColor3f(1f, 1f, 1f);
		mc.getTextureManager().bindTexture(ICONS);
	}

}
