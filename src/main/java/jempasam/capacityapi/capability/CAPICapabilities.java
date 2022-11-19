package jempasam.capacityapi.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CAPICapabilities {
	private CAPICapabilities() {}
	
	@CapabilityInject(ICapacityOwner.class)
	public static Capability<ICapacityOwner> CAPACITY_OWNER=null;
}
