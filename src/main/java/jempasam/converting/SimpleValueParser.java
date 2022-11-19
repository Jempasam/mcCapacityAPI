package jempasam.converting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class SimpleValueParser implements ValueParser{
	
	private Map<Class<?>, Map<Class<?>, Function<? extends Object, ? extends Object>>> parsers;
	
	public SimpleValueParser() {
		parsers=new HashMap<>();
	}

	
	@SuppressWarnings("unchecked")
	public <F,T> T parse(Class<T> to, F converted) {
		Map<Class<?>, Function<? extends Object, ? extends Object>> fromToConverterMap=parsers.get(to);
		if(fromToConverterMap!=null) {
			Function<? extends Object, ? extends Object> parser=get(fromToConverterMap, converted);
			if(parser==null)return null;
			else return ((Function<F,T>)parser).apply(converted);
		}
		return null;
	}
	
	public <F,T> void add(Class<F> from, Class<T> to, Function<F,T> serializer) {
		Map<Class<?>, Function<? extends Object, ? extends Object>> fromToConverterMap=parsers.get(to);
		if(fromToConverterMap==null) {
			fromToConverterMap=new HashMap<>();
			parsers.put(to, fromToConverterMap);
		}
		
		fromToConverterMap.put(from, serializer);
	}
	
	private static <V> V get(Map<Class<?>, V> map, Object value) {
		Set<Class<?>> classes;
		Set<Class<?>> nclasses=new HashSet<>();
		nclasses.add(value.getClass());
		while(!nclasses.isEmpty()) {
			classes=nclasses;
			nclasses=new HashSet<>();
			for(Class<?> type : classes) {
				V ret=map.get(type);
				if(ret!=null)return ret;
				Class<?> clazz=type.getSuperclass();
				if(clazz!=null && clazz!=Object.class)nclasses.add(clazz);
				nclasses.addAll(Arrays.asList(type.getInterfaces()));
			}
		}
		if(value.getClass().isPrimitive())return null;
		else return map.get(Object.class);
	}

}
