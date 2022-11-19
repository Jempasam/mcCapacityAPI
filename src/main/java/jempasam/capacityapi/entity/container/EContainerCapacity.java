package jempasam.capacityapi.entity.container;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EContainerCapacity implements IEntityAdditionalSpawnData, INBTSerializable<NBTTagCompound>{
	
	
	
	private Capacity capacity=null;
	@SideOnly(Side.CLIENT)public float red;
	@SideOnly(Side.CLIENT)public float green;
	@SideOnly(Side.CLIENT)public float blue;
	@SideOnly(Side.CLIENT)public boolean hasCapacity=false;
	
	private Entity holder;
	private UUID sender;
	private UUID marked;
	private int power;
	
	
	
	public EContainerCapacity(Entity holder) {
		this.holder=holder;
	}
	
	
	
	public void fill(Capacity capacity, Entity sender, Entity marked, int power) {
		this.sender= sender==null ? null : sender.getUniqueID();
		this.marked= marked==null ? null : marked.getUniqueID();
		this.power=power;
		this.capacity=capacity;
	}
	
	public void fill(Capacity capacity, CapacityContext context) {
		fill(capacity, context.getSender(), context.getMarked(), context.getPower());
	}
	
	public Capacity capacity() {
		return capacity;
	}
	
	public void use(Entity target) {
		capacity.use(getContext(target));
	}
	
	public CapacityContext getContext(Entity target) {
		Entity sender=getSender();
		CapacityContext ret=CapacityContext.atTarget(sender==null ? holder : sender, target==null ? holder : target, power);
		ret.mark(getMarked());
		return ret;
	}
	
	
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		capacity=getCapacity(nbt, "capacity");
		sender=nbt.getUniqueId("sender");
		marked=nbt.getUniqueId("marked");
		power=nbt.getInteger("power");
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt=new NBTTagCompound();
		setCapacity(nbt, "capacity", capacity);
		nbt.setUniqueId("marked", marked);
		nbt.setUniqueId("sender", sender);
		nbt.setInteger("power", power);
		return nbt;
	}
	
	@Override
	public void readSpawnData(ByteBuf buffer) {
		power=buffer.readInt();
		red=buffer.readFloat();
		if(red!=-1) {
			green=buffer.readFloat();
			blue=buffer.readFloat();
			hasCapacity=true;
		}
		else {red=0.2f; green=0.2f; blue=0.2f;}
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		buffer.writeInt(power);
		writeCapacityColor(capacity, buffer);
	}
	
	protected void writeCapacityColor(Capacity capacity, ByteBuf buffer) {
		if(capacity==null) {
			buffer.writeFloat(-1);
		}
		else {
			int color=capacity.getColor(CapacityContext.withPower(1));
			buffer.writeFloat((float)(color >> 16 & 255) / 255.0f);
			buffer.writeFloat((float)(color >> 8 & 255) / 255.0f);
			buffer.writeFloat((float)(color >> 0 & 255) / 255.0f);
		}
	}
	
	
	
	protected Capacity getCapacity(NBTTagCompound nbt, String name) {
		String cname=nbt.getString(name);
		if(cname==null)return null;
		else return CAPIRegistry.CAPACITIES.get(cname);
	}
	
	protected Entity getEntity(NBTTagCompound nbt, String name) {
		int id=nbt.getInteger(name);
		if(id==0)return null;
		else return holder.world.getEntityByID(id);
	}
	
	public int getPower() {
		return power;
	}
	
	public Entity getSender() {
		return sender==null ? null : holder.getServer().getEntityFromUuid(sender);
	}
	
	public UUID getSenderUUID() {
		return sender;
	}
	
	public Entity getMarked() {
		return marked==null ? null : holder.getServer().getEntityFromUuid(marked);
	}
	
	public UUID getMarkedUUID() {
		return marked;
	}
	
	protected void setCapacity(NBTTagCompound nbt, String name, Capacity capacity) {
		if(capacity!=null) {
			nbt.setString(name, CAPIRegistry.CAPACITIES.idOf(capacity));
		}
	}
	
	protected void setEntity(NBTTagCompound nbt, String name, Entity entity) {
		if(entity!=null) {
			nbt.setInteger(name, entity.getEntityId());
		}
	}
}
