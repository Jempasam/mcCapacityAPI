package jempasam.capacityapi.register;

import jempasam.objectmanager.HashObjectManager;
import jempasam.objectmanager.ObjectManager;

public class ClientSideRegistry {
	
	
	
	public static final ObjectManager<ObjectDisplayInfo> CAPACITIES=new HashObjectManager<>();
	public static final ObjectManager<ObjectDisplayInfo> CATEGORIES=new HashObjectManager<>();
	public static final ObjectManager<ObjectDisplayInfo> MATERIALS=new HashObjectManager<>();
	
	
	
	public static class ObjectDisplayInfo{
		public final String name;
		public final int color;
		public ObjectDisplayInfo(String name, int color) {
			super();
			this.name = name;
			this.color = color;
		}
	}
	
	
	
}
