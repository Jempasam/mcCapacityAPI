package jempasam.capacityapi.item;

import java.util.List;

import jempasam.capacityapi.item.functionnalities.ICategoryMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class CategoryMaterialItem extends Item implements ICategoryMaterialItem{
	
	
	
	public CategoryMaterialItem() {
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
		String c=getCategory(stack);
		MagicMaterial m=getMaterial(stack);
		if(c==null||m==null)return new TextComponentTranslation(getUnlocalizedName(stack)+".broken.name").getFormattedText();
		else return new TextComponentTranslation(getUnlocalizedName(stack)+".name", c, m.getName()).getFormattedText();
	}
}
