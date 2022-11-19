package jempasam.capacityapi.item.functionnalities;

import java.util.List;

import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICategoryMaterialItem extends ICategoryItem,IMaterialItem{
	
	@SideOnly(Side.CLIENT)
	default void addCAPIInformation(MagicMaterial material, String category, List<String> tooltip) {
		if(category!=null)addCategoryInformation(category, tooltip);
		if(material!=null)addMaterialInformation(material, tooltip);
	}
	
	@Override
	default int colorMultiplier(ItemStack stack, int tintIndex) {
		String c=getCategory(stack);
		MagicMaterial m=getMaterial(stack);
		
		if(tintIndex==0 ) {
			if(m==null)return 0x666666;
			else if(m.getColorCount()==1)return m.getColor(0);
			else return m.getColor(((int)System.currentTimeMillis())%(199*m.getColorCount())/200);
		}
		else return CAPIRegistry.COLORS.getOrDefault(c, 0x666666);
	}
	
	@SideOnly(Side.CLIENT)
	default void addCAPIInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MagicMaterial m=getMaterial(stack);
		String c=getCategory(stack);
		addCAPIInformation(m, c, tooltip);
	}
	
}
