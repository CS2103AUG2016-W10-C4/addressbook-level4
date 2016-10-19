package seedu.todo.commons.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.FileNotFoundException;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void isUnsignedPositiveInteger() {
        assertFalse(StringUtil.isUnsignedInteger(null));
        assertFalse(StringUtil.isUnsignedInteger(""));
        assertFalse(StringUtil.isUnsignedInteger("a"));
        assertFalse(StringUtil.isUnsignedInteger("aaa"));
        assertFalse(StringUtil.isUnsignedInteger("  "));
        assertFalse(StringUtil.isUnsignedInteger("-1"));
        assertFalse(StringUtil.isUnsignedInteger("0"));
        assertFalse(StringUtil.isUnsignedInteger("+1")); //should be unsigned
        assertFalse(StringUtil.isUnsignedInteger("-1")); //should be unsigned
        assertFalse(StringUtil.isUnsignedInteger(" 10")); //should not contain whitespaces
        assertFalse(StringUtil.isUnsignedInteger("10 ")); //should not contain whitespaces
        assertFalse(StringUtil.isUnsignedInteger("1 0")); //should not contain whitespaces

        assertTrue(StringUtil.isUnsignedInteger("1"));
        assertTrue(StringUtil.isUnsignedInteger("10"));
    }

    @Test
    public void getDetails_exceptionGiven(){
        assertThat(StringUtil.getDetails(new FileNotFoundException("file not found")),
                   containsString("java.io.FileNotFoundException: file not found"));
    }

    @Test
    public void getDetails_nullGiven_assertionError(){
        thrown.expect(AssertionError.class);
        StringUtil.getDetails(null);
    }

    @Test
    public void isEmpty() {
        // EP: null
        assertTrue(StringUtil.isEmpty(null));
        
        // EP: empty string (length zero) 
        assertTrue(StringUtil.isEmpty(""));
        
        // EP: whitespace characters only 
        assertTrue(StringUtil.isEmpty("    "));
        assertTrue(StringUtil.isEmpty("\t\n"));
        assertTrue(StringUtil.isEmpty("\r \r\n"));
        
        // EP: Non-empty strings 
        assertFalse(StringUtil.isEmpty("a")); // One character 
        assertFalse(StringUtil.isEmpty("  ah c ")); // Leading and trailing whitespace
        assertFalse(StringUtil.isEmpty(" c ah c")); 
        assertFalse(StringUtil.isEmpty("ah c ")); 
        
        assertFalse(StringUtil.isEmpty("12345")); // Non-alphabet characters
        assertFalse(StringUtil.isEmpty("!@#*&")); // Non-alphanumeric characters  
    }
}
