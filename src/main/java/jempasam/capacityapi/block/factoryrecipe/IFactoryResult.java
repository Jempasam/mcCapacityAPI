package jempasam.capacityapi.block.factoryrecipe;

import net.minecraft.item.ItemStack;

public interface IFactoryResult {
	ItemStack craft(ItemStack stack);
}
