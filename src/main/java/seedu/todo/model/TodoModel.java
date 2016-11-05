package seedu.todo.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import seedu.todo.commons.core.Config;
import seedu.todo.model.property.TaskViewFilter;
import seedu.todo.commons.core.UnmodifiableObservableList;
import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.tag.UniqueTagCollection;
import seedu.todo.model.tag.UniqueTagCollectionModel;
import seedu.todo.model.property.SearchStatus;
import seedu.todo.model.tag.UniqueTagCollectionValidator;
import seedu.todo.model.task.ImmutableTask;
import seedu.todo.model.task.MutableTask;
import seedu.todo.model.task.Task;
import seedu.todo.storage.MovableStorage;
import seedu.todo.storage.TodoListStorage;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

//@@author A0135817B
/**
 * Represents the data layer of the application. The TodoModel handles any 
 * interaction with the application state that are not persisted, such as the
 * view (sort and filtering), undo and redo. Since this layer handles 
 * sorting and filtering, task ID must be passed through {@link #getTaskIndex}
 * to transform them into the index {@link TodoList} methods can use. 
 */
public class TodoModel implements Model {
    // Constants 
    private static final int UNDO_LIMIT = 10;
    private static final String INDEX_OUT_OF_BOUND_FORMAT = "There is no task no. %d";
    private static final String NO_MORE_UNDO_REDO_FORMAT = "There are no more steps to %s";
    
    // Dependencies
    private TodoListModel todoList;
    private UniqueTagCollectionModel uniqueTagCollection;
    private MovableStorage<ImmutableTodoList> storage;
    
    // Stack of transformation that the tasks go through before being displayed to the user
    private ObservableList<ImmutableTask> tasks;
    private FilteredList<ImmutableTask> viewFilteredTasks;
    private FilteredList<ImmutableTask> findFilteredTasks;
    private SortedList<ImmutableTask> sortedTasks;
    
    // State stacks for managing un/redo
    private Deque<List<ImmutableTask>> undoStack = new ArrayDeque<>();
    private Deque<List<ImmutableTask>> redoStack = new ArrayDeque<>();

    /**
     * Contains the current view tab the user has selected. 
     * {@link #getViewFilter()} is the getter and {@link #view(TaskViewFilter)} is the setter
     */
    private ObjectProperty<TaskViewFilter> view = new SimpleObjectProperty<>();
    
    private ObjectProperty<SearchStatus> search = new SimpleObjectProperty<>();

    public TodoModel(Config config) {
        this(new TodoListStorage(config.getTodoListFilePath()));
    }
    
    public TodoModel(MovableStorage<ImmutableTodoList> storage) {
        this(new TodoList(storage), storage);
    }
    
    public TodoModel(TodoListModel todoList, MovableStorage<ImmutableTodoList> storage) {
        this.storage = storage;
        this.todoList = todoList;

        tasks = todoList.getObservableList();
        viewFilteredTasks = new FilteredList<>(tasks);
        findFilteredTasks = new FilteredList<>(viewFilteredTasks);
        sortedTasks = new SortedList<>(findFilteredTasks);

        uniqueTagCollection = new UniqueTagCollection(todoList.getTasks());
        
        // Sets the default view 
        view(TaskViewFilter.DEFAULT);
    }

    /**
     * Because the model does filtering and sorting on the tasks, the incoming index needs to be 
     * translated into it's index in the underlying todoList. The code below is not particularly
     * clean, but it works well enough. 
     * 
     * @throws ValidationException if the index is invalid
     */
    private int getTaskIndex(int index) throws ValidationException {
        int taskIndex;
        try {
            ImmutableTask task = getObservableList().get(index - 1);
            taskIndex = tasks.indexOf(task);
        } catch (IndexOutOfBoundsException e) {
            taskIndex = -1;
        }
        if (taskIndex == -1) {
            String message = String.format(TodoModel.INDEX_OUT_OF_BOUND_FORMAT, index);
            throw new ValidationException(message);
        }

        return taskIndex;
    }
    
    private void saveState(Deque<List<ImmutableTask>> stack) {
        List<ImmutableTask> tasks = todoList.getTasks().stream()
            .map(Task::new).collect(Collectors.toList());
        
        stack.addFirst(tasks);
        while (stack.size() > TodoModel.UNDO_LIMIT) {
            stack.removeLast();
        }
    }

