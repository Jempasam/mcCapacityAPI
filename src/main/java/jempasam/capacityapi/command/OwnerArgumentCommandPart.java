package jempasam.capacityapi.command;

import java.util.Arrays;
import java.util.List;

import jempasam.capacityapi.capability.CAPICapabilities;
import jempasam.capacityapi.capability.ICapacityOwner;
import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.CommandPartException;
import jempasam.mcsam.command.ICommandPart;
import jempasam.mcsam.command.argument.DecoratedCommandPart;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;

public class OwnerArgumentCommandPart extends DecoratedCommandPart{
	
	
	
	public OwnerArgumentCommandPart(String name) {
		super(name);
	}
	
	
	
	@Override
	public List<String> complete(CommandContext context) {
		if(context.hasRemaining(2))return then.complete(context.forward(1));
		else return Arrays.asList(context.server.getOnlinePlayerNames());
	}
	
	@Override
	public void execute(CommandContext context) throws CommandPartException {
		try {
			if(context.hasRemaining(1)) {
				Entity player=CommandBase.getPlayer(context.server, context.sender, context.arg(0));
				ICapacityOwner owner=player.getCapability(CAPICapabilities.CAPACITY_OWNER, null);
				if(owner==null)ICommandPart.error("The entity is not a capacity owner", context);
				context.addParameter(name, owner);
				then.execute(context.forward(1));
			}
			else{
				ICapacityOwner owner=context.sender.getCommandSenderEntity().getCapability(CAPICapabilities.CAPACITY_OWNER, null);
				if(owner==null)ICommandPart.error("The entity is not a capacity owner", context);
				context.addParameter(name, owner);
				then.execute(context);
			};
		}catch (CommandException e) {
			ICommandPart.error(e.getMessage(), context);
		}
	}
	
	@Override
	public boolean matches(CommandContext context) {
		if(context.hasRemaining(1))return then.matches(context.forward(1));
		else return false;
	}
	
	@Override
	protected String getRepresentation() {
		return "capacity owner";
	}
}
