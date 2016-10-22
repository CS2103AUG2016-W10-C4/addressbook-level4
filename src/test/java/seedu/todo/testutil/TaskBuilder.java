package seedu.todo.testutil;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.model.tag.Tag;
import seedu.todo.model.task.Task;

/**
 * Builds a task for testing purposes.
 */
public class TaskBuilder {

    private Task task;
    private boolean defaultTime = true; 
    
    // Set the time back six hours to ensure lastModified can be set properly 
    private static LocalDateTime now = LocalDateTime.now();

    private TaskBuilder(String name) {
        task = new Task(name);
    }
    
    public static TaskBuilder name(String name) {
        return new TaskBuilder(name);
    }

    public TaskBuilder description(String description) {
        task.setDescription(description);
        return this;
    }
    
    public TaskBuilder location(String location) {
        task.setLocation(location);
        return this;
    }

    public TaskBuilder lastUpdated(LocalDateTime lastUpdated) throws IllegalValueException {
        defaultTime = false;
        task.setCreatedAt(lastUpdated);
        return this;
    }

    public TaskBuilder completed() {
        task.setCompleted(true);
        return this;
    }

    public TaskBuilder pinned() {
        task.setPinned(true);
        return this;
    }
    
    public TaskBuilder due() {
        return due(TimeUtil.tomorrow().plusHours(12));
    }
    
    public TaskBuilder due(LocalDateTime due) {
        task.setEndTime(due);
        return this;
    }

    public TaskBuilder event() {
        return event(TimeUtil.tomorrow().plusHours(12), TimeUtil.tomorrow().plusHours(14));
    }

    public TaskBuilder event(LocalDateTime start, LocalDateTime end) {
        task.setStartTime(start);
        task.setEndTime(end);
        return this;
    }

    public TaskBuilder tagged(String ... tags) throws IllegalValueException {
        Set<Tag> setOfTags = new HashSet<>();
        for (String tag: tags) {
            setOfTags.add(new Tag(tag));
        }
        task.setTags(setOfTags);
        return this;
    }
    
    public Task build() {
        // Push the time up by 1s to avoid colliding with previously created tasks 
        if (defaultTime) {
            now = now.plusSeconds(1);
            task.setCreatedAt(now);
        }
        
        return task;
    }

}
