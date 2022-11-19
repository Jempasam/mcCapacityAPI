package jempasam.capacityapi.block.factoryrecipe;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.item.functionnalities.ICapacityItem;
import jempasam.capacityapi.item.functionnalities.IMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Loadable
public class ItemResult implements IFactoryResult{
	
	private Item item;
	@LoadableParameter private int count=1;
	@LoadableParameter private MagicMaterial material=null;
	@LoadableParameter private Capacity capacity=null;
	
	@LoadableParameter(paramnames =  {"item"})
	public ItemResult(Item item) {
		super();
		this.item = item;
	}
	
	@Override
	public ItemStack craft(ItemStack stack) {
		ItemStack ret=new ItemStack(item, count);
		if(item instanceof IMaterialItem)((IMaterialItem)item).setMaterial(stack, getMaterial(stack));
		if(item instanceof ICapacityItem)((ICapacityItem)item).setCapacity(stack, getCapacity(stack));
		return ret;
	}
	
	private MagicMaterial getMaterial(ItemStack stack) {
		if(material!=null)return material;
		else if(stack.getItem() instanceof IMaterialItem)return ((IMaterialItem)stack.getItem()).getMaterial(stack);
		else return null;
	}
	
	private Capacity getCapacity(ItemStack stack) {
		if(capacity!=null)return capacity;
		else if(stack.getItem() instanceof ICapacityItem)return ((ICapacityItem)stack.getItem()).getCapacity(stack);
		else return null;
	}
	
	
}
