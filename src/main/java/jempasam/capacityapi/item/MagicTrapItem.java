package jempasam.capacityapi.item;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.EntityCapacityTrap;
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



public class MagicTrapItem extends CapacityOnUseItem {
	
	public MagicTrapItem() {
		super();
		this.setMaxStackSize(16);
	}
	
	@Override
	protected boolean useAt(World world, ItemStack stack, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation) {
		if(!world.isRemote) {
			EntityCapacityTrap projectile=new EntityCapacityTrap(world);
			projectile.capacities().fill(capacity, projectile, projectile, material.getPower());
			projectile.setPosition(position.x, position.y, position.z);
			projectile.range=material.getInfusion()/500f;
			projectile.timeout=(int)(material.getInfusion()/25f);
			projectile.life=material.getCharge()/1000;
			projectile.rotationPitch=-90;
			world.spawnEntity(projectile);
			stack.shrink(1);
		}
		else capacity.spawnParticleBomb(CapacityContext.withPower(1), position, 10);
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
					EntityCapacityTrap projectile=new EntityCapacityTrap(user.world);
					projectile.capacities().fill(capacity, user, user, material.getPower());
					projectile.setPosition(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
					projectile.range=material.getInfusion()/500f;
					projectile.timeout=(int)(material.getInfusion()/25f);
					projectile.life=material.getCharge()/1000;
					projectile.rotationPitch=-90;
					user.world.spawnEntity(projectile);
					stack.shrink(1);
				}
				else {
					capacity.spawnParticleBomb(CapacityContext.withPower(1), raytraceresult.hitVec, 10);
				}
				return true;
		}
		return false;
	}
}
