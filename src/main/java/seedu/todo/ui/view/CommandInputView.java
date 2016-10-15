package seedu.todo.ui.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.util.FxViewUtil;
import seedu.todo.commons.util.TextAreaResizerUtil;
import seedu.todo.ui.UiPart;
import seedu.todo.ui.UiPartLoader;

import java.util.logging.Logger;

//@@author A0315805H
/**
 * A view class that handles the Input text box directly.
 */
public class CommandInputView extends UiPart {
    private final Logger logger = LogsCenter.getLogger(CommandInputView.class);
    private static final String FXML = "CommandInputView.fxml";
    private static final String ERROR_STYLE = "error";

    private AnchorPane placeHolder;
    private AnchorPane commandInputPane;

    @FXML
    private TextArea commandTextField;

    /**
     * Loads and initialise the input view element to the placeHolder
     * @param primaryStage of the application
     * @param placeHolder where the view element {@link #commandInputPane} should be placed
     * @return an instance of this class
     */
    public static CommandInputView load(Stage primaryStage, AnchorPane placeHolder) {
        CommandInputView commandInputView = UiPartLoader.loadUiPart(primaryStage, placeHolder, new CommandInputView());
        commandInputView.addToPlaceholder();
        commandInputView.configureLayout();
        commandInputView.configureProperties();
        return commandInputView;
    }

    /**
     * Adds this view element to external placeholder
     */
    private void addToPlaceholder() {
        placeHolder.getChildren().add(commandInputPane);
    }

    /**
     * Configure the UI layout of {@link CommandInputView}
     */
    private void configureLayout() {
        FxViewUtil.applyAnchorBoundaryParameters(commandInputPane, 0.0, 0.0, 0.0, 0.0);
        FxViewUtil.applyAnchorBoundaryParameters(commandTextField, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Configure the UI properties of {@link CommandInputView}
     */
    private void configureProperties() {
        setCommandInputHeightAutoResizeable();
        unflagErrorWhileTyping();
    }

    /**
     * Sets {@link #commandTextField} to listen out for a command.
     * Once a command is received, calls {@link CommandCallback} interface to process this command.
     */
    public void listenToCommandExecution(CommandCallback listener) {
        this.commandTextField.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String command = commandTextField.getText();
                listener.onCommandReceived(command);
                event.consume(); //To prevent commandTextField from printing a new line.
            }
        });
    }

    /* UI Methods */
    /**
     * Resets the text box by {@link #unflagError()} after a key is pressed
     */
    private void unflagErrorWhileTyping() {
        this.commandTextField.addEventFilter(KeyEvent.KEY_PRESSED, event -> unflagError());
    }

    /**
     * Allow {@link #commandTextField} to adjust automatically with the height of the content of the text area itself.
     */
    private void setCommandInputHeightAutoResizeable() {
        new TextAreaResizerUtil().setResizable(commandTextField);
    }

    /**
     * Resets the state of the text box, by clearing the text box and clear the errors.
     */
    public void resetViewState() {
        unflagError();
        clear();
    }

    /**
     * Indicate an error visually on the {@link #commandTextField}
     */
    public void flagError() {
        FxViewUtil.addClassStyle(commandTextField, ERROR_STYLE);
    }

    /**
     * Remove the error flag visually on the {@link #commandTextField}
     */
    private void unflagError() {
        FxViewUtil.removeClassStyle(commandTextField, ERROR_STYLE);
    }

    /**
     * Clears the texts inside the text box
     */
    private void clear() {
        commandTextField.clear();
    }

    /* Override Methods */
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
        this.placeHolder = pane;
    }

    /*Interface Declarations*/
    /**
     * Defines an interface for controller class to receive a command from this view class, and process it.
     */
    public interface CommandCallback {
        void onCommandReceived(String command);
    }
}
