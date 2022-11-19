package jempasam.capacityapi.item.functionnalities;

import java.util.List;

import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICategoryItem extends ICommonItem, IItemColor{
	
	default String getCategory(ItemStack stack) {
		return getString(stack, "category");
	}
	
	default void setCategory(ItemStack stack, String category) {
		setString(stack, "category", category);
	}
	
	@SideOnly(Side.CLIENT)
	default int colorMultiplier(ItemStack stack, int tintIndex) {
		return CAPIRegistry.COLORS.getOrDefault(getCategory(stack), 0x333333);
	}
	
	@SideOnly(Side.CLIENT)
	default void addCategoryInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		String category=getCategory(stack);
		if(category!=null)addCategoryInformation(category, tooltip);
	}
	
	@SideOnly(Side.CLIENT)
	default void addCategoryInformation(String category, List<String> tooltip) {
		tooltip.add("CATEGORY: "+category);
	}
}
