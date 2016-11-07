package seedu.todo.ui.view;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.model.property.TaskViewFilter;
import seedu.todo.ui.UiPart;
import seedu.todo.ui.util.UiPartLoaderUtil;
import seedu.todo.ui.util.FxViewUtil;
import seedu.todo.model.task.ImmutableTask;

import java.util.List;
import java.util.logging.Logger;

//@@author A0135805H
/**
 * A panel that holds all the tasks inflated from TaskCardView.
 */
public class TodoListView extends UiPart {
    /*Constants*/
    private static final String FXML = "TodoListView.fxml";
    
    /*Variables*/
    private final Logger logger = LogsCenter.getLogger(TodoListView.class);
    private VBox panel;
    
    /*Layout Declarations*/
    @FXML private ListView<ImmutableTask> todoListView;
    @FXML private VBox emptyListView;
    @FXML private ImageView emptyListImage;
    @FXML private Label emptyListLabel;

    //@@author A0135805H-reused
    /**
     * Default Constructor for {@link TodoListView}
     */
    public TodoListView() {
        super();
    }

    /**
     * Loads and initialise the {@link TodoListView} to the placeHolder.
     *
     * @param primaryStage of the application
     * @param placeHolder where the view element {@link #todoListView} should be placed
     * @param viewProperty the view that the user is at.
     * @return an instance of this class
     */
    public static TodoListView load(Stage primaryStage, AnchorPane placeHolder,
            ObservableList<ImmutableTask> todoList, ObjectProperty<TaskViewFilter> viewProperty) {
        
        TodoListView todoListView = UiPartLoaderUtil.loadUiPart(primaryStage, placeHolder, new TodoListView());
        todoListView.configure(todoList, viewProperty);
        return todoListView;
    }

    //@@author A0135805H
    /**
     * Configures the {@link TodoListView}
     *
     * @param todoList A list of {@link ImmutableTask} to be displayed on this {@link #todoListView}.
     */
    private void configure(ObservableList<ImmutableTask> todoList, ObjectProperty<TaskViewFilter> viewProperty) {
        setTodoListConnections(todoList);
        setEmptyListConnections(todoList, viewProperty);
    }

    /* Empty List View Methods */
    /**
     * Configures the {@link }
     * @param todoList
     */
    private void setEmptyListConnections(ObservableList<ImmutableTask> todoList,
                                         ObjectProperty<TaskViewFilter> viewProperty) {

        ListChangeListener<ImmutableTask> visibilityUpdate
                = c -> setEmptyListViewVisibility(todoList.isEmpty());

        ChangeListener<TaskViewFilter>  viewUpdate
                = (observable, oldValue, newValue) -> setEmptyListContent(newValue);

        todoList.addListener(visibilityUpdate);
        visibilityUpdate.onChanged(null);

        viewProperty.addListener(viewUpdate);
        viewUpdate.changed(null, null, viewProperty.get());
    }

    /**
     * Displays the {@link #emptyListView} if {@code isVisible} is true, hides otherwise.
     */
    private void setEmptyListViewVisibility(boolean isVisible) {
        FxViewUtil.setCollapsed(emptyListView, !isVisible);
    }

    private void setEmptyListContent(TaskViewFilter viewFilter) {
        emptyListLabel.setText(viewFilter.emptyListMessage);
    }

    /* To-do List Ui Methods */
    /**
     * Links the list of {@link ImmutableTask} to the {@link #todoListView}.
     *
     * @param todoList A list of {@link ImmutableTask} to be displayed on this {@link #todoListView}.
     */
    private void setTodoListConnections(ObservableList<ImmutableTask> todoList) {
        todoListView.setItems(todoList);
        todoListView.setCellFactory(param -> new TodoListViewCell());
    }

    /**
     * Toggles the expanded/collapsed view of a task card.
     *
     * @param task The specific to be expanded or collapsed from view.
     */
    public void toggleExpandCollapsed(ImmutableTask task) {
        TaskCardView taskCardView = TaskCardView.getTaskCard(task);
        if (taskCardView != null) {
            taskCardView.toggleCardCollapsing();
        }
    }

    /**
     * Scrolls the {@link #todoListView} to the particular task card at the listIndex.
     */
    public void scrollAndSelect(int listIndex) {
        Platform.runLater(() -> {
            todoListView.scrollTo(listIndex);
            todoListView.getSelectionModel().clearAndSelect(listIndex);
        });
    }

    /**
     * Scrolls the {@link #todoListView} to the particular task card, if the task card is available.
     * However, this method has a bounded wait between 10ms to 1000ms.
     *
     * @param task for the list to scroll to.
     */
    public void scrollAndSelect(ImmutableTask task) {
        scrollAndSelectHelper(task, 100);
    }

    /**
     * Helper method for {@link #scrollAndSelect(ImmutableTask)}.
     * Repeatedly and recursively tries to select for a task for finite number of times.
     */
    private void scrollAndSelectHelper(ImmutableTask task, int attempts) {
        Runnable runnable = () -> {
            TaskCardView taskCardView = TaskCardView.getTaskCard(task);
            if (taskCardView != null) {
                int listIndex = FxViewUtil.convertToListIndex(taskCardView.getDisplayedIndex());
                scrollAndSelect(listIndex);
            } else if (attempts > 0) {
                scrollAndSelectHelper(task, attempts - 1);
            } else {
                logger.warning("Task Card View is null for task: " + task.getTitle());
            }
        };

        //Tries to sleep first to wait for the ui, and then attempts the view.
        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(runnable);
            }
        });

        thread.start();
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

    //@@author A0135805H-reused
    /**
     * Models a Task Card as a single ListCell of the ListView
     */
    private class TodoListViewCell extends ListCell<ImmutableTask> {

        /* Override Methods */
        @Override
        protected void updateItem(ImmutableTask task, boolean empty) {
            super.updateItem(task, empty);

            if (empty || task == null) {
                setGraphic(null);
                setText(null);
            } else {
                TaskCardView taskCardView = TaskCardView.load(task, FxViewUtil.convertToUiIndex(getIndex()));
                setGraphic(taskCardView.getLayout());
                setTaskCardStyleProperties(taskCardView);
            }
        }

        //@@author A0135805H
        /**
         * Sets the style properties of a cell on the to-do list, that cannot be done in any other places.
         */
        private void setTaskCardStyleProperties(TaskCardView taskCardView) {
            this.setPadding(Insets.EMPTY);
            this.selectedProperty().addListener((observable, oldValue, newValue)
                    -> taskCardView.markAsSelected(newValue));
        }
    }
}
