package seedu.todo.ui.controller;

import javafx.scene.input.KeyCode;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.logic.Logic;
import seedu.todo.logic.commands.CommandResult;
import seedu.todo.model.ErrorBag;
import seedu.todo.ui.view.CommandErrorView;
import seedu.todo.ui.view.CommandFeedbackView;
import seedu.todo.ui.view.CommandInputView;
import seedu.todo.ui.view.CommandPreviewView;

//@@author A0135805H
/**
 * Processes the input command from {@link CommandInputView}, pass it to {@link seedu.todo.logic.Logic}
 * and hands the {@link seedu.todo.logic.commands.CommandResult} to {@link CommandFeedbackView} and
 * {@link CommandErrorView}.
 * Also, {@link CommandPreviewView} is also linked here to listen to user input.
 */
public class CommandController {
    /* Variables */
    private Logic logic;
    private CommandInputView inputView;
    private CommandPreviewView previewView;
    private CommandFeedbackView feedbackView;
    private CommandErrorView errorView;

    /* Private Constructor */
    private CommandController() {}

    /**
     * Constructs a link between the classes defined in the parameters.
     */
    public static CommandController constructLink(Logic logic, CommandInputView inputView,
            CommandPreviewView previewView, CommandFeedbackView feedbackView, CommandErrorView errorView) {

        CommandController controller = new CommandController();
        controller.logic = logic;
        controller.inputView = inputView;
        controller.previewView = previewView;
        controller.feedbackView = feedbackView;
        controller.errorView = errorView;
        controller.start();
        return controller;
    }

    /**
     * Asks {@link #inputView} to start listening for a new key strokes.
     * Once the callback returns a command, {@link #handleInput(KeyCode, String)} will process the input.
     */
    private void start() {
        inputView.listenToInput(this::handleInput);
    }

    //@@author A0139021U
    /**
     * Handles a key stroke from input and sends it to logic. Once logic sends back a preview, it will be
     * processed by {@link #handleCommandResult(CommandResult)}
     * @param keyCode key pressed by user
     * @param userInput text as shown in input view
     */
    private void handleInput(KeyCode keyCode, String userInput) {
        switch (keyCode) {
        case ENTER :    // Submitting command
            if (!StringUtil.isEmpty(userInput)) {
                CommandResult result = logic.execute(userInput);
                handleCommandResult(result);
            }
            break;
        default :   // Typing command, show preview
            logic.preview(userInput);
            errorView.hideCommandErrorView();     // Don't show error when previewing
            break;
        }
    }

    /**
     * Handles a CommandResult object, and updates the user interface to reflect the result.
     * @param result produced by {@link Logic}
     */
    private void handleCommandResult(CommandResult result) {
        previewView.hidePreviewPanel();
        displayMessage(result.getFeedback());
        if (result.isSuccessful()) {
            viewDisplaySuccess();
        } else {
            viewDisplayError(result.getErrors());
        }
    }

    //@@author A0135805H
    /**
     * Displays error in the respective UI elements
     * @param errorBag group of errors to display
     */
    private void viewDisplayError(ErrorBag errorBag) {
        inputView.flagError();
        feedbackView.flagError();
        errorView.displayErrors(errorBag);
    }

    /**
     * Displays success behaviour in the respective UI elements
     */
    private void viewDisplaySuccess() {
        inputView.resetViewState();
        feedbackView.unFlagError();
        errorView.hideCommandErrorView();
    }

    /**
     * Displays a message with regards to the user's input
     * @param message to be displayed to user
     */
    private void displayMessage(String message) {
        feedbackView.displayMessage(message);
    }
}
