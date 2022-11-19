package jempasam.capacityapi.command;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import jempasam.capacityapi.CapacityAPI;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.capacityapi.capacity.Capacity;
import jempasam.capacityapi.capacity.CapacityContext;
import jempasam.capacityapi.entity.EntityCapacityProjectile;
import jempasam.capacityapi.entity.EntityCapacityTrap;
import jempasam.capacityapi.entity.EntityCapacityTurret;
import jempasam.capacityapi.material.MagicMaterial;
import jempasam.capacityapi.register.CAPIRegistry;
import jempasam.mcsam.command.CommandRoot;
import jempasam.mcsam.command.ICommandPartBuildable;
import jempasam.mcsam.command.argument.ObjectCommandPart;
import jempasam.mcsam.command.argument.StringArgumentCommandPart;
import jempasam.mcsam.command.misc.MapCommandPart;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;



public class CapacityCommands{
	
	private CapacityCommands(){ }
	
	public static ICommand buildCommandAlter() {
		MapCommandPart root=new MapCommandPart();
		
		root.then("list", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			StringBuilder sb=new StringBuilder();
			for(Capacity c : context.<ICapacityOwner>getParameter("owner").capacities()) {
				sb.append("- ").append(c.getName(CapacityContext.withPower(1))).append('\n');
			}
			TextComponentString str=new TextComponentString(sb.toString());
			context.sender.sendMessage(str);
		});
		
