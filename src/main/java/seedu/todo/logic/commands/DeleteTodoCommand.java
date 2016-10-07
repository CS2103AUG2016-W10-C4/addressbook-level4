package seedu.todo.logic.commands;

import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.logic.arguments.Argument;
import seedu.todo.logic.arguments.IntArgument;
import seedu.todo.logic.arguments.Parameter;
import seedu.todo.model.task.ImmutableTask;

public class DeleteTodoCommand extends BaseCommand {
    // TODO: Consider either creating a new subclass for indices 
    // or create constraints framework for adding additional validation 
    // to ensure this is always a position, non-zero number
    Argument<Integer> index = new IntArgument("index").required();

    @Override
    protected Parameter[] getArguments() {
        return new Parameter[]{ index };
    }

    @Override
    public void execute() throws IllegalValueException {
        ImmutableTask toDelete = this.getTaskAt(index.getValue());
        this.model.delete(toDelete);
    }
}
