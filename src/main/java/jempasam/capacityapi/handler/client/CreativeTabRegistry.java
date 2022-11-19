package jempasam.capacityapi.handler.client;

import jempasam.capacityapi.creativetab.CapacityTab;
import jempasam.capacityapi.item.CAPIItems;
import jempasam.capacityapi.item.MagicBallItem;
import jempasam.capacityapi.item.MagicElementalItem;
import jempasam.capacityapi.item.MagicPotionItem;
import jempasam.capacityapi.item.MagicScrollItem;
import jempasam.capacityapi.item.MagicTrapItem;
import jempasam.capacityapi.item.MagicTurretItem;
import jempasam.capacityapi.item.MagicWandItem;
import jempasam.capacityapi.item.functionnalities.ICapacityItem;
import jempasam.capacityapi.item.functionnalities.ICategoryItem;
import jempasam.capacityapi.item.functionnalities.IMaterialItem;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CreativeTabRegistry {
	
	public CreativeTabRegistry() {
		CAPIRegistry.TABS.register("capacity_spell", new CapacityTab("capacity_spell",new ItemStack(CAPIItems.MAGIC_WAND),
        		item-> item instanceof MagicWandItem || item instanceof MagicScrollItem ));
        
        CAPIRegistry.TABS.register("capacity_potion", new CapacityTab("capacity_potion",new ItemStack(CAPIItems.MAGIC_POTION),
        		item-> item instanceof MagicPotionItem  ));
        
        CAPIRegistry.TABS.register("capacity_tools", new CapacityTab("capacity_tools",new ItemStack(CAPIItems.MAGIC_CIRCLE),
        		item-> item instanceof MagicBallItem || item instanceof MagicTurretItem || item instanceof MagicTrapItem || item instanceof MagicElementalItem  ));
        
        CAPIRegistry.TABS.register("capacity_capacity", new CapacityTab("capacity_capacity",new ItemStack(CAPIItems.MAGIC_POWDER),
        		item-> !(item instanceof IMaterialItem) && item instanceof ICapacityItem  ));
        
        CAPIRegistry.TABS.register("capacity_materials", new CapacityTab("capacity_materials",new ItemStack(CAPIItems.MATERIAL_INGOT),
        		item-> item instanceof IMaterialItem && !(item instanceof ICapacityItem)  ));
	}
	
}
