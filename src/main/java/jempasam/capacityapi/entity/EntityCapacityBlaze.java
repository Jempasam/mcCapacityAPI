package jempasam.capacityapi.entity;

import java.util.UUID;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.ai.EntityAIOwnerProtection;
import jempasam.capacityapi.entity.container.EContainerCapacity;
import jempasam.capacityapi.item.CAPIItems;
import jempasam.capacityapi.item.functionnalities.ICapacityItem;
import jempasam.mcsam.VectorUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityCapacityBlaze extends EntityMob implements IEntityAdditionalSpawnData, IEntityOwnable{
	
	
	
	public EContainerCapacity ccontainer;
	
	public int cadency;	
	public int lasthittime;
	private float heightOffset = 0.5F;
    private int heightOffsetUpdateTime;
    private int moveUpdateTime=0;
    private Entity owner;
    public boolean wild;
	
	
	
	public EntityCapacityBlaze(World worldIn) {
		super(worldIn);
		ccontainer=new EContainerCapacity(this);
        this.experienceValue = 0;
        wild=true;
	}
	
	
	
	@Override
	public Entity getOwner() {
		return ccontainer.getSender();
	}
	
	
	@Override
	public UUID getOwnerId() {
		return ccontainer.getSenderUUID();
	}
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(12, new EntityAILookIdle(this));
        this.tasks.addTask(13, new EntityAIWanderAvoidWater(this, 1.0D));
        this.targetTasks.addTask(1, new EntityAIOwnerProtection(this,this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
    }
	
	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(48.0D);
	}
	
	protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_BLAZE_AMBIENT;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return SoundEvents.ENTITY_BLAZE_HURT;
    }

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_BLAZE_DEATH;
    }
    
    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender()  {
        return 15728880;
    }

    public float getBrightness() {
        return 1.0F;
    }
    
    
    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
    	if(wild) {
    		ItemStack stack=new ItemStack(CAPIItems.MAGIC_POWDER,1);
    		if(ccontainer.capacity()!=null)((ICapacityItem)CAPIItems.MAGIC_POWDER).setCapacity(stack, ccontainer.capacity());
    		this.entityDropItem(stack, 0.0f);
    	}
    }
    
    public void onLivingUpdate()
    {
    	double speed=getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
        if (this.world.isRemote) {
            if (this.rand.nextInt(24) == 0 && !this.isSilent()){
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_BLAZE_BURN, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
            }
            if(this.ticksExisted%5==0) {
            	Capacity.sendParticleZone(ccontainer.red, ccontainer.green, ccontainer.blue, getEntityBoundingBox(), ccontainer.getPower()*5);
            }
        }
        else {
        	if(getAttackTarget()!=null){
        		if(ccontainer.capacity()!=null && this.ticksExisted-lasthittime>cadency) {
        			lasthittime=ticksExisted;
                	throwSpellOn(getAttackTarget());
        		}
        		Vec3d offset=getAttackTarget().getPositionVector().subtract(getPositionVector());
        		double distance=offset.x*offset.x+offset.y*offset.y+offset.z*offset.z;
        		Vec3d move=offset.addVector(0.0, heightOffset, 0.0).scale(distance-25).normalize();
        		if(this.motionX*move.x < speed) motionX+=speed/20f*move.x;
        		if(this.motionZ*move.z < speed) motionZ+=speed/20f*move.z;
        		if(this.motionY*move.y < speed) motionY+=speed/20f*move.y;
            }
    		this.motionX*=0.95f;
    		this.motionY=motionY*0.95f-0.05f;
    		this.motionZ*=0.95f;
        }
        fallDistance=0.0f;
        super.onLivingUpdate();
    }
    
    @Override
    public boolean hasNoGravity() {
    	return true;
    }
    
    @Override
    public void onUpdate() {
    	this.onGround=true;
    	super.onUpdate();
    }

    protected void updateAITasks()
    {
    	Entity sender=ccontainer.getSender();
    	if(sender!=null && sender==getAttackTarget())setAttackTarget(null);
        --this.heightOffsetUpdateTime;
        if (this.heightOffsetUpdateTime <= 0) {
            this.heightOffsetUpdateTime = 100;
            this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
        }
        super.updateAITasks();
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
	protected boolean isValidLightLevel(){
        return true;
    }
	
	@Override
	public void readSpawnData(ByteBuf additionalData) {
		ccontainer.readSpawnData(additionalData);
	}
	
	@Override
	public void writeSpawnData(ByteBuf additionalData) {
		ccontainer.writeSpawnData(additionalData);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		ccontainer.deserializeNBT(compound.getCompoundTag("capacity"));
		cadency=compound.getInteger("cadency");
		wild=compound.getBoolean("isWild");
		int id=compound.getInteger("owner");
		if(id!=0)owner=world.getEntityByID(id);
		getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(compound.getDouble("max_health")); heal(getMaxHealth());
		getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(compound.getDouble("move_speed"));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setTag("capacity", ccontainer.serializeNBT());
		compound.setInteger("cadency", cadency);
		if(owner!=null)compound.setInteger("owner", owner.getEntityId());
		compound.setDouble("max_health", getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
		compound.setDouble("move_speed", getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue());
		compound.setBoolean("isWild", wild);
	}
	
}
