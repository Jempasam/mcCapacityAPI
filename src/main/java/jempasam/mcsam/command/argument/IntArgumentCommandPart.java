package jempasam.mcsam.command.argument;

import java.util.Collections;
import java.util.List;

import org.codehaus.plexus.util.CollectionUtils;

import com.google.common.collect.Lists;

import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.CommandPartException;
import jempasam.mcsam.command.ICommandPart;
import jempasam.mcsam.command.ICommandPartBuildable;
import scala.actors.threadpool.Arrays;

public class IntArgumentCommandPart extends DecoratedCommandPart{
	
	
	
	public IntArgumentCommandPart(String name) {
		super(name);
	}
	
	
	
	@Override
	public List<String> complete(CommandContext context) {
		if(context.hasRemaining(2))return then.complete(context.forward(1));
		else return Lists.newArrayList(context.arg(0)+"0",context.arg(0)+"1",context.arg(0)+"2",context.arg(0)+"3",context.arg(0)+"4",context.arg(0)+"5",context.arg(0)+"6",context.arg(0)+"7",context.arg(0)+"8",context.arg(0)+"9");
	}
	
	@Override
	public void execute(CommandContext context) throws CommandPartException {
		try {
			if(context.hasRemaining(1)) {
				context.addParameter(name, Integer.parseInt(context.arg(0)));
				then.execute(context.forward(1));
			}
			else ICommandPart.error("Miss integer argument", context);
		}catch (NumberFormatException e) {
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
		return "integer";
	}
}
