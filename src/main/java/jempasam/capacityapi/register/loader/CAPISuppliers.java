package jempasam.capacityapi.register.loader;

import java.util.List;
import java.util.Random;

import com.google.common.base.Supplier;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.samstream.stream.SamStream;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public interface CAPISuppliers<T> extends Supplier<SamStream<T>>{
	
	public static Random rand=new Random();
	default T getOne() {
		List<T> ret=get().toList();
		return ret.get(rand.nextInt(ret.size()));
	}
	
	public static interface Items extends CAPISuppliers<Item>{ }
	public static interface Blocks extends CAPISuppliers<Block>{ }
	public static interface Capacities extends CAPISuppliers<Capacity>{ }
	public static interface Materials extends CAPISuppliers<MagicMaterial>{ }
}
