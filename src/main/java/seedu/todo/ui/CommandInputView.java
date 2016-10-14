package seedu.todo.ui;

import com.google.common.eventbus.Subscribe;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.commons.util.TextAreaResizerUtil;
import seedu.todo.logic.Logic;
import seedu.todo.logic.commands.CommandResult;

import java.util.logging.Logger;

public class CommandInputView extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandInputView.class);
    private static final String FXML = "CommandInputView.fxml";

    private AnchorPane placeHolderPane;
    private AnchorPane commandInputPane;
    private CommandFeedbackView commandFeedbackView;
    String previousCommandText;

    private Logic logic;

    @FXML
    private TextArea commandTextField;

    public static CommandInputView load(Stage primaryStage, AnchorPane commandBoxPlaceholder,
                                        CommandFeedbackView commandFeedbackView, Logic logic) {
        
        CommandInputView commandInputView = UiPartLoader.loadUiPart(primaryStage, commandBoxPlaceholder, new CommandInputView());
        commandInputView.configure(commandFeedbackView, logic);
        commandInputView.addToPlaceholder();
        return commandInputView;
    }

    public void configure(CommandFeedbackView commandFeedbackView, Logic logic) {
        this.commandFeedbackView = commandFeedbackView;
        this.logic = logic;
        setCommandInputHeightAutoResizeable();
        listenToCommandInput();
    }

    private void addToPlaceholder() {
        SplitPane.setResizableWithParent(placeHolderPane, false);
        placeHolderPane.getChildren().add(commandTextField);
        FxViewUtil.applyAnchorBoundaryParameters(commandInputPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    @Override
    public void setNode(Node node) {
        commandInputPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    @Override
    public void setPlaceholder(AnchorPane pane) {
        this.placeHolderPane = pane;
    }

    /**
     * Allow {@link #commandTextField} to adjust automatically with the height of the content of the text area itself.
     */
    private void setCommandInputHeightAutoResizeable() {
        new TextAreaResizerUtil().setResizable(commandTextField);
    }

    /**
     * Sets {@link #commandTextField} to listen out for a command.
     * Once a command is received, calls {@link #submitCommandInput(String)} to submit that command.
     */
    private void listenToCommandInput() {
        this.commandTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                submitCommandInput(commandTextField.getText());
                event.consume(); //To prevent commandTextField from printing a new line.
            }
        });
    }

    /**
     * Submits a command to logic, and handle the result of CommandResult with
     * {@link #handleCommandResult(CommandResult)}
     * @param commandText command submitted by user via {@link #commandTextField}
     */
    private void submitCommandInput(String commandText) {
        //Do not execute an empty command. TODO: This check should be done in the parser class.
        if (!StringUtil.isEmpty(commandText)) {
            commandTextField.setDisable(true);
            CommandResult result = logic.execute(previousCommandText);
            handleCommandResult(result);
        }
    }

    /**
     * Resets the {@link #commandTextField} to default state
     */
    private void resetCommandTextField() {
        commandTextField.getStyleClass().remove("error");
        commandTextField.clear();
    }

    /**
     * Restores the command box text to the previously entered command
     */
    private void restoreCommandText() {
        commandTextField.setText(previousCommandText);
    }

    /**
     * Sets the command box style to indicate an error
     */
    private void setStyleToIndicateIncorrectCommand() {
        commandTextField.getStyleClass().add("error");
    }

    @Subscribe
    private void handleIncorrectCommandAttempted(IncorrectCommandAttemptedEvent event){
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Invalid command: " + previousCommandText));
        setStyleToIndicateIncorrectCommand();
        restoreCommandText();
    }

}
