package jempasam.capacityapi.item;

import java.util.List;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.mcsam.VectorUtils;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class CapacityOnUseItem extends CapacityMaterialItem{
	
	
	
	public CapacityOnUseItem() {
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new IBehaviorDispenseItem() {
			CapacityOnUseItem item=CapacityOnUseItem.this;
			public ItemStack dispense(IBlockSource source, ItemStack stack) {
				Capacity c=getCapacity(stack);
				MagicMaterial m=getMaterial(stack);
				if(c!=null && m!=null) {
					EnumFacing enumfacing = (EnumFacing)source.getBlockState().getValue(BlockDispenser.FACING);
					BlockPos targetblock=source.getBlockPos().add(enumfacing.getDirectionVec());
					List<Entity> result=source.getWorld().getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(targetblock), EntityLivingBase.class::isInstance);
					if(result.size()>0) item.useOnEntity(null, result.get(0), stack, m, c);
					else item.useAt(source.getWorld(), stack, m, c, new Vec3d(targetblock), VectorUtils.getPitchYaw(new Vec3d(enumfacing.getDirectionVec())));
				}
				return stack;
			}
		});
	}
	
	
	protected abstract boolean useAt(World world, ItemStack stack, MagicMaterial material, Capacity capacity, Vec3d position, Vec2f rotation);
	
	protected boolean useOnPlayer(EntityLivingBase user, ItemStack stack, MagicMaterial material, Capacity capacity) {
		return useAt(user.getEntityWorld(), stack, material, capacity, user.getPositionVector().addVector(0.0, user.getEyeHeight(), 0.0), user.getPitchYaw());
	}
	
	protected boolean useOnEntity(EntityLivingBase user, Entity target, ItemStack stack, MagicMaterial material, Capacity capacity) {
		return useOnPlayer(user, stack, material, capacity);
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		Capacity c=getCapacity(player.getHeldItem(EnumHand.MAIN_HAND));
		MagicMaterial m=getMaterial(player.getHeldItem(EnumHand.MAIN_HAND));
		if(c!=null && m!=null) return this.useOnEntity(player, entity, stack, m, c);
		else return false;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack handitem=playerIn.getHeldItem(handIn);
		Capacity c=getCapacity(handitem);
		MagicMaterial m=getMaterial(handitem);
		
		if(c!=null && m!=null && this.useOnPlayer(playerIn, handitem, m, c))return new ActionResult<>(EnumActionResult.SUCCESS,handitem);
		else return new ActionResult<>(EnumActionResult.FAIL,handitem);
	}
}
