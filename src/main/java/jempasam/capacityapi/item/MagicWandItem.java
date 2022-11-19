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

public class MagicWandItem extends CapacityOnUseItem {
	
	public MagicWandItem() {
		this.setMaxStackSize(1);
	}
	
	@Override
	public int getMaxDamage(ItemStack stack) {
		MagicMaterial m=getMaterial(stack);
		return m==null ? 0 : m.getCharge();
	}
	
	@Override
	protected boolean useAt(World world, ItemStack stack, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation) {
		return false;
	}
	
	@Override
	protected boolean useOnEntity(EntityLivingBase user, Entity target, ItemStack stack, MagicMaterial material, Capacity capacity) {
		CapacityContext context=CapacityContext.atTarget(user, target, material.getPower());
		if(!user.world.isRemote && capacity.use(context)) {
			stack.damageItem(capacity.getMana(context), user);
			return true;
		} else return true;
	}
	
	@Override
	protected boolean useOnPlayer(EntityLivingBase user, ItemStack stack, MagicMaterial material, Capacity capacity) {
		CapacityContext context=CapacityContext.atTarget(user, user, material.getPower());
		if(!user.world.isRemote && capacity.use(context)) {
			stack.damageItem(capacity.getMana(context), user);
			return true;
		} else return true;
	}
}
