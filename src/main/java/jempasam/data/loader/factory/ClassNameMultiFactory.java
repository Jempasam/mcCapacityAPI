package jempasam.data.loader.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.function.Supplier;

import jempasam.data.chunk.DataChunk;
import jempasam.data.chunk.ObjectChunk;
import jempasam.data.chunk.value.StringChunk;
import jempasam.data.loader.tags.Loadable;
import jempasam.data.loader.tags.LoadableParameter;
import jempasam.objectmanager.ObjectManager;
import jempasam.reflection.ReflectionUtils;

public class ClassNameMultiFactory implements IMultiFactory{
	
	
	
	private String prefix;
	private String suffix;
	private ObjectManager<?> manager;
	
	
	
	public ClassNameMultiFactory(ObjectManager<?> manager, String prefix, String suffix) {
		this(manager);
		this.prefix = prefix;
		this.suffix = suffix;
	}

	public ClassNameMultiFactory(ObjectManager<?> manager, String prefix) {
		this(manager);
		this.prefix = prefix;
		this.suffix = "";
	}

	public ClassNameMultiFactory(ObjectManager<?> manager) {
		super();
		this.manager = manager;
	}
	
	
	
	@Override
	public List<Factory<?>> instantiate(Class<?> baseClass, String name) throws FactoryException{
		// Cut and get names
		String classname=null;
		String factoryname=null;
		if(name!=null) {
			String[] splited=name.split("\\.",2);
			classname=splited[0];
			if(splited.length>1)factoryname=splited[1];
		}
		
		// Get type
		try {
			Class<?> type=getType(baseClass, classname);
		} catch (ClassNotFoundException e) {
			return null;
		}
		
		// Create Object
		Supplier<?> factory=getFactory(baseClass,name);
	}
	
	private Supplier<?> getFactory(Class<?> type, String factoryname) {
		if(factoryname==null) {
			for(Constructor<?> constructor : type.getDeclaredConstructors()) {
				if(constructor.isAnnotationPresent(LoadableParameter.class)) {
					String[] names=constructor.getAnnotation(LoadableParameter.class).paramnames();
					if(constructor.getParameterCount()!=names.length)logger.error("Bad parameter name count of constructor of "+clazz.getSimpleName());
					Object args[]=new Object[names.length];
					used.clear();
					for(int i=0; i<names.length; i++) {
						DataChunk dataparam=data.get(names[i]);
						if(dataparam==null)break;
						Object value=getValue(dataparam, constructor.getParameters()[i].getType());
						if(value==null)break;
						args[i]=value;
						used.add(names[i]);
					}
					try {
						constructor.setAccessible(true);
						Object newobject=constructor.newInstance(args);
						constructor.setAccessible(false);
						return newobject;
					}catch (InstantiationError | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						logger.error("Instantiation error "+e.getClass().getSimpleName());
						return null;
					}
				}
			}
		}
	}
	
	private Class<?> getType(Class<?> baseClass, String name) throws ClassNotFoundException{
		ClassLoader loader=getClass().getClassLoader();
		Class<?> type=null;
		
		if(name==null) {
			try {
				Method defaultclass=ReflectionUtils.getMethod(baseClass, "defaultSubclass");
				if(defaultclass==null || !Modifier.isStatic(defaultclass.getModifiers())) throw new Exception();
				defaultclass.setAccessible(true);
				type=(Class<?>)defaultclass.invoke(null);
				defaultclass.setAccessible(false);
			}catch(Exception e) {
				type=baseClass;
				if(!type.isAnnotationPresent(Loadable.class))new ClassNotFoundException("Miss the parameter \"type\".");
			}
		}
		else {
			try {
				type=loader.loadClass(prefix+name+suffix);
			}catch(ClassNotFoundException e) {
				try {
					type=loader.loadClass(name);
				}catch(ClassNotFoundException ee) {
					type=null;
				}
			}

			//Check if type exist
			if(type==null || !type.isAnnotationPresent(Loadable.class))
				throw new ClassNotFoundException("Class \""+prefix+name+suffix+"\" or \""+name+"\" does not exist.");
		}
		
		//Check if type is child of roottype
		if(!baseClass.isAssignableFrom(type))
			throw new ClassNotFoundException("The type \""+type.getName()+"\" cannot be used here.");
		
		return type;
	}
	
	
}
