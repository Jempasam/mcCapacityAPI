package jempasam.capacityapi.item.functionnalities;

import java.util.List;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICapacityMaterialItem extends ICapacityItem,IMaterialItem{
	
	@SideOnly(Side.CLIENT)
	default void addCAPIInformation(MagicMaterial material, Capacity capacity, List<String> tooltip) {
		if(capacity!=null)addCapacityInformation(capacity, tooltip);
		if(material!=null)addMaterialInformation(material, tooltip);
	}
	
	@Override
	default int colorMultiplier(ItemStack stack, int tintIndex) {
		Capacity c=getCapacity(stack);
		MagicMaterial m=getMaterial(stack);
		
		if(tintIndex==0 ) {
			if(m==null)return 0x666666;
			else if(m.getColorCount()==1)return m.getColor(0);
			else return m.getColor((int)(System.currentTimeMillis()%(199*m.getColorCount())/200));
		}
		else {
			if(c==null)return 0x666666;
			else return c.getColor(CapacityContext.withPower(1));
		}
	}
	
	@SideOnly(Side.CLIENT)
	default void addCAPIInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MagicMaterial m=getMaterial(stack);
		Capacity c=getCapacity(stack);
		addCAPIInformation(m, c, tooltip);
	}
	
}
