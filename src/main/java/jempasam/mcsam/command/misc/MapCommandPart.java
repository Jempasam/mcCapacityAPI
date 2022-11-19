package jempasam.mcsam.command.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.CommandPartException;
import jempasam.mcsam.command.ICommandAction;
import jempasam.mcsam.command.ICommandPart;
import jempasam.mcsam.command.ICommandPartBuildable;

public class MapCommandPart implements ICommandPart{
	
	
	
	private Map<String,ICommandPart> subcommands;
	
	
	
	public MapCommandPart() {
		super();
		subcommands=new HashMap<>();
	}
	
	
	
	public ICommandPart thenFinally(String token, ICommandPart subcommand) {
		subcommands.put(token, subcommand);
		return subcommand;
	}
	
	public ICommandPartBuildable then(String token, ICommandPartBuildable subcommand) {
		subcommands.put(token, subcommand);
		return subcommand;
	}
	
	public ICommandPart thenDo(String token, ICommandAction action) {
		return thenFinally(token, new ExecuteCommandPart() {
			@Override public void execute(CommandContext context) throws CommandPartException { action.execute(context); }
		});
	}
	
	@Override
	public void execute(CommandContext context) throws CommandPartException {
		if(context.hasRemaining(1)) {
			ICommandPart part=subcommands.get(context.arg(0));
			if(part==null)ICommandPart.error("Bad subcommand name", context);
			else part.execute(context.forward(1));
		}
		else ICommandPart.error("Miss subcommand name", context);
	}
	
	@Override
	public List<String> complete(CommandContext context) {
		if(context.hasRemaining(2)) {
			ICommandPart part=subcommands.get(context.arg(0));
			if(part==null)return Collections.emptyList();
			else return part.complete(context.forward(1));
		}
		else return ICommandPart.filterAutocompletion(subcommands.keySet(),context.arg(0));
	}
	
	@Override
	public boolean matches(CommandContext context) {
		if(context.hasRemaining(1)) {
			ICommandPart part=subcommands.get(context.arg(0));
			if(part==null)return false;
			else return part.matches(context.forward(1));
		}
		else return false;
	}
	
	@Override
	public List<String> usage(CommandContext context) {
		List<String> ret=new ArrayList<>();
		for(Map.Entry<String, ICommandPart> part : subcommands.entrySet()) {
			for(String str : part.getValue().usage(context)) {
				ret.add(part.getKey()+" "+str);
			}
		}
		return ret;
	}
}
