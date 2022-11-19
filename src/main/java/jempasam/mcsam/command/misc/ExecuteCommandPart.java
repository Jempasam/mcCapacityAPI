package jempasam.mcsam.command.misc;

import java.util.Collections;
import java.util.List;

import io.netty.util.internal.shaded.org.jctools.queues.MessagePassingQueue.Consumer;
import jempasam.mcsam.command.CommandContext;
import jempasam.mcsam.command.ICommandPart;

public abstract class ExecuteCommandPart implements ICommandPart{
	
	
	
	@Override
	public List<String> complete(CommandContext context) {
		return Collections.emptyList();
	}
	
	@Override
	public List<String> usage(CommandContext context) {
		return Collections.singletonList("");
	}
	
	@Override
	public boolean matches(CommandContext context) {
		return true;
	}
}
