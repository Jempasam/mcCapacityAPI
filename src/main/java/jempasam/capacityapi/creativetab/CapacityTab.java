package jempasam.capacityapi.creativetab;

import java.util.Map;

import akka.japi.Predicate;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.item.functionnalities.ICapacityItem;
import jempasam.capacityapi.item.functionnalities.ICapacityMaterialItem;
import jempasam.capacityapi.item.functionnalities.ICategoryItem;
import jempasam.capacityapi.item.functionnalities.ICategoryMaterialItem;
import jempasam.capacityapi.item.functionnalities.IMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.capacityapi.register.CAPIRegistry.ItemEntry;
import jempasam.samstream.stream.SamStream;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CapacityTab extends CreativeTabs{
	
	private ItemStack icon;
	private Predicate<Item> predicate;
	
	public CapacityTab(String name,ItemStack icon, Predicate<Item> predicate) {
		super(name);
		this.predicate=predicate;
		this.icon=icon;
		this.setBackgroundImageName("item_search.png");
	}
	
	@Override
	public boolean hasSearchBar() {
		return true;
	}

	@Override
	public ItemStack getTabIconItem() {
		return icon;
	}
	
	@Override
	public void displayAllRelevantItems(NonNullList<ItemStack> tabinventory) {
		SamStream<MagicMaterial> materials=CAPIRegistry.MATERIALS.ofCategories("creative").fixed();
		for(Map.Entry<String,ItemEntry> entry : CAPIRegistry.ITEMS) {
			Item item=entry.getValue().item;
			if(predicate.test(item)) {
				if(item instanceof ICapacityMaterialItem) {
					for(MagicMaterial material : materials){
						CAPIRegistry.CAPACITIES.stream().filter(e->!e.getKey().startsWith("_")).forEach((centry)->{
							ItemStack stack=new ItemStack(item);
							((ICapacityMaterialItem)item).setCapacity(stack, centry.getValue());
							((ICapacityMaterialItem)item).setMaterial(stack, material);
							tabinventory.add(stack);
						});
					}
				}
				else if(item instanceof ICategoryMaterialItem) {
					for(MagicMaterial material : materials){
						CAPIRegistry.CAPACITIES.categoriesNames().filter(e->!e.startsWith("_")).forEach((categ)->{
							ItemStack stack=new ItemStack(item);
							((ICategoryMaterialItem)item).setCategory(stack, categ);
							((ICategoryMaterialItem)item).setMaterial(stack, material);
							tabinventory.add(stack);
						});
					}
				}
				else if(item instanceof ICategoryItem) {
					CAPIRegistry.CAPACITIES.categoriesNames().filter(e->!e.startsWith("_")).forEach((categ)->{
						ItemStack stack=new ItemStack(item);
						((ICategoryItem)item).setCategory(stack, categ);
						tabinventory.add(stack);
					});
				}
				else if(item instanceof ICapacityItem) {
					CAPIRegistry.CAPACITIES.stream().filter(e->!e.getKey().startsWith("_")).forEach((centry)->{
						ItemStack stack=new ItemStack(item);
						((ICapacityItem)item).setCapacity(stack, centry.getValue());
						tabinventory.add(stack);
					});
				}
				else if(item instanceof IMaterialItem) {
					for(MagicMaterial material : materials){
						ItemStack stack=new ItemStack(item);
						((IMaterialItem)item).setMaterial(stack, material);
						tabinventory.add(stack);
					}
				}
				else {
					tabinventory.add(new ItemStack(item));
				}
			}
		}
	}
	
}
