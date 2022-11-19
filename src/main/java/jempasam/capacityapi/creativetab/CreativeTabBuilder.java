package jempasam.capacityapi.creativetab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CreativeTabBuilder{
	private CTabs tab;
	
	public CreativeTabBuilder(String name,Item icon) {
		tab=new CTabs(name,icon);
	}
	
	public CreativeTabBuilder color(int c) {
		tab.color=c;
		return this;
	}
	
	public CreativeTabBuilder searchbar() {
		tab.searchbar=true;
		tab.setBackgroundImageName("item_search.png");
		return this;
	}
	
	public CreativeTabs get() {
		return tab;
	}
	private static int place=21;
	private class CTabs extends CreativeTabs{
		public ItemStack icon=null;
		public int color=0x404040;
		public boolean searchbar=false;
		
		public CTabs(String name,Item icon) {
			super(place,name);
			place++;
			this.icon=new ItemStack(icon);
		}
		@Override public ItemStack getTabIconItem() { return icon; }
		@Override public int getLabelColor() { return color; }
		@Override public boolean hasSearchBar() { return searchbar; }
	}

}
