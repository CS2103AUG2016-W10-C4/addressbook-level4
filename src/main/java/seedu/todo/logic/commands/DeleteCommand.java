package seedu.todo.logic.commands;

import com.google.common.collect.ImmutableList;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.arguments.Argument;
import seedu.todo.logic.arguments.IntArgument;
import seedu.todo.logic.arguments.Parameter;
import seedu.todo.model.task.ImmutableTask;

import java.util.List;

public class DeleteCommand extends BaseCommand {
    private static final String VERB = "deleted";
    
    // TODO: Consider either creating a new subclass for indices 
    // or create constraints framework for adding additional validation 
    // to ensure this is always a position, non-zero number
    private Argument<Integer> index = new IntArgument("index").required();

    @Override
    protected Parameter[] getArguments() {
        return new Parameter[]{ index };
    }

    @Override
    public String getCommandName() {
        return "delete";
    }

    @Override
    public List<CommandSummary> getCommandSummary() {
        return ImmutableList.of(new CommandSummary("Delete a task", getCommandName(), 
            getArgumentSummary()));
    }

    @Override
    public CommandResult execute() throws ValidationException {
        ImmutableTask deletedTask = this.model.delete(index.getValue());
        return taskSuccessfulResult(deletedTask.getTitle(), DeleteCommand.VERB);
    }
}
