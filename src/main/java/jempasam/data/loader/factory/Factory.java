package jempasam.data.loader.factory;

import java.util.List;

public abstract class Factory<T> {
	
	
	
	private List<Param> params;
	
	
	
	public Factory(List<Param> params) {
		super();
		this.params = params;
	}
	
	
	
	public abstract T create(Object... params);
	
	public List<Param> getParams(){
		return params;
	}
	
	public static class Param{
		public final String name;
		public final Class<?> type;
		public Param(String name, Class<?> type) {
			super();
			this.name = name;
			this.type = type;
		}
	}
}
