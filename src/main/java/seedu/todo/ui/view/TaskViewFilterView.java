package seedu.todo.ui.view;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.ocpsoft.prettytime.shade.org.apache.commons.lang.WordUtils;
import seedu.todo.commons.core.LogsCenter;
import seedu.todo.commons.enumerations.TaskViewFilter;
import seedu.todo.commons.util.StringUtil;
import seedu.todo.ui.UiPart;
import seedu.todo.ui.UiPartLoader;
import seedu.todo.ui.util.FxViewUtil;
import seedu.todo.ui.util.ViewStyleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Shows a row of filter categories via {@link seedu.todo.commons.enumerations.TaskViewFilter}
 * to filter the tasks in {@link seedu.todo.ui.TodoListPanel}
 */
public class TaskViewFilterView extends UiPart {
    /* Constants */
    private final Logger logger = LogsCenter.getLogger(TaskViewFilterView.class);
    private static final String FXML = "TaskViewFilterView.fxml";

    /* Layout Views */
    private AnchorPane placeholder;
    private FlowPane filterViewPane;

    /* Variables */
    private Map<TaskViewFilter, HBox> taskFilterBoxesMap = new HashMap<>();

    /* Layout Initialisation */
    /**
     * Loads and initialise the {@link #filterViewPane} to the {@link seedu.todo.ui.MainWindow}
     * @param primaryStage of the application
     * @param placeholder where the view element {@link #filterViewPane} should be placed
     * @return an instance of this class
     */
    public static TaskViewFilterView load(Stage primaryStage, AnchorPane placeholder) {
        TaskViewFilterView filterView = UiPartLoader.loadUiPart(primaryStage, placeholder, new TaskViewFilterView());
        filterView.addToPlaceholder();
        filterView.configureLayout();
        filterView.configureProperties();
        return filterView;
    }

    /**
     * Adds this view element to external placeholder
     */
    private void addToPlaceholder() {
        placeholder.getChildren().add(filterViewPane);
    }

    /**
     * Configure the UI layout of {@link TaskViewFilterView}
     */
    private void configureLayout() {
        FxViewUtil.applyAnchorBoundaryParameters(filterViewPane, 0.0, 0.0, 0.0, 0.0);
    }

    /**
     * Initialise and configure the UI properties of {@link TaskViewFilterView}
     */
    private void configureProperties() {
        initialiseAllViewFilters();
        selectOneViewFilter(TaskViewFilter.DEFAULT);
    }

    /**
     * Display all the {@link TaskViewFilter} on the {@link #filterViewPane}
     */
    private void initialiseAllViewFilters() {
        for (TaskViewFilter filter : TaskViewFilter.values()) {
            appendEachViewFilter(filter);
        }
    }

    /**
     * Add one {@link TaskViewFilter} on the {@link #filterViewPane}
     * and save an instance to the {@link #taskFilterBoxesMap}
     * @param filter to add onto the pane
     */
    private void appendEachViewFilter(TaskViewFilter filter) {
        HBox textContainer = constructViewFilterBox(filter);
        taskFilterBoxesMap.put(filter, textContainer);
        filterViewPane.getChildren().add(textContainer);
    }

    /**
     * Given a filter, construct a view element to be displayed on the {@link #filterViewPane}
     * @param filter to be displayed
     * @return a view element
     */
    private HBox constructViewFilterBox(TaskViewFilter filter) {
        String filterName = WordUtils.capitalize(filter.getViewName());
        String[] partitionedText = StringUtil.partitionStringAtPosition(filterName, filter.getShortcutCharPosition());

        Label leftText = new Label(partitionedText[0]);
        Label centreText = new Label(partitionedText[1]);
        Label rightText = new Label(partitionedText[2]);
        ViewStyleUtil.addClassStyles(centreText, ViewStyleUtil.STYLE_UNDERLINE);

        HBox textContainer = new HBox();
        textContainer.getChildren().add(leftText);
        textContainer.getChildren().add(centreText);
        textContainer.getChildren().add(rightText);
        return textContainer;
    }

    /* Methods interfacing with UiManager */
    /**
     * Select exactly one filter from {@link #filterViewPane}
     */
    public void selectOneViewFilter(TaskViewFilter filter) {
        clearAllViewFiltersSelection();
        selectViewFilter(filter);
    }

    /* Helper Methods */
    /**
     * Clears all selection from the {@link #filterViewPane}
     */
    private void clearAllViewFiltersSelection() {
        for (HBox filterBox : taskFilterBoxesMap.values()) {
            ViewStyleUtil.removeClassStyles(filterBox, ViewStyleUtil.STYLE_SELECTED);
        }
    }

    /**
     * Mark the filter as selected on {@link #filterViewPane}
     * However, if filter is null, nothing is done.
     */
    private void selectViewFilter(TaskViewFilter filter) {
        if (filter != null) {
            HBox filterBox = taskFilterBoxesMap.get(filter);
            ViewStyleUtil.addClassStyles(filterBox, ViewStyleUtil.STYLE_SELECTED);
        }
    }

    /* Override Methods */
    @Override
    public void setPlaceholder(AnchorPane placeholder) {
        this.placeholder = placeholder;
    }

    @Override
    public void setNode(Node node) {
        this.filterViewPane = (FlowPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }
}
