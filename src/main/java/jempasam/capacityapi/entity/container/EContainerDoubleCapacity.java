package jempasam.capacityapi.entity.container;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.Capacity;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EContainerDoubleCapacity extends EContainerCapacity{
	
	
	
	private Capacity capacity2=null;
	@SideOnly(Side.CLIENT)public float red2;
	@SideOnly(Side.CLIENT)public float green2;
	@SideOnly(Side.CLIENT)public float blue2;
	@SideOnly(Side.CLIENT)public boolean hasCapacity2=false;
	
	
	
	public EContainerDoubleCapacity(Entity holder) {
		super(holder);
	}
	
	
	
	public void setCapacity2(Capacity capacity) {
		this.capacity2=capacity;
	}
	
	public Capacity capacity2() {
		return capacity2;
	}
	
	public void use2(Entity target) {
		capacity2.use(getContext(target));
	}
	
	
	
	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		super.deserializeNBT(nbt);
		capacity2=getCapacity(nbt, "capacity2");
	}
	
	@Override
	public NBTTagCompound serializeNBT() {
		NBTTagCompound nbt=super.serializeNBT();
		setCapacity(nbt, "capacity2", capacity2);
		return nbt;
	}
	
	@Override
	public void readSpawnData(ByteBuf buffer) {
		super.readSpawnData(buffer);
		red2=buffer.readFloat();
		if(red2!=-1) {
			green2=buffer.readFloat();
			blue2=buffer.readFloat();
			hasCapacity2=true;
		}
		else {red2=red; green2=green; blue2=blue;}
		
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		super.writeSpawnData(buffer);
		writeCapacityColor(capacity2, buffer);
	}
}
