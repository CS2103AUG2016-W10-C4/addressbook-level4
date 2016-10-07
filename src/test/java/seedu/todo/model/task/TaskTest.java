package seedu.todo.model.task;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.model.tag.Tag;

public class TaskTest {
    /**
     * A mock Task class with setable UUID for testing equality
     */
    private class UUIDTestTask implements ImmutableTask {
        private UUID uuid;
        
        public UUIDTestTask(UUID uuid) {
            this.uuid = uuid; 
        }

        @Override
        public String getTitle() {
            return null;
        }

        @Override
        public Optional<String> getDescription() {
            return null;
        }

        @Override
        public Optional<String> getLocation() {
            return null;
        }

        @Override
        public Optional<LocalDateTime> getStartTime() {
            return null;
        }

        @Override
        public Optional<LocalDateTime> getEndTime() {
            return null;
        }

        @Override
        public boolean isPinned() {
            return false;
        }

        @Override
        public boolean isCompleted() {
            return false;
        }

        @Override
        public Set<Tag> getTags() {
            return null;
        }

        @Override
        public UUID getUUID() {
            return uuid;
        }
    }
    
    private Task task;
    
    private void assertAllPropertiesEqual(ImmutableTask a, ImmutableTask b) {
        assertEquals(a.getTitle(), b.getTitle());
        assertEquals(a.getDescription(), b.getDescription());
        assertEquals(a.getLocation(), b.getLocation());
        assertEquals(a.getStartTime(), b.getStartTime());
        assertEquals(a.getEndTime(), b.getEndTime());
        assertEquals(a.getTags(), b.getTags());
        assertEquals(a.getUUID(), b.getUUID());
    }

    @Before
    public void setUp() throws Exception {
        task = new Task("Test Task");
    }

    @Test
    public void testTaskString() {
        assertEquals("Test Task", task.getTitle());
    }

    @Test
    public void testTaskImmutableTask() {
        Task original = new Task("Mock Task");
        assertAllPropertiesEqual(original, new Task(original));
        
        original = new Task("Mock Task"); 
        original.setStartTime(LocalDateTime.now());
        original.setEndTime(LocalDateTime.now().plusHours(2));
        assertAllPropertiesEqual(original, new Task(original));
        
        original = new Task("Mock Task"); 
        original.setDescription("A Test Description");
        original.setLocation("Test Location");
        assertAllPropertiesEqual(original, new Task(original));
    }

    @Test
    public void testTitle() {
        task.setTitle("New Title");
        assertEquals("New Title", task.getTitle());
    }
    
    @Test
    public void testDescription() {
        assertFalse(task.getDescription().isPresent());
        
        task.setDescription("A short description");
        assertEquals("A short description", task.getDescription().get());
    }

    @Test
    public void testLocation() {
        assertFalse(task.getLocation().isPresent());

        task.setLocation("Some Test Location");
        assertEquals("Some Test Location", task.getLocation().get());
    }

    @Test
    public void testTime() {
        assertFalse(task.getStartTime().isPresent());
        assertFalse(task.getEndTime().isPresent());
        
        // TODO: Time definitely needs validation, for example task end time 
        // should come after start time. Issue #16 https://github.com/CS2103AUG2016-W10-C4/main/issues/16 
        // is blocking this though
    }

    @Test
    public void testPinned() {
        assertFalse(task.isPinned());

        task.setPinned(true);
        assertTrue(task.isPinned());
    }

    @Test
    public void testCompleted() {
        assertFalse(task.isCompleted());

        task.setCompleted(true);
        assertTrue(task.isCompleted());
    }

    @Test
    public void testTags() throws IllegalValueException {
        assertEquals(0, task.getTags().size());
        
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag("Hello"));
        tags.add(new Tag("World"));
        task.setTags(tags);
        
        assertEquals(2, task.getTags().size());
        // TODO: This should do more when we finalize how tags can be edited 
    }

    @Test
    public void testGetObservableProperties() {
        assertEquals(8, task.getObservableProperties().length);
    }

    @Test
    public void testGetUUID() {
        assertNotNull(task.getUUID());
    }

    @Test
    public void testEqualsObject() {
        UUIDTestTask testTask = new UUIDTestTask(task.getUUID());
        assertEquals(task, testTask);
        assertFalse(task.equals(12));
    }

}
