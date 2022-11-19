package jempasam.capacityapi.entity;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.container.EContainerDoubleCapacity;
import jempasam.capacityapi.entity.container.IEntityCapacity;
import jempasam.capacityapi.entity.render.IColorable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityCapacityProjectile extends EntityArrow implements IEntityAdditionalSpawnData, IColorable, IEntityCapacity<EContainerDoubleCapacity>{
	
	
	
	public EContainerDoubleCapacity capacities;
	public int age;
	public int maxage=-1;
	public int cadency=20;
	
	
	
	public EntityCapacityProjectile(World worldIn) {
		super(worldIn);
		capacities=new EContainerDoubleCapacity(this);
		this.setDamage(0.0);
		this.setSize(0.3F, 0.3F);
	}
	
	
	
	@Override
	public EContainerDoubleCapacity capacities() {
		return capacities;
	}
	
	@Override
	public Entity asEntity() {
		return this;
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		age=0;
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(world.isRemote) {
			if(capacities.hasCapacity2) {
				if(capacities.hasCapacity) {
					if(age%4==0)Capacity.spawnParticle(capacities.red, capacities.green, capacities.blue, getPositionVector());
					else Capacity.spawnParticle(capacities.red2, capacities.green2, capacities.blue2, getPositionVector());
				}
				else Capacity.spawnParticle(capacities.red2, capacities.green2, capacities.blue2, getPositionVector());
			}
			else if(capacities.hasCapacity)Capacity.spawnParticle(capacities.red, capacities.green, capacities.blue, getPositionVector());
		}
		else {
			age++;
			System.out.println("maxage:"+maxage+" age:"+age);
			if(inGround || (maxage!=-1 && age>maxage)) {
				if(capacities.capacity()!=null) {
					this.motionX=0;
					this.motionY=0;
					this.motionZ=0;
					capacities.capacity().use(getContext(null));
				}
				setDead();
			}
			else if(capacities.capacity2()!=null) {
				if(age%cadency==0)capacities.capacity2().use(getContext(null));
			}
		}
	}
	
	protected void onHit(RayTraceResult raytraceResultIn)
    {
        Entity entity = raytraceResultIn.entityHit;
        
        if (entity != null) {
        	if(entity!=shootingEntity) {
        		CapacityContext context=getContext(entity);
                DamageSource damagesource=DamageSource.causeIndirectMagicDamage(this, context.getSender());
                entity.attackEntityFrom(damagesource, (float)getDamage());
                if (entity instanceof EntityLivingBase)  {
            		if(!world.isRemote) {
            			this.motionX=0;
            			this.motionY=0;
            			this.motionZ=0;
            			capacities.capacity().use(context);
            			setDead();
            		}
                }
        	}
        }
        else super.onHit(raytraceResultIn);
    }
	
	private CapacityContext getContext(Entity target) {
		Vec2f pitchyaw=new Vec2f(this.prevRotationPitch, 0-this.prevRotationYaw);
		CapacityContext ret=capacities.getContext(target);
		ret.setRotation(pitchyaw);
		return ret;
	}
	
	
	
	@Override
	protected ItemStack getArrowStack() {
		return Items.AIR.getDefaultInstance();
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		capacities.writeSpawnData(buffer);
	}
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		capacities.readSpawnData(additionalData);
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound nbt) {
		super.writeEntityToNBT(nbt);
		nbt.setInteger("age", age);
		nbt.setInteger("cadency", cadency);
		nbt.setInteger("maxage", maxage);
		nbt.setTag("capacity", capacities.serializeNBT());
	}
	
	
	@Override
	public void readEntityFromNBT(NBTTagCompound nbt) {
		super.readEntityFromNBT(nbt);
		age=nbt.getInteger("age");
		cadency=nbt.getInteger("cadency");
		maxage=nbt.getInteger("maxage");
		capacities.deserializeNBT(nbt.getCompoundTag("capacity"));
	}
}
