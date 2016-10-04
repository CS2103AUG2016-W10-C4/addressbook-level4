package seedu.todo.model.task;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.google.common.collect.ImmutableSet;

import seedu.todo.model.tag.Tag;

public interface ReadOnlyTask {
    public String getTitle();
    
    public Optional<String> getDescription();
    
    public Optional<String> getLocation();
    
    public Optional<LocalDateTime> getStartTime();

    public Optional<LocalDateTime> getEndTime();
    
    public boolean isPinned();
    
    public boolean isCompleted();
    
    default public boolean isEvent() {
        return this.getStartTime().isPresent();
    }
    
    public ImmutableSet<Tag> getTags();
    
    public UUID getUUID();
}