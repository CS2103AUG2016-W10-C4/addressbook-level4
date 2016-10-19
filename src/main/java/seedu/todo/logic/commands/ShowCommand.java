package seedu.todo.logic.commands;

import java.util.List;

import com.google.common.collect.ImmutableList;

import javafx.collections.ObservableList;
import seedu.todo.commons.core.EventsCenter;
import seedu.todo.commons.events.ui.ExpandCollapseTaskEvent;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.arguments.Argument;
import seedu.todo.logic.arguments.IntArgument;
import seedu.todo.logic.arguments.Parameter;
import seedu.todo.model.task.ImmutableTask;

public class ShowCommand extends BaseCommand {
    private Argument<Integer> index = new IntArgument("index").required();
    private static final Integer INDEX_OFFSET = 1; 

    @Override
    protected Parameter[] getArguments() {
        return new Parameter[] { index };
    }

    @Override
    public String getCommandName() {
        return "show";
    }

    @Override
    public List<CommandSummary> getCommandSummary() {
        return ImmutableList.of(new CommandSummary("Shows details of a task", getCommandName(), getArgumentSummary()));
    }

    @Override
    public CommandResult execute() throws ValidationException {
        ImmutableTask task = this.model.getObservableList().get(index.getValue() - INDEX_OFFSET);
        EventsCenter.getInstance().post(new ExpandCollapseTaskEvent(task)); 
        return new CommandResult("Command executed");
    }

}