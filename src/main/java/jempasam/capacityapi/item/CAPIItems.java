package jempasam.capacityapi.item;

import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.capacityapi.register.CAPIRegistry.ItemEntry;
import net.minecraft.item.Item;

public class CAPIItems {
	
	public static Item MAGIC_WAND;
	public static Item MAGIC_SCROLL;
	public static Item MAGIC_POTION;
	public static Item MAGIC_SOUP;
	public static Item MAGIC_PILL;
	public static Item MAGIC_VACCIN;
	public static Item MAGIC_BALL;
	
    public static Item MAGIC_CIRCLE;
    public static Item MAGIC_SPHERE;
    public static Item MAGIC_BONE;
    
    public static Item MANA_POWDER;
    
    public static Item MAGIC_POWDER;
    
    public static Item MATERIAL_INGOT;
    
    public static Item CAPACITY_BERRY;
    public static Item CAPACITY_FRUIT;
    
	public static void init() {
		MAGIC_WAND=CAPIRegistry.ITEMS.register("magic_wand", ItemEntry.create(new MagicWandItem())).item;
		MAGIC_SCROLL= CAPIRegistry.ITEMS.register("magic_scroll", ItemEntry.create(new MagicScrollItem())).item;
		MAGIC_POTION= CAPIRegistry.ITEMS.register("magic_potion", ItemEntry.create(new MagicPotionItem(1,1f))).item;
		MAGIC_SOUP=CAPIRegistry.ITEMS.register("magic_soup", ItemEntry.create(new MagicPotionItem(0.1f,4f))).item;
		MAGIC_PILL=CAPIRegistry.ITEMS.register("magic_pill", ItemEntry.create(new MagicPotionItem(10f,0.8f))).item;
        MAGIC_VACCIN=CAPIRegistry.ITEMS.register("magic_vaccin", ItemEntry.create(new MagicPotionItem(40f,0.25f))).item;
        MAGIC_BALL= CAPIRegistry.ITEMS.register("magic_ball", ItemEntry.create(new MagicBallItem())).item;
        MAGIC_CIRCLE= CAPIRegistry.ITEMS.register("magic_circle", ItemEntry.create(new MagicTrapItem())).item;
        MAGIC_SPHERE= CAPIRegistry.ITEMS.register("magic_sphere", ItemEntry.create(new MagicTurretItem())).item;
        MAGIC_BONE= CAPIRegistry.ITEMS.register("magic_bone", ItemEntry.create(new MagicElementalItem())).item;
        MANA_POWDER= CAPIRegistry.ITEMS.register("mana_powder", ItemEntry.create(new ResourceItem(64,true)).oredict("powderMana")).item;
        MAGIC_POWDER= CAPIRegistry.ITEMS.register("magic_powder", ItemEntry.create(new CapacityItem()).oredict("powderMagic")).item;
        MATERIAL_INGOT=CAPIRegistry.ITEMS.register("material_ingot", ItemEntry.create(new MaterialItem()).oredict("ingot")).item;
        CAPACITY_BERRY=CAPIRegistry.ITEMS.register("magic_berry", ItemEntry.create(new GiveCapacityItem().setMaxStackSize(1)).oredict("berry")).item;
        CAPACITY_FRUIT=CAPIRegistry.ITEMS.register("magic_fruit", ItemEntry.create(new GiveCategoryItem().setMaxStackSize(1)).oredict("fruit")).item;
	}
}
