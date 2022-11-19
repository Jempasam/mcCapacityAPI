package jempasam.capacityapi.block.factoryrecipe;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.item.ItemStack;

@Loadable
public class CompositeFactoryRecipe implements IFactoryRecipe{
	
	private IFactoryIngredient ingredient;
	private IFactoryResult result;
	
	@LoadableParameter(paramnames = {"ingredient","result"})
	public CompositeFactoryRecipe(IFactoryIngredient ingredient, IFactoryResult result) {
		super();
		this.ingredient = ingredient;
		this.result = result;
	}
	
	@Override
	public boolean doAccept(ItemStack stack) {
		return ingredient.test(stack);
	}
	
	@Override
	public ItemStack getRemaining(ItemStack stack) {
		return ingredient.getRemaining(stack);
	}
	
	@Override
	public ItemStack craft(ItemStack stack) {
		return result.craft(stack);
	}
	
}
