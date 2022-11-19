package jempasam.capacityapi.capacity;

import java.util.Random;

import jempasam.capacityapi.gui.GuiMana;
import jempasam.capacityapi.network.CapacityNetwork;
import jempasam.capacityapi.utils.ColorUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public interface Capacity {
	public static Random rand=new Random(System.currentTimeMillis());
	
	boolean use(CapacityContext context);
	int getColor(CapacityContext context);
	String getName(CapacityContext context);
	int getMana(CapacityContext context);
	
	default void drawIcon(CapacityContext context, int x, int y, int width, int height) {
		float f = 0.00390625F;
        Minecraft.getMinecraft().getTextureManager().bindTexture(GuiMana.MAGIC_GUI);
		int color[]=ColorUtils.asRGB(getColor(context));
		GlStateManager.color(color[0]/255f,color[1]/255f,color[2]/255f,1f);
		GlStateManager.enableAlpha();
		Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x+width, y, 1.0).tex(39*f, 0).endVertex();
        bufferbuilder.pos(x, y, 1.0).tex(22*f, 0).endVertex();
        bufferbuilder.pos(x, y+height, 1.0).tex(22*f, 17*f).endVertex();
        bufferbuilder.pos(x+width, y+height, 1.0).tex(39*f, 17*f).endVertex();
        tessellator.draw();
        GlStateManager.color(1f, 1f, 1f);
        GlStateManager.disableAlpha();
	}
	
	default Particle spawnParticle(CapacityContext context, Vec3d position) {
		int color=getColor(context);
        return spawnParticle((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, position);
	}
	
	default Particle spawnParticle(CapacityContext context, Vec3d position, Vec3d move) {
		int color=getColor(context);
        return spawnParticle((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, position, move);
	}
	
	default void spawnParticleBomb(CapacityContext context, Vec3d position, int count) {
		int color=getColor(context);
        spawnParticleBomb((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, position, count);
	}
	
	default void spawnParticleLine(CapacityContext context, Vec3d start, Vec3d end, int count) {
		int color=getColor(context);
		spawnParticleLine((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, start, end, count);
	}
	
	default void spawnParticleZone(CapacityContext context, AxisAlignedBB box, int count) {
		Random random=new Random();
		double offX=box.maxX-box.minX;
		double offY=box.maxY-box.minY;
		double offZ=box.maxZ-box.minZ;
		for(int i=0; i<count; i++) {
			spawnParticle(context, new Vec3d(
					box.minX+random.nextDouble()*offX,
					box.minY+random.nextDouble()*offY,
					box.minZ+random.nextDouble()*offZ
			));
		}
	}
	
	public static Particle spawnParticle(float red, float green, float blue, Vec3d position) {
		Particle particle = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.SPELL_INSTANT.getParticleID(), position.x, position.y, position.z, 0, 0, 0);
        particle.setRBGColorF(red, green, blue);
        return particle;
	}
	
	public static Particle spawnParticle(float red, float green, float blue, Vec3d position,  Vec3d move) {
		Particle particle = Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.CLOUD.getParticleID(), position.x, position.y, position.z, move.x, move.y, move.z);
        particle.setRBGColorF(red, green, blue);
        return particle;
	}
	
	public static void spawnParticleBomb(float red, float green, float blue, Vec3d position, int count) {
		Random random=new Random();
		for(int i=0; i<count; i++) {
			spawnParticle(red, green, blue, position, new Vec3d( random.nextDouble()/2f-0.25f, random.nextDouble()/2f-0.25f, random.nextDouble()/2f-0.25f ));
		}
	}
	
	public static void spawnParticleLine(float red, float green, float blue, Vec3d start, Vec3d end, int count) {
		Vec3d offset=end.subtract(start).scale(1f/count);
		for(int i=0; i<=count; i++) {
			spawnParticle(red, green, blue, start.add(offset.scale(i)));
		};
	}
	
	public static void spawnParticleZone(float red, float green, float blue, AxisAlignedBB box, int count) {
		Random random=new Random();
		double offX=box.maxX-box.minX;
		double offY=box.maxY-box.minY;
		double offZ=box.maxZ-box.minZ;
		for(int i=0; i<count; i++) {
			spawnParticle(red, green, blue, new Vec3d(
					box.minX+random.nextDouble()*offX,
					box.minY+random.nextDouble()*offY,
					box.minZ+random.nextDouble()*offZ
			));
		}
	}
	
	public static void sendParticle(float red, float green, float blue, Vec3d position) {
		CapacityNetwork.sendParticle(0, position, position, red, green, blue, 0, 0);
	}
	
	public static void sendParticle(float red, float green, float blue, Vec3d position,  Vec3d move) {
		CapacityNetwork.sendParticle(4, position, move, red, green, blue, 0, 0);
	}
	
	public static void sendParticleBomb(float red, float green, float blue, Vec3d position, int count) {
		CapacityNetwork.sendParticle(1, position, position, red, green, blue, count, 0);
	}
	
	public static void sendParticleLine(float red, float green, float blue, Vec3d start, Vec3d end, int count) {
		CapacityNetwork.sendParticle(2, start, end, red, green, blue, count, 0);
	}
	
	public static void sendParticleZone(float red, float green, float blue, AxisAlignedBB box, int count) {
		CapacityNetwork.sendParticle(3, new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.maxX, box.maxY, box.maxZ), red, green, blue, count, 0);
	}
	
	public static void sendParticleZone(float red, float green, float blue, AxisAlignedBB box, int count, int repeat) {
		CapacityNetwork.sendParticle(3, new Vec3d(box.minX, box.minY, box.minZ), new Vec3d(box.maxX, box.maxY, box.maxZ), red, green, blue, count, repeat);
	}
	
	
	
	
	default void sendParticle(CapacityContext context, Vec3d position) {
		int color=getColor(context);
		sendParticle((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, position);
	}
	
	default void sendParticle(CapacityContext context, Vec3d position,  Vec3d move) {
		int color=getColor(context);
		sendParticle((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, position, move);
	}
	
	default void sendParticleBomb(CapacityContext context, Vec3d position, int count) {
		int color=getColor(context);
		sendParticleBomb((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, position, count);
	}
	
	default void sendParticleLine(CapacityContext context, Vec3d start, Vec3d end, int count) {
		int color=getColor(context);
		sendParticleLine((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, start, end, count);
	}
	
	default void sendParticleZone(CapacityContext context, AxisAlignedBB box, int count) {
		int color=getColor(context);
		sendParticleZone((color >> 16 & 255) / 255.0f, (color >> 8 & 255) / 255.0f, (color >> 0 & 255) / 255.0f, box, count);
	}
}
