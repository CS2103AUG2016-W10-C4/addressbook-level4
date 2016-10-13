package seedu.todo.logic.commands;

import seedu.todo.model.ErrorBag;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {
    private final String feedback;
    private final ErrorBag errors; 
    
    public CommandResult(String feedback) {
        this.feedback = feedback;
        this.errors = null;
    }
    
    public CommandResult(String feedback, ErrorBag errors) {
        this.feedback = feedback;
        this.errors = errors;
    }

    public String getFeedback() {
        return feedback;
    }

    public ErrorBag getErrors() {
        return errors;
    }
    
    public boolean isSuccessful() {
        return errors == null;
    }
}
