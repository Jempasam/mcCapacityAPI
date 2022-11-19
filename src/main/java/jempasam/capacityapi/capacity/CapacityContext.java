package jempasam.capacityapi.capacity;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CapacityContext implements IMessage{
	
	private Vec3d pos;
	private Vec2f rotation;
	
	private Entity marked;
	private Entity sender;
	private Entity target;
	
	private World world;
	private int power;
	
	
	
	public CapacityContext(Vec3d pos, Vec2f rotation, Entity sender, Entity target, World world, int power) {
		super();
		this.pos = pos;
		this.rotation=rotation;
		this.sender = sender;
		this.marked = target;
		this.target=target;
		this.world = world;
		this.power=power;
	}
	
	public CapacityContext(Vec3d pos, Vec2f rotation, Entity sender, Entity marked, Entity target, World world, int power) {
		super();
		this.pos = pos;
		this.rotation=rotation;
		this.sender = sender;
		this.marked = marked;
		this.target=target;
		this.world = world;
		this.power=power;
	}
	
	public CapacityContext(ByteBuf buf) {
		super();
		fromBytes(buf);
	}
	
	
	
	public Entity getMarked(){
		return marked;
	}
	
	public void mark(Entity e) {
		marked=e;
	}
	
	public static CapacityContext atSender(Entity sender, int power) {
		return new CapacityContext(sender.getPositionVector().add(new Vec3d(0, sender.getEyeHeight(), 0)), sender.getPitchYaw(), sender, sender, sender.getEntityWorld(), power);
	}
	
	public static CapacityContext atTarget(Entity sender, Entity target, int power) {
		return new CapacityContext(target.getPositionVector(), target.getPitchYaw(), sender, target, target.getEntityWorld(), power);
	}
	
	public static CapacityContext atPosition(Entity sender, Vec3d position, int power) {
		return new CapacityContext(position, new Vec2f(0, 0), sender, null, sender.getEntityWorld(), power);
	}
	
	public static CapacityContext atPositionRotation(Entity sender, Vec3d position, Vec2f rotation, int power) {
		return new CapacityContext(position, rotation, sender, sender, sender.getEntityWorld(), power);
	}
	
	public static CapacityContext atBlock(Entity sender, BlockPos position, int power) {
		Vec3d targetpos=new Vec3d(position.getX()+0.5f,position.getY()+0.5f,position.getZ()+0.5f);
		return new CapacityContext(targetpos, sender.getPitchYaw(), sender, sender, sender.getEntityWorld(), power);
	}
	
	public static CapacityContext withPower(int power) {
		return new CapacityContext(null, null, null, null, null, power);
	}
	
	public CapacityContext clone() {
		return new CapacityContext(pos, rotation, sender, marked, target, world, power);
	}
	
	
	
	
	public Vec3d getPos() {
		return pos;
	}
	
	public void setPos(Vec3d pos) {
		this.pos = pos;
	}
	
	public Entity getSender() {
		return sender;
	}
	
	public void setSender(Entity sender) {
		this.sender = sender;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}

	public Vec2f getRotation() {
		return rotation;
	}

	public void setRotation(Vec2f rotation) {
		this.rotation = rotation;
	}

	public Entity getTarget() {
		return target;
	}

	public void setTarget(Entity target) {
		this.target = target;
	}
	
	@Override
	public void toBytes(ByteBuf buf) {
		System.out.println("ENTITY5 "+sender);
		buf.writeDouble(pos.x);
		buf.writeDouble(pos.y);
		buf.writeDouble(pos.z);
		buf.writeFloat(rotation.x);
		buf.writeFloat(rotation.y);
		buf.writeInt(world.provider.getDimension());
		buf.writeInt(target.getEntityId());
		buf.writeInt(sender.getEntityId());
		buf.writeInt(power);
		if(marked==null)buf.writeInt(-1);
		else buf.writeInt(marked.getEntityId());
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		pos=new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
		rotation=new Vec2f(buf.readFloat(), buf.readFloat());
		world=buf.readInt()!=Minecraft.getMinecraft().world.provider.getDimension() ? null : Minecraft.getMinecraft().world;
		if(world!=null) {
			target=world.getEntityByID(buf.readInt());
			sender=world.getEntityByID(buf.readInt());
			power=buf.readInt();
		}
		int id=buf.readInt();
		if(id==-1)marked=null;
		else marked=world.getEntityByID(id);
	}
	
	@Override
	public String toString() {
		StringBuilder sb=new StringBuilder();
		sb.append("(").append(pos).append(")[").append(rotation.x).append(",").append(rotation.y).append("]").append(" power of ").append(power).append(" in ").append(world.provider.getDimension());
		return sb.toString();
	}
	
	
	
}
