package jempasam.capacityapi.block.factoryrecipe;

import jempasam.capacityapi.item.functionnalities.ICapacityItem;
import jempasam.capacityapi.item.functionnalities.IMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@Loadable
public class ItemCondition implements IFactoryIngredient{
	
	private Item item;
	@LoadableParameter private Item replacer=null;
	@LoadableParameter private CapacityPredicate capacity=c->true;
	@LoadableParameter private MagicMaterial material=null;
	@LoadableParameter private int count=1;
	
	@LoadableParameter(paramnames = {"item"})
	public ItemCondition(Item item) {
		super();
		this.item = item;
	}
	
	@Override
	public boolean test(ItemStack arg0) {
		System.out.println(arg0.getItem()+"="+item+" && "+arg0.getCount()+" >= "+count);
		return arg0.getItem()==item && arg0.getCount()>=count
				&& (material==null || material==IMaterialItem.getMaterialOf(arg0))
				&& capacity.test(ICapacityItem.getCapacityOf(arg0));
	}
	
	@Override
	public ItemStack getRemaining(ItemStack ingredient) {
		if(replacer!=null)return replacer.getDefaultInstance();
		ingredient.shrink(count);
		return ingredient;
	}
	
	
	
	
}
