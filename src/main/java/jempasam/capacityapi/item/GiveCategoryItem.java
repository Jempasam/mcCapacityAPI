package jempasam.capacityapi.item;

import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class GiveCategoryItem extends CategoryMaterialItem{
	
	
	
	public GiveCategoryItem() {
		super();
	}
	
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		ItemStack stack=player.getHeldItem(hand);
		if(!worldIn.isRemote) {
			String category=getCategory(stack);
			MagicMaterial mat=getMaterial(stack);
			if(category!=null && mat!=null && player.hasCapability(CAPICapabilities.CAPACITY_OWNER, null)) {
				ICapacityOwner owner=player.getCapability(CAPICapabilities.CAPACITY_OWNER, null);
				owner.giveCategory(category);
				owner.generate(-1, mat.getInfusion()/400);
			}
			stack.shrink(1);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
	
}
