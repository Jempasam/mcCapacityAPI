package jempasam.capacityapi.network;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.gui.GuiMana;
import jempasam.capacityapi.network.CapacityKeyHandler.CapacityKeyMessage;
import jempasam.capacityapi.network.CapacityManaHandler.CapacityManaMessage;
import jempasam.capacityapi.network.CapacityParticleHandler.CapacityParticleMessage;
import jempasam.capacityapi.network.CapacityPlayerMoveHandler.CapacityPlayerMoveMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class CapacityNetwork {
	
	
	
	private static SimpleNetworkWrapper CHANNEL;
	public static CapacityParticleHandler CAPACITY;
	
	
	
	public static void init() {
		CHANNEL=NetworkRegistry.INSTANCE.newSimpleChannel(CapacityAPI.MODID);
		
		CAPACITY=new CapacityParticleHandler();
		
		CHANNEL.registerMessage(new CapacityKeyHandler(), CapacityKeyMessage.class, 1, Side.SERVER);
		CHANNEL.registerMessage(CAPACITY, CapacityParticleMessage.class, 2, Side.CLIENT);
		CHANNEL.registerMessage(new CapacityManaHandler(), CapacityManaMessage.class, 3, Side.CLIENT);
		CHANNEL.registerMessage(new CapacityPlayerMoveHandler(), CapacityPlayerMoveMessage.class, 4, Side.CLIENT);
	}
	
	public static void sendMana(EntityPlayerMP player, int mana, int manamax, int[] manacolor) {
		CapacityManaMessage message=new CapacityManaMessage();
		message.color=manacolor;
		message.mana=mana;
		message.manamax=manamax;
		CHANNEL.sendTo(message, player);
	}
	
	public static void sendPositionAndMotion(EntityPlayerMP player) {
		CapacityPlayerMoveMessage message=new CapacityPlayerMoveMessage();
		message.x=player.posX;
		message.y=player.posY;
		message.z=player.posZ;
		message.dx=(float)player.motionX;
		message.dy=(float)player.motionY;
		message.dz=(float)player.motionZ;
		message.yaw=player.rotationYaw;
		message.pitch=player.rotationPitch;
		CHANNEL.sendTo(message, player);
	}
	
	public static void sendMana(EntityPlayerMP player, ICapacityOwner owner) {
		sendMana(player, owner.getMana(), owner.getMaxMana(), owner.getManaColors());
	}
	
	public static void sendParticle(int type, Vec3d position, Vec3d position2, float red, float green, float blue, int sup, int repeat) {
		CapacityParticleMessage message=new CapacityParticleMessage();
		message.x=(float)position.x;
		message.y=(float)position.y;
		message.z=(float)position.z;
		message.x2=(float)position2.x;
		message.y2=(float)position2.y;
		message.z2=(float)position2.z;
		message.type=type;
		message.sup=sup;
		message.r=red;
		message.g=green;
		message.b=blue;
		message.repeat=repeat+1;
		CHANNEL.sendToAll(message);
	}
	
	public static void sendKey(int code, int info) {
		GuiMana.displayText();
		CapacityKeyMessage message=new CapacityKeyMessage();
		message.code=code;
		message.info=info;
		message.entity=Minecraft.getMinecraft().player;
		message.level=Minecraft.getMinecraft().world;
		CHANNEL.sendToServer(message);
	}
}
