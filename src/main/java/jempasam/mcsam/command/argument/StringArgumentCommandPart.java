package jempasam.mcsam.command.argument;

import java.util.Collections;
import java.util.List;

import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.CommandPartException;
import jempasam.mcsam.command.ICommandPart;

public class StringArgumentCommandPart extends DecoratedCommandPart{
	
	
	
	public StringArgumentCommandPart(String name) {
		super(name);
	}
	
	
	
	@Override
	public List<String> complete(CommandContext context) {
		if(context.hasRemaining(2))return then.complete(context.forward(1));
		else return Collections.emptyList();
	}
	
	@Override
	public void execute(CommandContext context) throws CommandPartException {
		if(context.hasRemaining(1)) {
			context.addParameter(name, context.arg(0));
			then.execute(context.forward(1));
		}
		else ICommandPart.error("Miss string argument", context);
	}
	
	@Override
	public boolean matches(CommandContext context) {
		if(context.hasRemaining(1))return then.matches(context.forward(1));
		else return false;
	}
	
	@Override
	protected String getRepresentation() {
		return "string";
	}
}
