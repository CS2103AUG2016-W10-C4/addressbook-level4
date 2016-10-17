package seedu.todo.logic.commands;



import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import seedu.todo.commons.events.ui.ExpandCollapseTaskEvent;
import seedu.todo.commons.exceptions.ValidationException;
import seedu.todo.testutil.EventsCollector;

import static org.hamcrest.Matchers.*;

public class ShowCommandTest extends CommandTest {
    
    @Override
    protected BaseCommand commandUnderTest() {
        return new ShowCommand();
    }

    @Before
    public void setUp() throws Exception {
        model.add("Task 1");
        model.add("Task 3");
    }

    @Test
    public void test() throws ValidationException {
        EventsCollector eventCollector = new EventsCollector();
        execute(true);
        assertThat(eventCollector.get(0), instanceOf(ExpandCollapseTaskEvent.class));
    }

    

}