    private void saveUndoState() {
        saveState(undoStack);
        redoStack.clear();
    }
    
    @Override
    public ImmutableTask add(String title) throws IllegalValueException {
        saveUndoState();
        return todoList.add(title);
    }

    @Override
    public ImmutableTask add(String title, Consumer<MutableTask> update) throws ValidationException {
        saveUndoState();
        return todoList.add(title, update);
    }
    //@@author A0092382A
    @Override
    public ImmutableTask delete(int index) throws ValidationException {
        saveUndoState();
        int taskIndex = getTaskIndex(index);
        ImmutableTask taskToDelete = tasks.get(taskIndex);
        ImmutableTask taskDeleted = todoList.delete(taskIndex);
        //Notification only sent if delete is valid
        uniqueTagCollection.notifyTaskDeleted(taskToDelete);
        return taskDeleted;
    }
    
    @Override
    public List<ImmutableTask> deleteAll() throws ValidationException{
        saveUndoState();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 1; i <= getObservableList().size(); i++) {
            ImmutableTask task = getObservableList().get(i-1);
            if (task.isCompleted()){
                indexes.add(getTaskIndex(i));
            }
        }
        //Notification only sent if deletions are valid
        List<ImmutableTask> deletedTasks = todoList.delete(indexes);
        for (ImmutableTask task : deletedTasks) {
            uniqueTagCollection.notifyTaskDeleted(task);
        }
        return deletedTasks;

    }

    @Override
    public ImmutableTask update(int index, Consumer<MutableTask> update) throws ValidationException {
        saveUndoState();
        int taskIndex = getTaskIndex(index);
        return todoList.update(taskIndex, update);
    }

    @Override
    public List<ImmutableTask> updateAll(Consumer<MutableTask> update) throws ValidationException {
        saveUndoState();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 1; i <= getObservableList().size(); i++) {
            indexes.add(getTaskIndex(i));
        }
        return todoList.update(indexes, update);
    }

    //@@author A0135817B
    @Override
    public void view(TaskViewFilter view) {
        viewFilteredTasks.setPredicate(view.filter);

        sortedTasks.setComparator((a, b) -> {
            int pin = Boolean.compare(b.isPinned(), a.isPinned());
            return pin != 0 || view.sort == null ? pin : view.sort.compare(a, b);
        });
        
        this.view.setValue(view);
    }
    
    @Override
    public void find(Predicate<ImmutableTask> predicate) {
        findFilteredTasks.setPredicate(predicate);
        search.setValue(null);
    }

    @Override
    public void find(Predicate<ImmutableTask> predicate, List<String> terms) {
        findFilteredTasks.setPredicate(predicate);
        search.setValue(new SearchStatus(terms, findFilteredTasks.size(), tasks.size()));
    }

    @Override
    public void undo() throws ValidationException {
        if (undoStack.isEmpty()) {
            String message = String.format(TodoModel.NO_MORE_UNDO_REDO_FORMAT, "undo");
            throw new ValidationException(message);
        }
        
        List<ImmutableTask> tasks = undoStack.removeFirst();
        uniqueTagCollection.update(tasks);
        saveState(redoStack);
        todoList.setTasks(tasks);
    }

    @Override
    public void redo() throws ValidationException {
        if (redoStack.isEmpty()) {
            String message = String.format(TodoModel.NO_MORE_UNDO_REDO_FORMAT, "redo");
            throw new ValidationException(message);
        }

        List<ImmutableTask> tasks = redoStack.removeFirst();
        uniqueTagCollection.update(tasks);
        saveState(undoStack);
        todoList.setTasks(tasks);
    }

    @Override
    public void save(String location) throws ValidationException {
        todoList.save(location);
    }

    @Override
    public void load(String location) throws ValidationException {
        todoList.load(location);
        uniqueTagCollection.update(tasks);
    }

    @Override
    public String getStorageLocation() {
        return storage.getLocation();
    }

    @Override
    public UnmodifiableObservableList<ImmutableTask> getObservableList() {
        return new UnmodifiableObservableList<>(sortedTasks);
    }

    @Override
    public ObjectProperty<TaskViewFilter> getViewFilter() {
        return view;
    }

    @Override
    public ObjectProperty<SearchStatus> getSearchStatus() {
        return search;
    }

    //@@author A0135805H
    @Override
    public List<Tag> getGlobalTagsList() {
        return uniqueTagCollection.getUniqueTagList();
    }

    @Override
    public void addTagsToTask(int index, String... tagNames) throws ValidationException {
        saveUndoState();
        ImmutableTask task = getTask(index);
        addTagsToTaskHelper(index, task, tagNames);
    }

    @Override
    public void addTagsToTask(MutableTask task, String... tagNames) {
        saveUndoState();
        //Do not perform validation (disallowed by Consumer interface). Perform actual tag adding.
        addTagsToTaskHelper(task, tagNames);
    }

    @Override
    public void deleteTagsFromTask(int index, String... tagNames) throws ValidationException {
        saveUndoState();
        ImmutableTask task = getTask(index);
        deleteTagsFromTaskHelper(task, index, tagNames);
    }

    @Override
    public void deleteTags(String... tagNames) throws ValidationException {
        saveUndoState();
        deleteTagsHelper(tagNames);
    }

    @Override
    public void renameTag(String oldName, String newName) throws ValidationException {
        saveUndoState();
        renameTagHelper(oldName, newName);
    }

    /**
     * Gets the {@link ImmutableTask} object at the respective displayed index.
     * @throws ValidationException when the index is invalid.
     */
    @Override
    public ImmutableTask getTask(int displayedIndex) throws ValidationException {
        int index = getTaskIndex(displayedIndex);
        return tasks.get(index);
    }

    /* Helper Method */
    /**
     * Helper method that validate and handles tag adding operation
     */
    private void addTagsToTaskHelper(int index, ImmutableTask task, String... tagNames)
            throws ValidationException {

        UniqueTagCollectionValidator
                .validate("add tags", validator -> validator.validateAddTags(task, tagNames));

        update(index, mutableTask -> {
            assert mutableTask.equals(task); //Just a sanity check.
            addTagsToTask(mutableTask, tagNames);
        });
    }

    /**
     * Helper method that handles tag adding operation without validation
     */
    private void addTagsToTaskHelper(MutableTask task, String... tagNames) {
        Set<Tag> newTags = new HashSet<>(uniqueTagCollection.associateTaskToTags(task, tagNames));
        newTags.addAll(task.getTags());
        task.setTags(newTags);
    }

    /**
     * Helper method that validates and handles tag deletion from task operation
     */
    private void deleteTagsFromTaskHelper(ImmutableTask task, int index, String... tagNames) throws ValidationException {
        UniqueTagCollectionValidator
                .validate("delete tags", validator -> validator.validateDeleteTags(task, tagNames));

        update(index, mutableTask -> {
            Set<Tag> tagsFromTask = new HashSet<>(mutableTask.getTags());
            Collection<Tag> deletedTags = uniqueTagCollection.dissociateTaskFromTags(mutableTask, tagNames);
            tagsFromTask.removeAll(deletedTags);
            mutableTask.setTags(tagsFromTask);
        });
    }

    /**
     * Helper method that validates and handles tag deletion operation
     */
    private void deleteTagsHelper(String... tagNames) throws ValidationException {
        UniqueTagCollectionValidator.validate("delete tags",
                validator -> validator.validateDeleteTags(uniqueTagCollection, tagNames));

        Collection<Tag> deletedTags = uniqueTagCollection.deleteTags(tagNames);
        todoList.updateAll(mutableTask -> {
            Set<Tag> tagsFromTask = new HashSet<>(mutableTask.getTags());
            tagsFromTask.removeAll(deletedTags);
            mutableTask.setTags(tagsFromTask);
        });
    }

    /**
     * Helper method that validates and handles tag renaming operation
     */
    private void renameTagHelper(String oldName, String newName) throws ValidationException {
        UniqueTagCollectionValidator.validate("rename tag",
                validator -> validator.validateRenameCommand(uniqueTagCollection, oldName, newName));

        Set<ImmutableTask> tasksWithTag = uniqueTagCollection.getTasksLinkedToTag(oldName);
        deleteTagsHelper(oldName);

        todoList.updateAll(mutableTask -> {
            if (tasksWithTag.contains(mutableTask)) {
                addTagsToTask(mutableTask, newName);
            }
        });
    }
}
