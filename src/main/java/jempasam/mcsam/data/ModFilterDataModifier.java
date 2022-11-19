package jempasam.mcsam.data;

import java.util.ArrayList;
import java.util.List;

import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.modifier.DataModifier;
import net.minecraftforge.fml.common.Loader;

public class ModFilterDataModifier implements DataModifier{
	@Override
	public void applyOn(ObjectChunk root) {
		List<Runnable> todo=new ArrayList<>();
		for(DataChunk child : root.childStream()) {
			if(child instanceof ObjectChunk && child.getName().startsWith("with-")) {
				ObjectChunk ochild=(ObjectChunk)child;
				todo.add(()->{
					int index=root.find(child);
					root.remove(index);
					if(Loader.isModLoaded(child.getName().substring(5))) ochild.forEach(dc->root.add(index,dc));
				});
			}
		}
		todo.forEach(Runnable::run);
	}
}
