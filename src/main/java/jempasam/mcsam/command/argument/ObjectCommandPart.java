package jempasam.mcsam.command.argument;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import jempasam.capacityapi.capacity.Capacity;
import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.CommandPartException;
import jempasam.mcsam.command.ICommandPart;
import jempasam.objectmanager.ObjectManager;

public class ObjectCommandPart<T> extends DecoratedCommandPart{
	
	
	
	private ObjectManager<T> manager;
	private Predicate<Map.Entry<String,T>> test;
	private String typename;
	
	
	
	public ObjectCommandPart(String name, ObjectManager<T> manager, String typename, Predicate<Map.Entry<String,T>> test) {
		super(name);
		this.manager = manager;
		this.typename = typename;
		this.test = test;
	}
	
	
	
	@Override
	protected String getRepresentation() {
		return typename;
	}
	
	@Override
	public List<String> complete(CommandContext context) {
		if(context.hasRemaining(2))return then.complete(context.forward(1));
		else return ICommandPart.filterAutocompletion(manager.stream().filter(test).map(Map.Entry::getKey), context.arg(0));
	}
	
	@Override
	public void execute(CommandContext context) throws CommandPartException {
		if(context.hasRemaining(1)) {
			T obj=manager.get(context.arg(0));
			if(obj==null)ICommandPart.error("Bad "+typename+" name", context);
			context.addParameter(name, obj);
			then.execute(context.forward(1));
		}
		else ICommandPart.error("Miss "+typename+" name.", context);
	}
	
	@Override
	public boolean matches(CommandContext context) {
		if(context.hasRemaining(1))return then.matches(context.forward(1));
		else return false;
	}
	
	
	
	

}
