package jempasam.capacityapi.recipe;

import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.item.CAPIItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class CAPIRecipes {
	private CAPIRecipes() {}
	
	
	public static void registerRecipes() {
		ForgeRegistries.RECIPES.register(new CAPIRecipe(1,3)
			.add(CAPIItems.MAGIC_POWDER, -1, 0)
			.add(CAPIItems.MATERIAL_INGOT, 0, -1)
			.add(CAPIItems.MATERIAL_INGOT, 0, -1)
			.setResult(CAPIItems.MAGIC_WAND.getDefaultInstance(), 0, 0)
			.setRegistryName(CapacityAPI.MODID, "magic_wand"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(1,3)
				.add(CAPIItems.MAGIC_POWDER, -1, 0)
				.add(Items.PAPER)
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)
        		.setResult(new ItemStack(CAPIItems.MAGIC_SCROLL, 16), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_scroll"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,3)
				.empty()																		.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.empty()
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(CAPIItems.MAGIC_POWDER, -1, 0)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.empty()																		.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.empty()
        		.setResult(new ItemStack(CAPIItems.MAGIC_CIRCLE, 8), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_circle"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,3)
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)	
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.add(CAPIItems.MAGIC_POWDER, -1, 0)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)	
        		.setResult(new ItemStack(CAPIItems.MAGIC_SPHERE, 8), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_sphere"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,2)
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(CAPIItems.MAGIC_POWDER, -1, 0)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.empty()																		.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.empty()
        		.setResult(new ItemStack(CAPIItems.MAGIC_SOUP, 1), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_soup"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,3)
				.empty()																		.add(Items.BREAD)													.empty()
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(CAPIItems.MAGIC_POWDER, -1, 0)				.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.empty()																		.add(Items.BREAD)													.empty()
        		.setResult(new ItemStack(CAPIItems.MAGIC_PILL, 2), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_pill"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,3)
				.empty()																		.add(CAPIItems.MATERIAL_INGOT, 0, -1)		.empty()
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(CAPIItems.MAGIC_POWDER, -1, 0)		.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.empty()																		.add(Items.GUNPOWDER)								.empty()
        		.setResult(new ItemStack(CAPIItems.MAGIC_BALL, 8), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_ball"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,3)
				.empty()									.empty()																		.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.empty()									.add(CAPIItems.MAGIC_POWDER, -1, 0)				.empty()
				.add(Items.STICK)				.empty()																		.empty()
        		.setResult(new ItemStack(CAPIItems.MAGIC_VACCIN, 4), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_vaccin"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,2)
				.empty()																		.add(CAPIItems.MAGIC_POWDER, -1, 0)		.empty()
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(Items.GLASS_BOTTLE)							.add(CAPIItems.MATERIAL_INGOT, 0, -1)
        		.setResult(new ItemStack(CAPIItems.MAGIC_POTION, 4), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_potion"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,2)
				.empty()																		.add(CAPIItems.MAGIC_POWDER, -1, 0)		.empty()
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(Items.GLASS_BOTTLE)							.add(CAPIItems.MATERIAL_INGOT, 0, -1)
        		.setResult(new ItemStack(CAPIItems.MAGIC_POTION, 4), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_potion"));
		
		ForgeRegistries.RECIPES.register(new CAPIRecipe(3,3)
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(Items.ENDER_PEARL)								.add(CAPIItems.MATERIAL_INGOT, 0, -1)
				.add(Items.BLAZE_ROD)											.add(CAPIItems.MAGIC_POWDER, -1, 0)		.add(Items.BLAZE_ROD)
				.add(CAPIItems.MATERIAL_INGOT, 0, -1)				.add(Items.BONE)											.add(CAPIItems.MATERIAL_INGOT, 0, -1)
        		.setResult(new ItemStack(CAPIItems.MAGIC_BONE, 6), 0, 0)
        		.setRegistryName(CapacityAPI.MODID, "magic_bone"));
	}
}
