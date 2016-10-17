package seedu.todo.ui;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.model.task.ImmutableTask;

import java.util.logging.Logger;

/**
 * A panel that holds all the tasks inflated from TaskCard.
 */
public class TodoListPanel extends UiPart {
    /*Constants*/
    private static final String FXML = "TodoListPanel.fxml";
    
    /*Variables*/
    private final Logger logger = LogsCenter.getLogger(TodoListPanel.class);
    private VBox panel;
    private AnchorPane placeHolderPane;
    
    /*Layout Declarations*/
    @FXML
    private ListView<ImmutableTask> todoListView;

    /**
     * Default Constructor for {@link TodoListPanel}
     */
    public TodoListPanel() {
        super();
    }

    /**
     * Loads and initialise the {@link TodoListPanel} to the placeHolder
     * @param primaryStage of the application
     * @param placeHolder where the view element {@link #todoListView} should be placed
     * @return an instance of this class
     */
    public static TodoListPanel load(Stage primaryStage, AnchorPane placeHolder,
            ObservableList<ImmutableTask> todoList) {
        
        TodoListPanel todoListPanel = 
                UiPartLoader.loadUiPart(primaryStage, placeHolder, new TodoListPanel());
        todoListPanel.configure(todoList);
        return todoListPanel;
    }

    /**
     * Configures the {@link TodoListPanel}
     * @param todoList a list of {@link ImmutableTask} to be displayed on this {@link #todoListView}.
     */
    private void configure(ObservableList<ImmutableTask> todoList) {
        setConnections(todoList);
        addToPlaceholder();
    }

    /**
     * Adds this view element to external placeholder
     */
    private void addToPlaceholder() {
        placeHolderPane.getChildren().add(panel);
    }

    /**
     * Links the list of {@link ImmutableTask} to the todoListView.
     * @param todoList a list of {@link ImmutableTask} to be displayed on this {@link #todoListView}.
     */
    private void setConnections(ObservableList<ImmutableTask> todoList) {
        todoListView.setItems(todoList);
        todoListView.setCellFactory(listView -> new TodoListViewCell());
    }

    /* Ui Methods */
    /**
     * Toggles the expanded/collapsed view of a task card.
     * @param index of a task card that is shown on the Ui (that means index accepts a range of 1 to num of tasks).
     */
    public void toggleExpandCollapsed(int index) {

    }

    public void scrollTo(int index) {
        Platform.runLater(() -> {
            todoListView.scrollTo(index);
            todoListView.getSelectionModel().clearAndSelect(index);
        });
    }

    /* Override Methods */
    @Override
    public void setNode(Node node) {
        panel = (VBox) node;
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
     * Models a Task Card as a single ListCell of the ListView
     */
    private class TodoListViewCell extends ListCell<ImmutableTask> {

        public TodoListViewCell() {
        }

        @Override
        protected void updateItem(ImmutableTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                TaskCard taskCard = TaskCard.load(task, getIndex() + 1);
                setGraphic(taskCard.getLayout());
                setTaskCardStyleProperties(taskCard);
            }
        }

        /**
         * Sets the style properties of a cell on the to-do list, that cannot be done in any other places.
         */
        private void setTaskCardStyleProperties(TaskCard taskCard) {
            this.setPadding(Insets.EMPTY);
            this.selectedProperty().addListener((observable, oldValue, newValue) -> taskCard.markAsSelected(newValue));
        }
    }

}
