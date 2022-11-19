package jempasam.capacityapi.entity;

import io.netty.buffer.ByteBuf;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.container.EContainerDoubleCapacity;
import jempasam.capacityapi.entity.render.IColorable;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityDoubleCapacity extends Entity implements IEntityAdditionalSpawnData, IColorable{
	
	
	
	public EContainerDoubleCapacity ccontainer;
	
	
	
	protected EntityDoubleCapacity(World worldIn) {
		super(worldIn);
		ccontainer=new EContainerDoubleCapacity(this);
		setSize(0.5f, 0.5f);
	}
	
	
	
	@Override
	public boolean hasNoGravity() {
		return true;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		ccontainer.deserializeNBT(nbt.getCompoundTag("capacity"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		nbt.setTag("capacities", ccontainer.serializeNBT());
	}
	
	@Override
	public void readSpawnData(ByteBuf buffer) {
		ccontainer.readSpawnData(buffer);
	}
	
	@Override
	public void writeSpawnData(ByteBuf buffer) {
		ccontainer.writeSpawnData(buffer);
	}
	
	@Override
	public boolean isImmuneToExplosions() {
		return true;
	}
	
	protected CapacityContext getContext(Entity target) {
		return ccontainer.getContext(target);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Vec3d getColor() {
		return new Vec3d(ccontainer.red, ccontainer.green, ccontainer.blue);
	}
	
}
