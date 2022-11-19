package jempasam.mcsam.command;

public interface ICommandAction {
	void execute(CommandContext context) throws CommandPartException;
}
