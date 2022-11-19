package jempasam.capacityapi.entity;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCapacityTrap extends EntityCapacity{
	
	
	
	public ItemStack item=null;
	public int timeout;
	public int life;
	public boolean self=false;
	public float range;
	
	
	
	public EntityCapacityTrap(World worldIn) {
		super(worldIn);
		setSize(0.5f, 0.5f);
	}
	
	
	
	@Override protected void entityInit() { }
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(timeout>0) {
			timeout--;
			if(world.isRemote)Capacity.spawnParticle(0.2f, 0.2f, 0.2f, this.getPositionVector());
			return;
		}
        Vec3d pos=getPositionVector();
        AxisAlignedBB bbox=new AxisAlignedBB(pos.subtract(range, range, range), pos.addVector(range, range, range));
		if(world.isRemote) {
            Capacity.spawnParticleZone(capacities().red, capacities().green, capacities().blue, bbox, (int)(3+3*range));
		}
		else {
			this.world.getEntitiesWithinAABB(Entity.class, bbox, e->e!=this).stream().findAny().ifPresent(entity->{
				capacities().capacity().use(getContext(entity));
				life--;
				if(life<=0)this.setDead();
				else {
					timeout=150;
				}
			});
		}
	}
	
	@Override
	protected CapacityContext getContext(Entity target) {
		if(self)return super.getContext(this);
		else return super.getContext(target);
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		range=compound.getFloat("range");
		timeout=compound.getInteger("timeout");
		life=compound.getInteger("life");
		self=compound.getBoolean("self");
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setFloat("range", range);
		compound.setInteger("timeout", timeout);
		compound.setInteger("life", life);
		compound.setBoolean("self", self);
	}
	
	@Override
	public void readSpawnData(ByteBuf buffer) {
		super.readSpawnData(buffer);
		range=buffer.readFloat();
		timeout=buffer.readInt();
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		buffer.writeFloat(range);
		buffer.writeInt(timeout);
	}
	
}
