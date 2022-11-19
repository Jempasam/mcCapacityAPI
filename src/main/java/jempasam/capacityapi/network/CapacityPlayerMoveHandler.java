package jempasam.capacityapi.network;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.gui.GuiMana;
import jempasam.mcsam.BufferUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.actors.threadpool.Arrays;

public class CapacityPlayerMoveHandler implements IMessageHandler<CapacityPlayerMoveHandler.CapacityPlayerMoveMessage, IMessage>{
	
	@Override
	public IMessage onMessage(CapacityPlayerMoveMessage message, MessageContext ctx) {
		EntityPlayerSP player=Minecraft.getMinecraft().player;
		player.setPosition(message.x, message.y, message.z);
		player.motionX=message.dx;
		player.motionY=message.dy;
		player.motionZ=message.dz;
		player.rotationPitch=message.pitch;
		player.rotationYaw=message.yaw;
		return null;
	}
	
	public static class CapacityPlayerMoveMessage implements IMessage{
		public double x;
		public double y;
		public double z;
		
		public float pitch;
		public float yaw;
		
		public float dx;
		public float dy;
		public float dz;
		
		public void fromBytes(ByteBuf buf) {
			x=buf.readDouble();
			y=buf.readDouble();
			z=buf.readDouble();
			
			pitch=buf.readFloat();
			yaw=buf.readFloat();
			
			dx=buf.readFloat();
			dy=buf.readFloat();
			dz=buf.readFloat();
		}
		public void toBytes(ByteBuf buf) {
			buf.writeDouble(x);
			buf.writeDouble(y);
			buf.writeDouble(z);
			
			buf.writeFloat(pitch);
			buf.writeFloat(yaw);
			
			buf.writeFloat(dx);
			buf.writeFloat(dy);
			buf.writeFloat(dz);
		}
	}
}
