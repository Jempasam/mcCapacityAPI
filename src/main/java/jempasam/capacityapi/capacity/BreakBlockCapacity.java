package jempasam.capacityapi.capacity;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

@Loadable
public class BreakBlockCapacity implements Capacity{
	
	
	
	@LoadableParameter
	private float strength=0.5f;
	@LoadableParameter
	private boolean doDrop=false;
	
	
	
	@LoadableParameter
	public BreakBlockCapacity() { }
	
	
	
	@Override
	public boolean use(CapacityContext context) {
		float hardnessmax=strength*context.getPower();
		BlockPos pos=new BlockPos(context.getPos());
		IBlockState state=context.getWorld().getBlockState(pos);
		if(state.getBlock()!=Blocks.AIR && state.getBlockHardness(context.getWorld(), pos)<=hardnessmax) {
			context.getWorld().destroyBlock(pos, doDrop);
			return true;
		}
		else return false;
	}
	
	@Override
	public String getName(CapacityContext context) {
		return (int)(strength*context.getPower()*20)+"N Breaker";
	}
	
	@Override
	public int getColor(CapacityContext context) {
		return 0x687170;
	}
	
	@Override
	public int getMana(CapacityContext context) {
		return (int)(strength*30*(doDrop ? 3f : 1f));
	}
}
