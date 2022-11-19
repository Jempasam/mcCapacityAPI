package jempasam.capacityapi.item;

import java.util.List;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.effect.CapacityEffect;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MagicPotionItem extends CapacityMaterialItem {
	
	private float duration;
	private float amplifier;
	
	public MagicPotionItem(float duration, float amplifier) {
		this.setMaxStackSize(8);
		this.duration=duration;
		this.amplifier=amplifier;
	}
	
	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		Capacity c=getCapacity(player.getHeldItem(EnumHand.MAIN_HAND));
		MagicMaterial m=getMaterial(player.getHeldItem(EnumHand.MAIN_HAND));
		
		if(c!=null && m!=null && entity instanceof EntityLivingBase) {
			CapacityAPI.EFFECT.applyCapacityEffect((EntityLivingBase)entity, c, m, duration, amplifier);
			destroy(stack,player);
			return true;
		}
		return false;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 32;
	}
	
	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.DRINK;
	}
	
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        playerIn.setActiveHand(handIn);
        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if(!worldIn.isRemote) {
			Capacity c=getCapacity(stack);
			MagicMaterial m=getMaterial(stack);
			if(c!=null && m!=null)CapacityAPI.EFFECT.applyCapacityEffect(entityLiving, c, m, duration, amplifier);
		}
		destroy(stack, entityLiving);
		return stack;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addCAPIInformation(MagicMaterial material, Capacity capacity, List<String> tooltip) {
		if(material!=null) {
			tooltip.add("Duration "+Integer.toString(CapacityEffect.duration(duration, material)/20)+"s");
			tooltip.add("Cadency "+Float.toString(CapacityEffect.cadency(amplifier, material))+" per second");
		}
		super.addCAPIInformation(material, capacity, tooltip);
	}
}
