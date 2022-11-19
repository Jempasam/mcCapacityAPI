package jempasam.capacityapi.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ResourceItem extends Item {
	private boolean glowing;
	public ResourceItem(int stacksize, boolean glowing) {
		this.setMaxStackSize(stacksize);
		this.glowing=glowing;
	}
	@Override
	public boolean hasEffect(ItemStack stack) {
		return glowing;
	}
}
