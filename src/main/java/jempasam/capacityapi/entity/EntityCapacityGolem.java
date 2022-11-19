package jempasam.capacityapi.entity;

import java.lang.reflect.Field;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.container.EContainerQuardrubleCapacity;
import jempasam.mcsam.VectorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCapacityGolem extends EntityIronGolem implements IEntityAdditionalSpawnData{
	
	
	
	public EContainerQuardrubleCapacity ccontainer;
	
	@SideOnly(Side.CLIENT) public float red;
	@SideOnly(Side.CLIENT) public float green;
	@SideOnly(Side.CLIENT) public float blue;
	@SideOnly(Side.CLIENT) public boolean has1;
	@SideOnly(Side.CLIENT) public boolean has2;
	@SideOnly(Side.CLIENT) public boolean has3;
	@SideOnly(Side.CLIENT) public boolean has4;
	
	public int cadency;	
	public int lasthittime;
	public boolean wild;
	
	
	
	public EntityCapacityGolem(World worldIn) {
		super(worldIn);
		ccontainer=new EContainerQuardrubleCapacity(this);
	}
	
	
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).setBaseValue(0.0D);
	}
	
	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if(!world.isRemote) {
			lasthittime=cadency;
			if(ccontainer.capacity()!=null) {
				throwSpellOn(entityIn);
			}
		}
		return super.attackEntityAsMob(entityIn);
	}
	
	private void throwSpellOn(Entity target) {
		CapacityContext context=ccontainer.getContext(this);
		context.setPos(context.getPos().addVector(0.0, this.getEyeHeight(), 0.0));
		Vec2f rotation=VectorUtils.getPitchYaw(target.getPositionVector().subtract(context.getPos()));
		rotation=new Vec2f(rotation.x-10, rotation.y);
		context.setRotation(rotation);
		ccontainer.capacity().use(context);
	}
	
	@Override
	public void onEntityUpdate() {
		super.onEntityUpdate();
		if(!world.isRemote) {
			if(ccontainer.capacity4()!=null && ticksExisted%cadency==0) {
				ccontainer.use4(null);
			}
			if(getAttackTarget()!=null) {
				if(ccontainer.capacity2()!=null && ticksExisted-lasthittime>60) {
					lasthittime=ticksExisted+cadency;
					ccontainer.use2(null);
				}
				if(ccontainer.capacity()!=null && (ticksExisted+cadency/2)%cadency==0) {
					throwSpellOn(getAttackTarget());
					try {
						Field field=this.getClass().getSuperclass().getField("attackTimer");
						field.setAccessible(true);
						field.set(this, 10);
						field.setAccessible(false);
						this.world.setEntityState(this, (byte)4);
					} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) { }
				}
			}
		}
	}
	
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount){
		if(super.attackEntityFrom(source, amount)) {
			if( !world.isRemote && ccontainer.capacity3()!=null && (source instanceof EntityDamageSourceIndirect || ticksExisted%4==0)) ccontainer.use3(null);
			return true;
		}
		else return false;
    }
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		ccontainer.readSpawnData(additionalData);
		red=additionalData.readFloat();
		green=additionalData.readFloat();
		blue=additionalData.readFloat();
		has1=additionalData.readBoolean();
		has2=additionalData.readBoolean();
		has3=additionalData.readBoolean();
		has4=additionalData.readBoolean();
	}
	
	@Override
	public void writeSpawnData(ByteBuf additionalData) {
		ccontainer.writeSpawnData(additionalData);
		additionalData.writeFloat(red);
		additionalData.writeFloat(green);
		additionalData.writeFloat(blue);
		additionalData.writeBoolean(ccontainer.capacity()!=null);
		additionalData.writeBoolean(ccontainer.capacity2()!=null);
		additionalData.writeBoolean(ccontainer.capacity3()!=null);
		additionalData.writeBoolean(ccontainer.capacity4()!=null);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		ccontainer.deserializeNBT(compound.getCompoundTag("capacity"));
		red=compound.getFloat("red");
		green=compound.getFloat("green");
		blue=compound.getFloat("blue");
		cadency=compound.getInteger("cadency");
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(compound.getDouble("max_health"));
		heal(getMaxHealth());
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(compound.getDouble("move_speed"));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("capacity", ccontainer.serializeNBT());
		compound.setFloat("red", red);
		compound.setFloat("green", green);
		compound.setFloat("blue", blue);
		compound.setInteger("cadency", cadency);
		compound.setDouble("max_health", getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
		compound.setDouble("move_speed", getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
	}
	
}
