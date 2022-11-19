package jempasam.capacityapi.block;

import java.util.Map;

import jempasam.capacityapi.block.factoryrecipe.IFactoryRecipe;
import jempasam.capacityapi.register.CAPIRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockMaterialFactory extends Block{

	public BlockMaterialFactory() {
		super(Material.IRON, MapColor.CYAN);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack=playerIn.getHeldItem(hand);
		if(!stack.isEmpty()) {
			for(IFactoryRecipe recipe : CAPIRegistry.FACTORY_RECIPES.stream().map(Map.Entry::getValue)) {
				if(recipe.doAccept(stack)) {
					playerIn.setHeldItem(hand, recipe.getRemaining(stack));
					playerIn.addItemStackToInventory(recipe.craft(stack));
					return true;
				}
			}
		}
		return false;
	}
	
}
