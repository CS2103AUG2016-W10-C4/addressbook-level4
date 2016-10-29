package seedu.todo.model;

import javafx.beans.property.ObjectProperty;
import seedu.todo.model.property.TaskViewFilter;
import seedu.todo.commons.core.UnmodifiableObservableList;
import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.model.property.SearchStatus;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.task.ImmutableTask;
import seedu.todo.model.task.MutableTask;
import seedu.todo.model.task.Task;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

//@@author A0135817B
public interface Model {
    /**
     * Adds a new task or event with title only to the todo list.
     *
     * @param title  the title of the task
     * @return the task that was just created 
     * @throws IllegalValueException if the values set in the update predicate is invalid
     */
    ImmutableTask add(String title) throws IllegalValueException;

    /**
     * Adds a new task or event with title and other fields to the todo list.
     *
     * @param title   the title of the task 
     * @param update  a {@link MutableTask} is passed into this lambda. All other fields 
     *                should be set from inside this lambda. 
     * @return the task that was just created
     * @throws ValidationException   if the fields in the task to be updated are not valid
     */
    ImmutableTask add(String title, Consumer<MutableTask> update) throws ValidationException;

    /**
     * Deletes the given task from the todo list. This change is also propagated to the 
     * underlying persistence layer.  
     *
     * @param index  the 1-indexed position of the task that needs to be deleted
     * @return the task that was just deleted
     * @throws ValidationException if the task does not exist
     */
    ImmutableTask delete(int index) throws ValidationException;

    /**
     * Replaces certain fields in the task. Mutation of the {@link Task} object should 
     * only be done in the <code>update</code> lambda. The lambda takes in one parameter, 
     * a {@link MutableTask}, and does not expect any return value. For example: 
     *
     * <pre><code>todo.update(task, t -> {
     *     t.setEndTime(t.getEndTime.get().plusHours(2)); // Push deadline back by 2h
     *     t.setPin(true); // Pin this task
     * });</code></pre>
     *
     * @return the task that was just updated
     *
     * @throws ValidationException    if the task does not exist or if the fields in the 
     *                                task to be updated are not valid
     */
    ImmutableTask update(int index, Consumer<MutableTask> update) throws ValidationException;
    
    /**
     * Carries out the specified update in the fields of all visible tasks. Mutation of all {@link Task}
     * objects should only be done in the <code>update</code> lambda. The lambda takes in a single parameter,
     * a {@link MutableTask}, and does not expect any return value, as per the {@link update} command. Since
     * this represents the observable layer, the changes required to be done to the underlying layer TodoList
     * is set via getting a list of their indices in the underlying layer using a UUID map.  
     * 
     * <pre><code>todo.updateAll (t -> {
     *     t.setEndTime(t.getEndTime.get().plusHours(2)); // Push deadline of all Observable tasks back by 2h
     *     t.setPin(true); // Pin all tasks in Observable view
     * });</code></pre>
     * 
     * @throws ValidationException if any updates on any of the task objects are considered invalid
     */
    void updateAll(Consumer <MutableTask> update) throws ValidationException;

    /**
     * Sets the model to the provided TaskViewFilter object. TaskViewFilters represents the
     * filter and sorting needed by each intelligent view
     */
    void view(TaskViewFilter view);

    /**
     * Filters the list of tasks by this predicate. This is filtering is ran
     * after the view predicate. No information about the search is shown to the user.
     * Setting predicate to null will reset the search.
     */
    void find(Predicate<ImmutableTask> predicate);

    /**
     * Filters the list of tasks by this predicate. This is filtering is ran
     * after the view predicate. A list of search terms is also shown to the user.
     */
    void find(Predicate<ImmutableTask> predicate, List<String> terms);

    /**
     * Undoes the last operation that modifies the todolist
     * @throws ValidationException if there are no more changes to undo
     */
    void undo() throws ValidationException;

    /**
     * Redoes the last operation that was undone
     * @throws ValidationException if there are no more changes to redo
     */
    void redo() throws ValidationException;
    
    /**
     * Changes the save path of the TodoList storage 
     * @throws ValidationException if the path is not valid
     */
    void save(String location) throws ValidationException;

    /**
     * Loads a TodoList from the path. 
     * @throws ValidationException if the path or file is invalid
     */
    void load(String location) throws ValidationException;

    /**
     * Obtains the current storage methods 
     */
    String getStorageLocation();

    /**
     * Get an observable list of tasks. Used mainly by the JavaFX UI. 
     */
    UnmodifiableObservableList<ImmutableTask> getObservableList();

    /**
     * Get the current view filter used on the model. Used mainly by the JavaFx UI. 
     */
    ObjectProperty<TaskViewFilter> getViewFilter();

    /**
     * Get the current status of the search used on the model.
     */
    ObjectProperty<SearchStatus> getSearchStatus();

    //@@author A0135805H
    /**
     * Gets a copy of the list of tags
     */
    List<Tag> getGlobalTagsList();

    /**
     * Adds the supplied list of tags (using tag names) to the specified task.
     *
     * @param index The task displayed index.
     * @param tagNames The list of tag names to be added.
     * @throws ValidationException when the given index is invalid, or the given {@code tagNames} contain
     *                             illegal characters.
     */
    void addTagsToTask(int index, String[] tagNames) throws ValidationException;

    /**
     * Deletes a list of tags (using tag names) from the specified task.
     *
     * @param index The task displayed index.
     * @param tagNames The list of tag names to be deleted.
     * @throws ValidationException when the given index is invalid, or when the tag is not found (no-op).
     */
    void deleteTagsFromTask(int index, String[] tagNames) throws ValidationException;

    /**
     * Deletes the list of tags globally.
     *
     * @param tagNames The list of tag names to be deleted.
     * @throws ValidationException when the tag is not found (no-op).
     */
    void deleteTags(String[] tagNames) throws ValidationException;

    /**
     * Renames a tag across all tasks.
     * @param oldName Name of the tag to be renamed.
     * @param newName New name that the tag will assume.
     * @throws ValidationException when the tag is not found, or the new name clashes with existing names.
     */
    void renameTag(String oldName, String newName) throws ValidationException;
}
