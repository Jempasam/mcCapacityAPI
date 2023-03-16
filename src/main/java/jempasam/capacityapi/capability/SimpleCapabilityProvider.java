package jempasam.capacityapi.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SimpleCapabilityProvider<T> implements ICapabilitySerializable<NBTBase>{
	
	
	
	private Capability<T> capability;
	private T instance;
	private EnumFacing face;
	
	
	
	public SimpleCapabilityProvider(Capability<T> capability, T capinstance, EnumFacing face) {
		this.capability=capability;
		this.face=face;
		this.instance=capinstance;
	}
	
	
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (face==null || facing==face) && capability==this.capability;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <E> E getCapability(Capability<E> capability, EnumFacing facing) {
		if(hasCapability(capability, facing))return (E)instance;
		else return null;
	}
	
	@Override
	public NBTBase serializeNBT() {
		return capability.writeNBT(instance, face);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt) {
		capability.readNBT(instance, face, nbt);
	};

}
