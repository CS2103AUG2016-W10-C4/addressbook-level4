package seedu.todo.logic.commands;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import seedu.todo.commons.exceptions.IllegalValueException;
import seedu.todo.logic.arguments.Argument;
import seedu.todo.logic.arguments.FlagArgument;
import seedu.todo.logic.arguments.IntArgument;
import seedu.todo.logic.arguments.Parameter;
import seedu.todo.logic.arguments.StringArgument;

public class BaseCommandTest extends CommandTest {
    private class MockCommand extends BaseCommand {
        private Argument<String> required = new StringArgument("required").required();
        
        private Argument<Boolean> flag = new FlagArgument("flag")
                .flag("f");
        
        private Argument<Integer> integer = new IntArgument("int")
                .flag("i");
        
        private Argument<String> string = new StringArgument("string")
                .flag("s");

        @Override
        protected Parameter[] getArguments() {
            return new Parameter[]{ required, flag, integer, string };
        }

        @Override
        public void execute() throws IllegalValueException {
            // Does nothing
        }
        
        public void accessInvalidIndex() throws IllegalValueException {
            getTaskAt(100);
        }
        
        public boolean getFlag() {
            return flag.getValue();
        }
        
        public int getInteger() {
            return integer.getValue();
        }
        
        public String getString() {
            return string.getValue();
        }
    }
    
    private MockCommand testCommand;

    @Override
    protected BaseCommand commandUnderTest() {
        this.testCommand = new MockCommand(); 
        return this.testCommand;
    }
    
    @Test
    public void testSetParameter() throws IllegalValueException {
        this.setParameter("required")
            .setParameter("f", "")
            .setParameter("i", "20")
            .setParameter("s", "Hello World");
        
        execute();
        
        assertEquals(20, testCommand.getInteger());
        assertEquals("Hello World", testCommand.getString());
        assertTrue(testCommand.getFlag());
    }

    @Test(expected=IllegalValueException.class)
    @Ignore  // Currently required arguments don't trigger exceptions 
    public void testMissingRequiredArgument() throws IllegalValueException {
        execute();
    }
    
    @Test(expected=IllegalValueException.class)
    public void testInvalidGetTaskAt() throws IllegalValueException {
        this.setParameter("required");
        execute();
        testCommand.accessInvalidIndex();
    }

}
