package jempasam.capacityapi.item;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MagicScrollItem extends CapacityOnUseItem {
	
	
	
	public MagicScrollItem() {
		super();
		this.setMaxStackSize(64);
	}
	
	
	
	@Override
	protected boolean useAt(World world, ItemStack stack, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation) {
		return false;
	}
	
	@Override
	protected boolean useOnEntity(EntityLivingBase user, Entity target, ItemStack stack, MagicMaterial material, Capacity capacity) {
		if(!user.world.isRemote && capacity.use(CapacityContext.atTarget(user, target, material.getPower()))) {
			destroy(stack,user);
			return true;
		} else return true;
	}
	
	@Override
	protected boolean useOnPlayer(EntityLivingBase user, ItemStack stack, MagicMaterial material, Capacity capacity) {
		if(!user.world.isRemote && capacity.use(CapacityContext.atSender(user, material.getPower()))) {
			destroy(stack,user);
			return true;
		} else return true;
	}
}
