package jempasam.objectmanager;

import java.util.Map.Entry;
import java.util.function.Supplier;

import jempasam.samstream.Streamable;

public interface ObjectManager<T> extends Streamable<Entry<String,T>> {
	T get(String name);
	String idOf(T name);
	T register(String name, T obj);
	boolean unregister(T obj);
	void clear();
	int size();
	
	default T unregister(String name) {
		T toremove=get(name);
		if(toremove==null)return null;
		else {
			unregister(toremove);
			return toremove;
		}
	}
	
	default <Y extends T> Y getOrDefault(Class<Y> type, String name, Supplier<Y> def) {
		T ret=get(name);
		if(ret!=null && type.isInstance(ret)) return type.cast(ret);
		else return def.get();
	}
}
