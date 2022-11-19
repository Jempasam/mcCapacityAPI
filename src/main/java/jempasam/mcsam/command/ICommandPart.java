package jempasam.mcsam.command;

import java.util.ArrayList;
import java.util.List;

public interface ICommandPart{
	
	public boolean matches(CommandContext context);
	public void execute(CommandContext context) throws CommandPartException;
	public List<String> complete(CommandContext context);
	public List<String> usage(CommandContext context);
	
	public static void error(String message, CommandContext context) throws CommandPartException {
		throw new CommandPartException(message, context);
	}
	
	public static List<String> filterAutocompletion(Iterable<String> tokens, String lastarg){
		List<String> ret=new ArrayList<>();
		for(String t : tokens)if(t.startsWith(lastarg))ret.add(t);
		return ret;
	}
}
