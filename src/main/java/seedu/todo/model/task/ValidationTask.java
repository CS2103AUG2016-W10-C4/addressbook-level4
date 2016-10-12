package seedu.todo.model.task;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.model.ErrorBag;
import seedu.todo.model.tag.Tag;

public class ValidationTask implements ImmutableTask {
    private static final String TITLE = "title";
    private static final String START_TIME = "startTime";
    private static final String END_TIME = "endTime";

    private static final String ONLY_ONE_TIME_ERROR_MESSAGE = "Field=%s is not defined";
    private static final String TITLE_EMPTY_ERROR_MESSAGE = "Title should not be a empty string.";
    private static final String VALIDATION_ERROR_MESSAGE = "Model validation failed";
    private static final String START_AFTER_END_ERROR_MESSAGE = "startTime is after endTime";

    private ErrorBag errors = new ErrorBag();

    private String title;
    private String description;
    private String location;

    private boolean pinned;
    private boolean completed;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Set<Tag> tags = new HashSet<Tag>();
    private UUID uuid;

    public ValidationTask(String title) {
        this.setTitle(title);
        this.uuid = UUID.randomUUID();
    }

    /**
     * Constructs a ValidationTask from an ImmutableTask
     */
    public ValidationTask(ImmutableTask task) {
        this.setTitle(task.getTitle());
        this.setDescription(task.getDescription().orElse(null));
        this.setLocation(task.getLocation().orElse(null));
        this.setStartTime(task.getStartTime().orElse(null));
        this.setEndTime(task.getEndTime().orElse(null));
        this.setCompleted(task.isCompleted());
        this.setPinned(task.isPinned());
        this.uuid = task.getUUID();
    }

    /**
     * Validates the task by checking the individual fields are valid.
     */
    public void validate() throws ValidationException {
        isValidTime();
        isValidTitle();
        errors.validate(VALIDATION_ERROR_MESSAGE);
    }

    private void isValidTitle() {
        if (title.isEmpty()) {
            errors.put(TITLE, TITLE_EMPTY_ERROR_MESSAGE);
        }
    }

    /**
     * Validates time. Only valid when
     * 1) both time fields are not declared
     * 2) start time is before end time
     */
    private void isValidTime() {
        if (startTime == null && endTime == null) {
            return;
        } else if (startTime == null) {
            String field = START_TIME;
            errors.put(field, String.format(ONLY_ONE_TIME_ERROR_MESSAGE, field));
        } else if (endTime == null) {
            String field = END_TIME;
            errors.put(field, String.format(ONLY_ONE_TIME_ERROR_MESSAGE, field));
        } else if (startTime.isAfter(endTime)) {
            errors.put(START_TIME, START_AFTER_END_ERROR_MESSAGE);
        }
    }

    /**
     * Converts the validation task into an actual task for consumption.
     * 
     * @return A task with observable properties
     */
    public Task convertToTask() throws ValidationException {
        validate();
        return new Task(this);
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Optional<String> getDescription() {
        return Optional.ofNullable(description);
    }

    @Override
    public Optional<String> getLocation() {
        return Optional.ofNullable(location);
    }

    @Override
    public Optional<LocalDateTime> getStartTime() {
        return Optional.ofNullable(startTime);
    }

    @Override
    public Optional<LocalDateTime> getEndTime() {
        return Optional.ofNullable(endTime);
    }

    @Override
    public boolean isPinned() {
        return pinned;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setPinned(boolean pinned) {
        this.pinned = pinned;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

}
