package jempasam.capacityapi.item;

import java.util.List;

import jempasam.capacityapi.item.functionnalities.IMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class MaterialItem extends Item implements IMaterialItem{
	
	
	
	public MaterialItem() {
		super();
	}
	
	
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		addMaterialInformation(stack, worldIn, tooltip, flagIn);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		MagicMaterial m=getMaterial(stack);
		if(m==null)return new TextComponentTranslation(getUnlocalizedName(stack)+".broken.name").getFormattedText();
		else return new TextComponentTranslation(getUnlocalizedName(stack)+".name", m.getName()).getFormattedText();
	}
	
}
