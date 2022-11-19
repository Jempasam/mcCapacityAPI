package jempasam.capacityapi.block.factoryrecipe;

import akka.japi.Predicate;
import net.minecraft.item.ItemStack;

public interface IFactoryIngredient extends Predicate<ItemStack>{
	ItemStack getRemaining(ItemStack ingredient);
}
