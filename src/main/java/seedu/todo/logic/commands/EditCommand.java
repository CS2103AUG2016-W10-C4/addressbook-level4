package seedu.todo.logic.commands;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.logic.arguments.*;
import seedu.todo.model.task.ImmutableTask;

import java.util.Iterator;
import java.util.List;

public class EditCommand extends BaseCommand {
    private static final String VERB = "edited";
    
    // These parameters will be sorted out manually
    private Argument<Integer> index = new IntArgument("index").required();
    private Argument<String> title = new StringArgument("title");
    
    private Argument<String> description = new StringArgument("description")
            .flag("m");
    
    private Argument<Boolean> pin = new FlagArgument("pin")
            .flag("p");
    
    private Argument<String> location = new StringArgument("location")
            .flag("l");

    private Argument<DateRange> date = new DateRangeArgument("date")
            .flag("d");

    @Override
    protected Parameter[] getArguments() {
        return new Parameter[] { index, title, date, description, pin, location };
    }

    @Override
    public String getCommandName() {
        return "edit";
    }

    @Override
    public List<CommandSummary> getCommandSummary() {
        return ImmutableList.of(new CommandSummary("Edit task", getCommandName(), 
            getArgumentSummary()));

    }

    @Override
    protected void setPositionalArgument(String argument) {
        String[] tokens = argument.trim().split("\\s+", 2);

        try {
            index.setValue(tokens[0].trim());
        } catch (IllegalValueException e) {
            errors.put(index.getName(), e.getMessage());
        }
        
        if (tokens.length > 1) {
            try {
                title.setValue(tokens[1].trim());
            } catch (IllegalValueException e) {
                errors.put(index.getName(), e.getMessage());
            }
        }
    }

    @Override
    public CommandResult execute() throws ValidationException {
        ImmutableTask editedTask = this.model.update(index.getValue(), task -> {
            if (title.hasBoundValue()) {
                task.setTitle(title.getValue());
            }
            
            if (description.hasBoundValue()) {
                task.setDescription(description.getValue());
            }
            
            if (pin.hasBoundValue()) {
                task.setPinned(pin.getValue());
            }
            
            if (location.hasBoundValue()) {
                task.setLocation(location.getValue());
            }
            
            if (date.hasBoundValue()) {
                task.setStartTime(date.getValue().getStartTime());
                task.setEndTime(date.getValue().getEndTime());
            }
        });
        
        return taskSuccessfulResult(editedTask.getTitle(), EditCommand.VERB);
    }

}
