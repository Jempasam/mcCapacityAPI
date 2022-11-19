package jempasam.capacityapi.recipe;

import java.util.ArrayList;
import java.util.List;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.item.CapacityItem;
import jempasam.capacityapi.item.MaterialItem;
import jempasam.capacityapi.item.functionnalities.ICapacityItem;
import jempasam.capacityapi.item.functionnalities.IMaterialItem;
import jempasam.capacityapi.material.MagicMaterial;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.IShapedRecipe;
import scala.actors.threadpool.Arrays;

public class CAPIRecipe extends net.minecraftforge.registries.IForgeRegistryEntry.Impl<IRecipe> implements IShapedRecipe{
	
	
	
	private NonNullList<Ingredient> ingredients;
	private int width;
	private int height;
	private ItemStack result;
	private int resultMaterialGroup;
	private int resultCapacityGroup;
	
	
	
	public CAPIRecipe(int width, int height) {
		super();
		this.ingredients = NonNullList.create();
		this.width = width;
		this.height = height;
	}
	
	
	
	public CAPIRecipe add(CapacityIngredient ingredient) {
		ingredients.add(ingredient);
		return this;
	}
	
	public CAPIRecipe add(Item item, int mgroup, int cgroup) {
		return add(new CapacityIngredient(mgroup, cgroup, item.getDefaultInstance()));
	}
	
	public CAPIRecipe add(Ingredient item) {
		ingredients.add(item);
		return this;
	}
	
	public CAPIRecipe add(Item item) {
		return add(Ingredient.fromItem(item));
	}
	
	public CAPIRecipe empty() {
		ingredients.add(Ingredient.EMPTY);
		return this;
	}
	
	public CAPIRecipe setResult(ItemStack stack, int resultMaterialGroup, int resultCapacityGroup) {
		result=stack;
		this.resultCapacityGroup=resultCapacityGroup;
		this.resultMaterialGroup=resultMaterialGroup;
		return this;
	}
	
	@Override
	public int getRecipeHeight() {
		return height;
	}
	
	@Override
	public int getRecipeWidth() {
		return width;
	}
	
	@Override
	public NonNullList<Ingredient> getIngredients() {
		return ingredients;
	}
	
	@Override
	public boolean canFit(int width, int height) {
		return width>=this.width && height>=this.height;
	}
	
	@Override
	public ItemStack getRecipeOutput() {
		return result;
	}
	
	@Override
	public boolean matches(InventoryCrafting inv, World worldIn) {
		List<MagicMaterial> materials=new ArrayList<>();
		List<Capacity> capacities=new ArrayList<>();
		System.out.println("Try");
		for(int x=0; x<=inv.getWidth()-width; x++) {
			for(int y=0; y<=inv.getHeight()-height; y++) {
				materials.clear(); capacities.clear();
				if(matchesWithOffset(inv, x, y, 1, 1, materials, capacities))return true;
				materials.clear(); capacities.clear();
				if(matchesWithOffset(inv, inv.getWidth()-x-1, y, -1, 1, materials, capacities))return true;
			}
		}
		return false;
	}
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv) {
		System.out.println("Build");
		List<MagicMaterial> materials=new ArrayList<>();
		List<Capacity> capacities=new ArrayList<>();
		for(int x=0; x<=inv.getWidth()-width; x++) {
			for(int y=0; y<=inv.getHeight()-height; y++) {
				materials.clear(); capacities.clear();
				if(matchesWithOffset(inv, x, y, 1, 1, materials, capacities))return buildResult(materials,capacities);
				materials.clear(); capacities.clear();
				if(matchesWithOffset(inv, inv.getWidth()-x-1, y, -1, 1, materials, capacities))return buildResult(materials,capacities);
			}
		}
		return null;
	}
	
	public ItemStack buildResult(List<MagicMaterial> materials, List<Capacity> capacities) {
		System.out.println(materials+" "+capacities);
		ItemStack ret=result.copy();
		if(ret.getItem() instanceof ICapacityItem) {
			((ICapacityItem)result.getItem()).setCapacity(ret, capacities.get(resultCapacityGroup));
		}
		if(ret.getItem() instanceof IMaterialItem) {
			((IMaterialItem)result.getItem()).setMaterial(ret, materials.get(resultMaterialGroup));
		}
		return ret;
	}
	
	public boolean matchesWithOffset(InventoryCrafting inv, int xOffset, int yOffset, int xDirection, int yDirection, List<MagicMaterial> materials, List<Capacity> capacities) {
		System.out.println("  With");
		for(int x=0; x<width; x++) {
			for(int y=0; y<height; y++) {
				int sx=x*xDirection+xOffset;
				int sy=y*yDirection+yOffset;
				ItemStack stack=inv.getStackInSlot(sx+sy*inv.getWidth());
				System.out.println("    "+x+" "+y+":"+sx+" "+sy+":"+stack);
				if(!doMatch(stack, x, y, materials, capacities))return false;
			}
		}
		System.out.println("Succeed");
		return true;
	}
	
	public boolean doMatch(ItemStack stack, int x, int y, List<MagicMaterial> materials, List<Capacity> capacities) {
		int slot=x+y*width;
		Ingredient ingredient=ingredients.get(slot);
		System.out.println("       "+ingredient);
		if(ingredient.test(stack)) {
			System.out.println("       OK");
			if(ingredient instanceof CapacityIngredient) {
				CapacityIngredient cingredient=(CapacityIngredient)ingredient;
				if(cingredient.materialGroup!=-1 && stack.getItem() instanceof IMaterialItem) {
					IMaterialItem mitem=(IMaterialItem)stack.getItem();
					MagicMaterial material=mitem.getMaterial(stack);
					while(materials.size()<=cingredient.materialGroup)materials.add(null);
					if(materials.get(cingredient.materialGroup)==null)materials.set(cingredient.materialGroup, material);
					else if(materials.get(cingredient.materialGroup)!=material)return false;
				}
				if(cingredient.capacityGroup!=-1 && stack.getItem() instanceof ICapacityItem) {
					ICapacityItem citem=(ICapacityItem)stack.getItem();
					Capacity capacity=citem.getCapacity(stack);
					while(capacities.size()<=cingredient.capacityGroup)capacities.add(null);
					if(capacities.get(cingredient.capacityGroup)==null)capacities.set(cingredient.capacityGroup, capacity);
					else if(capacities.get(cingredient.capacityGroup)!=capacity)return false;
				}
				return true;
			}
			else return true;
		}
		else return false;
	}
	
	
	
	
	
	
	
	
	public static class CapacityIngredient extends Ingredient{
		
		public final int materialGroup;
		public final int capacityGroup;
		
		public CapacityIngredient(int materialGroup, int capacityGroup, ItemStack... p_i47503_1_) {
			super(p_i47503_1_);
			this.materialGroup=materialGroup;
			this.capacityGroup=capacityGroup;
		}
		
		@Override
		public String toString() {
			return "["+Arrays.toString(this.getMatchingStacks())+"]"+materialGroup+" "+capacityGroup;
		}
		
	}
	
}
