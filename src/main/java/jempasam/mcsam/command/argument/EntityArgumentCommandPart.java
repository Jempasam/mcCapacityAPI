package jempasam.mcsam.command.argument;

import java.util.Arrays;
import java.util.List;

import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.CommandPartException;
import jempasam.mcsam.command.ICommandPart;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.entity.Entity;

public class EntityArgumentCommandPart extends DecoratedCommandPart{
	
	
	
	public EntityArgumentCommandPart(String name) {
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
				context.addParameter(name, player);
				then.execute(context.forward(1));
			}
			else{
				context.addParameter(name, context.sender.getCommandSenderEntity());
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
		return "player";
	}
}