		root.then("generate", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			ICapacityOwner owner=context.getParameter("owner");
			owner.resetCapacities(-1);
			owner.generate(-1, 3);
		});
		
		root.then("reset", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			ICapacityOwner owner=context.getParameter("owner");
			owner.resetCapacities(-1);
		});
		
		root.then("name", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			ICapacityOwner owner=context.getParameter("owner");
			context.sender.sendMessage(new TextComponentString(owner.getAlterName()));
		});
		
		root.then("upgrade", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			ICapacityOwner owner=context.getParameter("owner");
			owner.generate(-1, 1);
		});
		
		root.then("refill", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			ICapacityOwner owner=context.getParameter("owner");
			owner.setMana(owner.getMaxMana());
		});
		
		root.then("materials", new OwnerArgumentCommandPart("owner")).thenDo(context->{
			ICapacityOwner owner=context.getParameter("owner");
			StringBuilder sb=new StringBuilder();
			for(MagicMaterial m : owner.materials()) {
				sb.append("- ").append(m.name).append('\n');
			}
			TextComponentString str=new TextComponentString(sb.toString());
			context.sender.sendMessage(str);
		});
		
		root.then("categories", new StringArgumentCommandPart("searched")).thenDo(context->{
			String searched=context.getParameter("searched");
			StringBuilder sb=new StringBuilder();
			for(Map.Entry<String, Collection<Capacity>> category : CAPIRegistry.CAPACITIES.categories()) if(category.getKey().contains(searched)) {
				sb.append(category.getKey()).append(":").append("\n");
				for(Capacity c : category.getValue()) {
					sb.append("  -").append(CAPIRegistry.CAPACITIES.idOf(c)).append("\n");
				}
			}
			TextComponentString str=new TextComponentString(sb.toString());
			context.sender.sendMessage(str);
		});
		
		return new CommandRoot("alters", Collections.emptyList(), root);
	}
	
	
	
	private static ICommandPartBuildable capactityList(String name) {
		return new ObjectCommandPart<>(name, CAPIRegistry.CAPACITIES, "capacity", e->!e.getKey().startsWith("_"));
	}
	
	
	
	public static ICommand buildCommandCapacity() {
		MapCommandPart root=new MapCommandPart();
		
		root.then("use", capactityList("used")).thenInt("power").thenDo(context->{
			Capacity capacity=context.getParameter("used");
			Integer power=context.getParameter("power");
			CapacityContext cc=CapacityContext.atSender(context.sender.getCommandSenderEntity(), power);
			if(capacity.use(cc)) context.sender.sendMessage(new TextComponentString("Suceed for "+capacity.getMana(cc)+" mana"));
			else context.sender.sendMessage(new TextComponentString("Fail"));
		});
		
		root.then("shoot", capactityList("used")).thenInt("power").thenFloat("distance").thenDo(context->{
			Capacity capacity=context.getParameter("used");
			int power=context.getParameter("power");
			float distance=context.getParameter("distance");
			Entity entity=context.sender.getCommandSenderEntity();
			World world=context.sender.getEntityWorld();
			EntityCapacityProjectile projectile=new EntityCapacityProjectile(world);
			projectile.capacities().fill(capacity, entity, entity, power);
			projectile.setPosition(entity.posX,entity.getEyeHeight()+entity.posY,entity.posZ);
			projectile.shootingEntity=entity;
			projectile.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0f, distance, 0.0f);
			context.sender.getEntityWorld().spawnEntity(projectile);
		});
		
		root.then("trap", capactityList("used")).thenInt("power").thenFloat("range").thenDo(context->{
			Capacity capacity=context.getParameter("used");
			int power=context.getParameter("power");
			float range=context.getParameter("range");
			Entity entity=context.sender.getCommandSenderEntity();
			World world=context.sender.getEntityWorld();
			EntityCapacityTrap projectile=new EntityCapacityTrap(world);
			projectile.capacities().fill(capacity, entity, entity, power);
			projectile.setPosition(entity.posX,entity.posY+0.1,entity.posZ);
			projectile.range=range;
			projectile.timeout=(int)(20+projectile.range*20);
			context.sender.getEntityWorld().spawnEntity(projectile);
		});
		
		root.then("turret", capactityList("used")).thenInt("power").thenInt("repetition").thenInt("time").thenDo(context->{
			Capacity capacity=context.getParameter("used");
			int power=context.getParameter("power");
			int repetition=context.getParameter("repetition");
			int reloadtime=context.getParameter("reloadtime");
			Entity entity=context.sender.getCommandSenderEntity();
			World world=context.sender.getEntityWorld();
			EntityCapacityTurret projectile=new EntityCapacityTurret(world);
			projectile.ccontainer.fill(capacity, entity, entity, power);
			projectile.setPosition(entity.posX,entity.posY+0.1,entity.posZ);
			projectile.rotationYaw=entity.rotationYaw;
			projectile.rotationPitch=entity.rotationPitch;
			projectile.repetition=repetition;
			projectile.reloadTime=reloadtime;
			context.sender.getEntityWorld().spawnEntity(projectile);
		});
		
		root.then("doubleturret", capactityList("used")).thenInt("power").then(capactityList("final")).thenInt("repetition").thenInt("time").thenDo(context->{
			Capacity capacity=context.getParameter("used");
			int power=context.getParameter("power");
			Capacity finall=context.getParameter("final");
			int repetition=context.getParameter("repetition");
			int reloadtime=context.getParameter("reloadtime");
			Entity entity=context.sender.getCommandSenderEntity();
			World world=context.sender.getEntityWorld();
			EntityCapacityTurret projectile=new EntityCapacityTurret(world);
			projectile.ccontainer.fill(capacity, entity, entity, power);
			projectile.ccontainer.setCapacity2(finall);
			projectile.setPosition(entity.posX,entity.posY+0.1,entity.posZ);
			projectile.rotationYaw=entity.rotationYaw;
			projectile.rotationPitch=entity.rotationPitch;
			projectile.repetition=repetition;
			projectile.reloadTime=reloadtime;
			context.sender.getEntityWorld().spawnEntity(projectile);
		});
		
		root.then("effect", capactityList("capacity")).thenInt("duration").thenFloat("level").thenDo(context->{
			Capacity capacity=context.getParameter("capacity");
			int duration=context.getParameter("duration");
			int level=context.getParameter("level");
			CapacityAPI.EFFECT.applyCapacityEffect((EntityLivingBase)context.sender.getCommandSenderEntity(), capacity, duration*20, level);
		});
		
		root.thenDo("list", context->{
			StringBuilder sb=new StringBuilder();
			for(Map.Entry<String,Capacity> c : CAPIRegistry.CAPACITIES) {
				if(!c.getKey().startsWith("_"))sb.append("- ").append(c.getKey()).append('\n');
			}
			TextComponentString str=new TextComponentString(sb.toString());
			context.sender.sendMessage(str);
		});
		
		root.thenDo("materials", context->{
			StringBuilder sb=new StringBuilder();
			for(Map.Entry<String, MagicMaterial> material : CAPIRegistry.MATERIALS.stream()) {
				sb.append("- ").append(material.getKey()).append(": ").append(material.getValue().getName()).append('\n');
			}
			TextComponentString str=new TextComponentString(sb.toString());
			context.sender.sendMessage(str);
		});
		
		root.thenDo("reload", context->CAPIRegistry.reload());
		
		return new CommandRoot("capacity", Collections.emptyList(), root);
	}
}
