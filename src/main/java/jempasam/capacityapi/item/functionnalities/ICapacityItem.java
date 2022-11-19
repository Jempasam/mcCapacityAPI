package jempasam.capacityapi.item.functionnalities;

import java.util.List;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ICapacityItem extends IItemColor,ICommonItem{
	
	default Capacity getCapacity(ItemStack stack) {
		String name=getString(stack,"capacity");
		if(name!=null) return CAPIRegistry.CAPACITIES.get(name);
		else return null;
	}
	
	default void setCapacity(ItemStack stack,Capacity c) {
		setString(stack, "capacity", CAPIRegistry.CAPACITIES.idOf(c));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	default int colorMultiplier(ItemStack stack, int tintIndex) {
		Capacity c=getCapacity(stack);
		int color=c==null ? 0x555555 : c.getColor(CapacityContext.withPower(1));
		
		if(tintIndex==0) return color;
		else if(tintIndex==1) return 0xffffff;
		else return color;
	}
	
	@SideOnly(Side.CLIENT)
	default void addCapacityInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		Capacity c=getCapacity(stack);
		if(c!=null)addCapacityInformation(c, tooltip);
	}
	
	@SideOnly(Side.CLIENT)
	default void addCapacityInformation(Capacity capacity, List<String> tooltip) {
		CapacityContext context=CapacityContext.withPower(1);
		CapacityContext context2=CapacityContext.withPower(5);
		if(GuiScreen.isShiftKeyDown() || Minecraft.getMinecraft().player.isCreative()) {
			tooltip.add("CAPACITY: "+capacity.getName(context));
			tooltip.add("  ("+CAPIRegistry.CAPACITIES.idOf(capacity)+")");
			tooltip.add("  Mana "+capacity.getMana(context)+"(1)-"+capacity.getMana(context2)+"(5)");
		}
		else {
			tooltip.add("CAPACITY: "+capacity.getName(context));
			tooltip.add("  Mana "+capacity.getMana(context)+"(1)-"+capacity.getMana(context2)+"(5)");
		}
	}
	
	public static Capacity getCapacityOf(ItemStack stack) {
		if(stack.getItem() instanceof ICapacityItem) {
			return ((ICapacityItem)stack.getItem()).getCapacity(stack);
		}
		else return null;
	}
}
