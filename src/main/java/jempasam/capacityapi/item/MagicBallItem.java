package jempasam.capacityapi.item;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.entity.EntityCapacityProjectile;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class MagicBallItem extends CapacityMaterialItem {
	
	public MagicBallItem() {
		this.setMaxStackSize(16);
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, new BehaviorProjectileDispense() {
			protected IProjectile getProjectileEntity(World worldIn, IPosition position, ItemStack stackIn) {
				Capacity c=getCapacity(stackIn);
				MagicMaterial m=getMaterial(stackIn);
				if(c!=null && m!=null) {
					EntityCapacityProjectile ret= new EntityCapacityProjectile(worldIn);
					ret.capacities().fill(c, ret, ret, m.getPower());
					ret.setPosition(position.getX(), position.getY(), position.getZ());
					return ret;
				}
				else return null;
			}
		});
	}
	
	@Override
	public void setMaterial(ItemStack stack, MagicMaterial c) {
		super.setMaterial(stack, c);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer player, EnumHand handIn) {
		ItemStack handitem=player.getHeldItem(handIn);
		
		if(!worldIn.isRemote) {
			Capacity c=getCapacity(handitem);
			MagicMaterial m=getMaterial(handitem);
			EntityCapacityProjectile projectile=new EntityCapacityProjectile(worldIn);
			projectile.capacities().fill(c, projectile, projectile, m.getPower());
			projectile.shoot(player, player.rotationPitch, player.rotationYaw, 0.0f, m.getSpeed()/150f, 0.0f);
			projectile.setPosition(player.posX, player.posY+player.eyeHeight, player.posZ);
			projectile.shootingEntity=player;
			worldIn.spawnEntity(projectile);
		}
		
		handitem.shrink(1);
		return new ActionResult<>(EnumActionResult.SUCCESS,handitem);
	}
}
