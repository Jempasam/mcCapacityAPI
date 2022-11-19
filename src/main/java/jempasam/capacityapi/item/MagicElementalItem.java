package jempasam.capacityapi.item;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.EntityCapacityBlaze;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.World;

public class MagicElementalItem extends CapacityOnUseItem {
	
	
	
	public MagicElementalItem() {
		super();
		setMaxStackSize(16);
	}
	
	
	
	@Override
	protected boolean useAt(World world, ItemStack stack, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation) {
		if(!world.isRemote) {
			EntityCapacityBlaze entity=getEntity(null, world, material, capacity, position, rotation);
			world.spawnEntity(entity);
			stack.shrink(1);
		}
		else  capacity.spawnParticleBomb(CapacityContext.withPower(1), position, 10);
		return true;
	}
	
	@Override
	protected boolean useOnPlayer(EntityLivingBase user, ItemStack stack, MagicMaterial material, Capacity capacity) {
		double reach;
		if(user instanceof EntityPlayer)reach=user.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
		else reach=2.0;
		RayTraceResult raytraceresult = user.rayTrace(reach, 0);
		if(raytraceresult!=null && raytraceresult.typeOfHit==Type.BLOCK) {
				if(!user.world.isRemote) {
					EntityCapacityBlaze entity=getEntity(user, user.world, material, capacity, raytraceresult.hitVec, user.getPitchYaw());
					user.world.spawnEntity(entity);
					stack.shrink(1);
				}
				else  capacity.spawnParticleBomb(CapacityContext.withPower(1), raytraceresult.hitVec, 10);
				return true;
		}
		else {
			if(!user.world.isRemote) {
				EntityCapacityBlaze entity=getEntity(user, user.world, material, capacity, user.getPositionVector(), user.getPitchYaw());
				user.world.spawnEntity(entity);
				stack.shrink(1);
			}
			else capacity.spawnParticleBomb(CapacityContext.withPower(1), user.getPositionVector(), 10);
			return true;
		}
	}
	
	private EntityCapacityBlaze getEntity(Entity user, World world, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation) {
		EntityCapacityBlaze ret=new EntityCapacityBlaze(world);
		if(user==null)user=ret;
		ret.setPosition(position.x, position.y, position.z);
		ret.rotationPitch=rotation.x;
		ret.rotationYaw=rotation.y;
		ret.ccontainer.fill(capacity, user, user, material.getPower());
		ret.cadency=650-material.getInfusion()/2;
		ret.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(material.getCharge()/100f);
		ret.setHealth(ret.getMaxHealth());
		ret.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(material.getSpeed()/200f);
		ret.wild=false;
		return ret;
	}
}
