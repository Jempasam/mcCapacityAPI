package jempasam.capacityapi.item;

import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capacity.Capacity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class GiveCapacityItem extends CapacityItem {
	
	
	
	public GiveCapacityItem() {
		super();
	}
	
	
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand hand) {
		ItemStack stack=player.getHeldItem(hand);
		if(!worldIn.isRemote) {
			Capacity capacity=getCapacity(stack);
			if(capacity!=null && player.hasCapability(CAPICapabilities.CAPACITY_OWNER, null)) {
				player.getCapability(CAPICapabilities.CAPACITY_OWNER, null).giveCapacity(capacity);
			}
			stack.shrink(1);
		}
		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}
}
