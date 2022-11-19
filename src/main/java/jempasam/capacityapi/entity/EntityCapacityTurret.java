package jempasam.capacityapi.entity;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.entity.container.EContainerDoubleCapacity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityCapacityTurret extends EntityDoubleCapacity{
	
	
	
	public ItemStack item=null;
	public int reloadTime;
	public int reloadTimer;
	public int repetition;
	public float yawRotation;
	
	
	
	public EntityCapacityTurret(World worldIn) {
		super(worldIn);
		setSize(0.5f, 0.5f);
	}
	
	
	
	@Override
	protected void entityInit() {
		yawRotation=0;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		reloadTimer++;
		rotationYaw+=yawRotation;
		if(reloadTimer>reloadTime) {
			reloadTimer=0;
			repetition--;
			if(repetition>=0) {
				if(world.isRemote) Capacity.spawnParticleBomb(ccontainer.red, ccontainer.green, ccontainer.blue, getPositionVector(), 5);
				else {
					if(ccontainer.capacity()!=null)ccontainer.use(null);
					if(repetition==0 && ccontainer.capacity2()==null)setDead();
				}
			}
			else {
				if(world.isRemote) Capacity.spawnParticleBomb(ccontainer.red2, ccontainer.green2, ccontainer.blue2, getPositionVector(), 5);
				else if(ccontainer.capacity2()!=null){
					ccontainer.use2(null);
					setDead();
				}
				else setDead();
			}
		}
		else if(world.isRemote) {
			if(this.ticksExisted%4==0)Capacity.spawnParticle(ccontainer.red2, ccontainer.green2, ccontainer.blue2, getPositionVector());
			else Capacity.spawnParticle(ccontainer.red, ccontainer.green, ccontainer.blue, getPositionVector());
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		reloadTime=compound.getInteger("reloadTime");
		reloadTimer=compound.getInteger("reloadTimer");
		repetition=compound.getInteger("repetition");
		yawRotation=compound.getFloat("yawRotation");
		ccontainer.deserializeNBT(compound.getCompoundTag("capacity"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setFloat("yawRotation", yawRotation);
		compound.setInteger("reloadTime", reloadTime);
		compound.setInteger("reloadTimer", reloadTimer);
		compound.setInteger("repetition", repetition);
		compound.setTag("capacity", ccontainer.serializeNBT());
	}
	
	@Override
	public void readSpawnData(ByteBuf buffer) {
		super.readSpawnData(buffer);
		reloadTime=buffer.readInt();
		reloadTimer=buffer.readInt();
		ccontainer.readSpawnData(buffer);
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeFloat(reloadTime);
		buffer.writeInt(reloadTimer);
		ccontainer.writeSpawnData(buffer);
	}
	
}
