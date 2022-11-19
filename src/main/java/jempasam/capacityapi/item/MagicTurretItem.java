package jempasam.capacityapi.item;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.EntityCapacityTurret;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class MagicTurretItem extends CapacityOnUseItem {
	
	
	
	public MagicTurretItem() {
		this.setMaxStackSize(16);
	}
	
	
	
	@Override
	protected boolean useAt(World world, ItemStack stack, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation) {
		if(!world.isRemote) {
			EntityCapacityTurret projectile=new EntityCapacityTurret(world);
			projectile.ccontainer.fill(capacity, projectile, projectile, material.getPower());
			projectile.setPosition(position.x,position.y,position.z);
			projectile.reloadTime=48000/material.getSpeed();
			projectile.repetition=material.getCharge()/300;
			projectile.yawRotation=5;
			world.spawnEntity(projectile);
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
					EntityCapacityTurret projectile=new EntityCapacityTurret(user.world);
					projectile.ccontainer.fill(capacity, user, user, material.getPower());
					projectile.setPosition(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
					projectile.reloadTime=48000/material.getSpeed();
					projectile.repetition=material.getCharge()/300;
					projectile.yawRotation=5;
					user.world.spawnEntity(projectile);
					stack.shrink(1);
				}
				else  capacity.spawnParticleBomb(CapacityContext.withPower(1), raytraceresult.hitVec, 10);
				return true;
		}
		else {
			if(!user.world.isRemote) {
				EntityCapacityTurret projectile=new EntityCapacityTurret(user.world);
				projectile.ccontainer.fill(capacity, user, user, material.getPower());
				projectile.setPosition(user.posX,user.posY+user.getEyeHeight(),user.posZ);
				projectile.reloadTime=48000/material.getSpeed();
				projectile.repetition=material.getCharge()/300;
				projectile.yawRotation=0;
				projectile.rotationPitch=user.rotationPitch;
				projectile.rotationYaw=user.rotationYaw;
				user.world.spawnEntity(projectile);
				stack.shrink(1);
			}
			else capacity.spawnParticleBomb(CapacityContext.withPower(1), user.getPositionVector(), 10);
		}
		return false;
	}
	
}
