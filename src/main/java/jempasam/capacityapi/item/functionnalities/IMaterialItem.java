package jempasam.capacityapi.item.functionnalities;

import java.util.List;

import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMaterialItem extends IItemColor,ICommonItem{
	
	default MagicMaterial getMaterial(ItemStack stack) {
		String name=getString(stack,"material");
		if(name!=null) return CAPIRegistry.MATERIALS.get(name);
		else return null;
	}
	
	default void setMaterial(ItemStack stack,MagicMaterial c) {
		setString(stack, "material", CAPIRegistry.MATERIALS.idOf(c));
	}
	
	@Override
	default int colorMultiplier(ItemStack stack, int tintIndex) {
		MagicMaterial m=getMaterial(stack);
		
		if(m==null)return 0x555555;
		else {
			if(m.getColorCount()==1)return m.getColor(0);
			else return m.getColor((int)((System.currentTimeMillis()+200L*tintIndex)%(199*m.getColorCount())/200));
		}
	}
	
	@SideOnly(Side.CLIENT)
	default void addMaterialInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		MagicMaterial m=getMaterial(stack);
		if(m!=null)addMaterialInformation(m, tooltip);
	}
	
	@SideOnly(Side.CLIENT)
	default void addMaterialInformation(MagicMaterial material, List<String> tooltip) {
		if(GuiScreen.isShiftKeyDown() || Minecraft.getMinecraft().player.isCreative()) {
			tooltip.add("MATERIAL: "+material.getName());
			tooltip.add("  ("+CAPIRegistry.MATERIALS.idOf(material)+")");
			tooltip.add("  Power "+Integer.toString(material.getPower()));
			tooltip.add("  Speed "+Integer.toString(material.getSpeed()));
			tooltip.add("  Charge "+Integer.toString(material.getCharge()));
			tooltip.add("  Infusion "+Integer.toString(material.getInfusion()));
		}
		else {
			tooltip.add("MATERIAL: "+material.getName());
			tooltip.add("  Power "+Integer.toString(material.getPower()));
			tooltip.add("  Speed "+Integer.toString(material.getSpeed()));
			tooltip.add("  Charge "+Integer.toString(material.getCharge()));
			tooltip.add("  Infusion "+Integer.toString(material.getInfusion()));
		}
	}
	
	public static MagicMaterial getMaterialOf(ItemStack stack) {
		if(stack.getItem() instanceof IMaterialItem) {
			return ((IMaterialItem)stack.getItem()).getMaterial(stack);
		}
		else return null;
	}
}
