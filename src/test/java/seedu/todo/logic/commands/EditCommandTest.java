package seedu.todo.logic.commands;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.model.task.ImmutableTask;
import seedu.todo.testutil.TimeUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class EditCommandTest extends CommandTest {
    @Override
    protected BaseCommand commandUnderTest() {
        return new EditCommand();
    }
    
    @Before
    public void setUp() throws Exception {
        // TODO: fix sorting problems when adding tasks
        model.add("Task 1", task-> {
            task.setPinned(true);
            task.setLocation("NUS");
        });
        
        model.add("Task 3", task-> {
            task.setDescription("Description");
            // 12-10-2016 16:00
            task.setEndTime(LocalDateTime.of(2016, 10, 12, 16, 0));
        });
        
        model.add("Task 2");
    }
    
    
    @Test(expected=IllegalValueException.class)
    public void testEditInvalidIndex() throws Exception {
        setParameter("4");
        setParameter("l", "If this prints out this might hurt");
        execute();
    }
    
    @Test
    public void testEditPinned() throws Exception {
        setParameter("1");
        setParameter("p", null);
        execute();
        
        ImmutableTask task = getTaskAt(1);
        assertEquals("Task 1", task.getTitle());
        assertTrue(task.isPinned());
        assertEquals("NUS", task.getLocation().get());
        assertFalse(task.getDescription().isPresent());
    }
    
    @Test
    public void testEditLocation() throws Exception {
        setParameter("3");
        setParameter("l", "NTU");
        execute();
        
        ImmutableTask task = getTaskAt(3);
        assertEquals("Task 3", task.getTitle());
        assertEquals("NTU", task.getLocation().get());
        assertFalse(task.isPinned());
        assertEquals("Description", task.getDescription().get());
    }
    
    @Test
    public void testEditDescription() throws Exception {
        setParameter("2");
        setParameter("m", "Some other description");
        execute();
        
        ImmutableTask toEditDesc = getTaskAt(2);
        assertEquals("Task 2", toEditDesc.getTitle());
        assertFalse(toEditDesc.isPinned());
        assertFalse(toEditDesc.getLocation().isPresent());
        assertEquals("Some other description", toEditDesc.getDescription().get());
    }
    
    @Test
    public void testDeleteField() throws Exception {
        setParameter("2");
        setParameter("m", "");
        execute();
        
        ImmutableTask toDeleteField = getTaskAt(2);
        assertEquals("Task 2", toDeleteField.getTitle());
        assertFalse(toDeleteField.isPinned());
        assertFalse(toDeleteField.getDescription().isPresent());
        assertFalse(toDeleteField.getLocation().isPresent());
    }


    @Test
    public void testAddSingleDate() throws Exception {
        setParameter("1");
        setParameter("d", "tomorrow");
        execute();
        
        ImmutableTask task = getTaskAt(1);
        assertFalse(task.isEvent());
        assertFalse(task.getStartTime().isPresent());
        assertEquals(LocalDate.now().plusDays(1), task.getEndTime().get().toLocalDate());
    }

    @Test
    public void testAddDateRange() throws Exception {
        setParameter("1");
        setParameter("d", "tomorrow 10 to 11pm");
        execute();

        ImmutableTask task = getTaskAt(1);
        assertTrue(task.isEvent());
        assertEquals(TimeUtil.tomorrow().withHour(22), task.getStartTime().get());
        assertEquals(TimeUtil.tomorrow().withHour(23), task.getEndTime().get());
    }
    
    @Test
    public void testRemoveDate() throws Exception {
        setParameter("2");
        setParameter("d", "");
        execute();
        
        ImmutableTask task = getTaskAt(2);
        assertFalse(task.getEndTime().isPresent());
    }

    @Test
    public void testEditMoreThanOneParameter() throws Exception {
        setParameter("1");
        setParameter("m", "New description");
        setParameter("l", "Singapura");
        execute();
        
        ImmutableTask toEditTwoThings = getTaskAt(1);
        assertEquals("Task 1", toEditTwoThings.getTitle());
        assertTrue(toEditTwoThings.isPinned());
        assertEquals("New description", toEditTwoThings.getDescription().get());
        assertEquals("Singapura", toEditTwoThings.getLocation().get());
    }
}
