package jempasam.data.doc;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.SimpleObjectChunk;
import jempasam.data.chunk.value.StringChunk;
import jempasam.data.loader.LoadableUtils;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;

public class SimpleObjectDocMaker implements ObjectDocMaker{
	@Override
	public ObjectChunk getDocFor(List<Class<?>> classes) {
		ObjectChunk doc=new SimpleObjectChunk("doc");
		for(Class<?> clazz : classes) {
			ObjectChunk obj=new SimpleObjectChunk(clazz.getSimpleName());
			if(clazz.isAnnotationPresent(Loadable.class)) {
				for(Field field : LoadableUtils.getLoadableFields(clazz))obj.add(new StringChunk(LoadableUtils.getName(field), "?"+field.getType().getSimpleName()));
				for(Method method : LoadableUtils.getLoadableMethods(clazz))obj.add(new StringChunk(LoadableUtils.getName(method), "?"+method.getParameterTypes()[0].getSimpleName()));
				for(Constructor<?> constructor : clazz.getConstructors()) if(constructor.isAnnotationPresent(LoadableParameter.class)) {
					String[] names=constructor.getAnnotation(LoadableParameter.class).paramnames();
					Class<?>[] types=constructor.getParameterTypes();
					for(int i=0; i<names.length; i++) obj.add(new StringChunk(names[i], types[i].getSimpleName()));
				}
			}
			doc.add(obj);
		}
		return doc;
	}
}
