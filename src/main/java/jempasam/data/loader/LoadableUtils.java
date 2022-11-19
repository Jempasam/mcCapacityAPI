package jempasam.data.loader;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.reflection.ReflectionUtils;

public class LoadableUtils{
	private LoadableUtils() {}
	
	public static Collection<Field> getLoadableFields(Class<?> clazz) {
		List<Field> ret=new ArrayList<>();
		for(Field field : ReflectionUtils.getAllFields(clazz))
			if(field.isAnnotationPresent(LoadableParameter.class))
				ret.add(field);
		return ret;
	}
	
	public static Collection<Method> getLoadableMethods(Class<?> clazz) {
		List<Method> ret=new ArrayList<>();
		for(Method field : ReflectionUtils.getAllMethods(clazz))
			if(field.isAnnotationPresent(LoadableParameter.class))
				ret.add(field);
		return ret;
	}
	
	public static String getName(Field member) {
		LoadableParameter params=member.getAnnotation(LoadableParameter.class);
		if(params.name()!="")return params.name();
		else return member.getName();
	}
	
	public static String getName(Method member) {
		LoadableParameter params=member.getAnnotation(LoadableParameter.class);
		if(params.name()!="")return params.name();
		else return member.getName();
	}
	
	public static boolean isLoadable(Class<?> clazz) {
		return clazz.isAnnotationPresent(Loadable.class);
	}
	
}
