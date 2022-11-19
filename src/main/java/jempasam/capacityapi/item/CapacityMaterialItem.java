package jempasam.capacityapi.item;

import java.util.List;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.item.functionnalities.ICapacityMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CapacityMaterialItem extends Item implements ICapacityMaterialItem{
	
	
	
	public CapacityMaterialItem() {
		super();
	}
	
	
	
	@Override
	public boolean hasEffect(ItemStack stack) {
		MagicMaterial m=getMaterial(stack);
		return m!=null && m.glowing;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addCAPIInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		Capacity c=getCapacity(stack);
		MagicMaterial m=getMaterial(stack);
		if(c==null||m==null)return new TextComponentTranslation(getUnlocalizedName(stack)+".broken.name").getFormattedText();
		else return new TextComponentTranslation(getUnlocalizedName(stack)+".name", c.getName(CapacityContext.withPower(1)), m.getName()).getFormattedText();
	}
}
