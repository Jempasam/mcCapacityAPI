package jempasam.mcsam.command.argument;

import java.util.ArrayList;
import java.util.List;

import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.ICommandPart;
import jempasam.mcsam.command.ICommandPartBuildable;

public abstract class DecoratedCommandPart implements ICommandPartBuildable{
	
	
	
	protected ICommandPart then;
	protected String name;
	
	
	
	public DecoratedCommandPart(String name) {
		this.name=name;
	}
	
	
	
	@Override
	public void thenFinally(ICommandPart commandpart) {
		then=commandpart;
	}
	
	@Override
	public List<String> usage(CommandContext context) {
		List<String> ret=new ArrayList<>();
		String representation=getRepresentation();
		for(String str : then.complete(context)) {
			ret.add("<"+name+":"+representation+"> "+str);
		}
		return ret;
	}
	
	protected abstract String getRepresentation();
}
