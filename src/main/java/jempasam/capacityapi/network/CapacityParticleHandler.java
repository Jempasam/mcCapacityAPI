package jempasam.capacityapi.network;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CapacityParticleHandler implements IMessageHandler<CapacityParticleHandler.CapacityParticleMessage, IMessage>{
	
	
	
	public CapacityParticleMessage lastMessage;
	
	public IMessage onMessage(CapacityParticleMessage message, MessageContext ctx) {
		if(Minecraft.getMinecraft().world.provider.getDimension()==message.level) {
			if(message.repeat>1) {
				message.repeat--;
				lastMessage=message;
			}
			if(message.type==0) Capacity.spawnParticle(message.r, message.g, message.b, new Vec3d(message.x, message.y, message.z));
			else if(message.type==1) Capacity.spawnParticleBomb(message.r, message.g, message.b, new Vec3d(message.x, message.y, message.z),message.sup);
			else if(message.type==2) Capacity.spawnParticleLine(message.r, message.g, message.b, new Vec3d(message.x, message.y, message.z), new Vec3d(message.x2, message.y2, message.z2), message.sup);
			else if(message.type==3) Capacity.spawnParticleZone(message.r, message.g, message.b, new AxisAlignedBB(message.x, message.y, message.z, message.x2, message.y2, message.z2), message.sup);
			else if(message.type==4) Capacity.spawnParticle(message.r, message.g, message.b, new Vec3d(message.x, message.y, message.z), new Vec3d(message.x2, message.y2, message.z2));
		}
		return null;
	}
	
	public static class CapacityParticleMessage implements IMessage{
		public int type;
		public int level;
		public float x;
		public float y;
		public float z;
		public float x2;
		public float y2;
		public float z2;
		public int sup;
		public int repeat=0;
		public float r;
		public float g;
		public float b;



		public void fromBytes(ByteBuf buf) {
			type=buf.readInt();
			level=buf.readInt();
			sup=buf.readInt();
			x=buf.readFloat();
			y=buf.readFloat();
			z=buf.readFloat();
			x2=buf.readFloat();
			y2=buf.readFloat();
			z2=buf.readFloat();
			r=buf.readFloat();
			g=buf.readFloat();
			b=buf.readFloat();
			repeat=buf.readInt();
		}
		public void toBytes(ByteBuf buf) {
			buf.writeInt(type);
			buf.writeInt(level);
			buf.writeInt(sup);
			buf.writeFloat(x);
			buf.writeFloat(y);
			buf.writeFloat(z);
			buf.writeFloat(x2);
			buf.writeFloat(y2);
			buf.writeFloat(z2);
			buf.writeFloat(r);
			buf.writeFloat(g);
			buf.writeFloat(b);
			buf.writeInt(repeat);
		}
	}
}
