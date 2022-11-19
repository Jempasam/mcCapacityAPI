package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Loadable
public class AddBlockCapacity implements Capacity{
	
	Block block;
	@LoadableParameter public boolean aironly=false;
	
	@LoadableParameter
	public void block(String id) {
		block=ForgeRegistries.BLOCKS.getValue(new ResourceLocation(id));
	}
	
	@LoadableParameter
	public AddBlockCapacity() {
	}
	
	@Override
	public boolean use(CapacityContext context) {
		BlockPos pos=new BlockPos(context.getPos());
		if(aironly && !context.getWorld().getBlockState(pos).getBlock().isReplaceable(context.getWorld(), pos)) {
			return false;
		}
		else{
			context.getWorld().setBlockState(pos, block.getDefaultState());
		}
		return true;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return block.getLocalizedName()+" Maker";
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return block.getDefaultState().getMaterial().getMaterialMapColor().colorValue;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		/*block.getDefaultState().getBlockHardness(context.getWorld(), new BlockPos(context.getPos()))*/
		return (int)(20);
	}
}
