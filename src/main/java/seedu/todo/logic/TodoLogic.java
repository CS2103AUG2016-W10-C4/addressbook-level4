package seedu.todo.logic;

import seedu.todo.commons.core.EventsCenter;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.events.ui.ShowPreviewEvent;
import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.logic.commands.BaseCommand;
import seedu.todo.logic.commands.CommandPreview;
import seedu.todo.logic.commands.CommandResult;
import seedu.todo.logic.commands.CommandSummary;
import seedu.todo.logic.parser.ParseResult;
import seedu.todo.logic.parser.Parser;
import seedu.todo.model.ErrorBag;
import seedu.todo.model.Model;

import java.util.List;
import java.util.logging.Logger;

//@@author A0135817B
/**
 * Central controller for the application, abstracting application logic from the UI
 */
public class TodoLogic implements Logic {
    private final Parser parser;
    private final Model model;
    private final Dispatcher dispatcher;
    
    private static final Logger logger = LogsCenter.getLogger(TodoLogic.class);
    
    public TodoLogic(Parser parser, Model model, Dispatcher dispatcher) {
        assert parser != null;
        assert model != null;
        assert dispatcher != null;
        
        this.parser = parser;
        this.model = model;
        this.dispatcher = dispatcher;
    }
    
    public CommandResult execute(String input) {
        // Sanity check - input should not be empty 
        if (StringUtil.isEmpty(input)) {
            return new CommandResult();
        }
        
        ParseResult parseResult = parser.parse(input);
        BaseCommand command;
        logger.fine("Parsed command: " + parseResult.toString());
        
        try {
            command = dispatcher.dispatch(parseResult.getCommand());
        } catch (IllegalValueException e) {
            return new CommandResult(e.getMessage(), new ErrorBag());
        }
        
        try {
            command.setArguments(parseResult);
            command.setModel(model);
            return command.execute();
        } catch (ValidationException e) {
            logger.info(e.getMessage());
            return new CommandResult(e.getMessage(), e.getErrors());
        }
    }

    //@@author A0139021U
    @Override
    public void preview(String input) {
        List<CommandSummary> listOfCommands = new CommandPreview(input).getPreview();
        EventsCenter.getInstance().post(new ShowPreviewEvent(listOfCommands));
    }
}
