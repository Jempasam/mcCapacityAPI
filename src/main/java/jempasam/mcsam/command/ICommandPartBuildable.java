package jempasam.mcsam.command;

import jempasam.mcsam.command.argument.EntityArgumentCommandPart;
import jempasam.mcsam.command.argument.FloatArgumentCommandPart;
import jempasam.mcsam.command.argument.IntArgumentCommandPart;
import jempasam.mcsam.command.argument.StringArgumentCommandPart;
import jempasam.mcsam.command.misc.ExecuteCommandPart;

public interface ICommandPartBuildable extends ICommandPart{
	
	void thenFinally(ICommandPart commandpart);
	
	default ICommandPartBuildable then(ICommandPartBuildable commandpart) {
		thenFinally(commandpart);
		return commandpart;
	}
	
	default void thenDo(ICommandAction action) {
		thenFinally(new ExecuteCommandPart() {
			@Override public void execute(CommandContext context) throws CommandPartException { action.execute(context); }
		});
	}
	
	default ICommandPartBuildable thenInt(String name) { return then(new IntArgumentCommandPart(name)); }
	default ICommandPartBuildable thenFloat(String name) { return then(new FloatArgumentCommandPart(name)); }
	default ICommandPartBuildable thenString(String name) { return then(new StringArgumentCommandPart(name)); }
	default ICommandPartBuildable thenEntity(String name) { return then(new EntityArgumentCommandPart(name)); }
}
