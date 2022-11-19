package jempasam.capacityapi.block.factoryrecipe;

import net.minecraft.item.ItemStack;

public interface IFactoryRecipe {
	boolean doAccept(ItemStack ingredient);
	ItemStack getRemaining(ItemStack ingredient);
	ItemStack craft(ItemStack ingredient);
}
