package seedu.todo.commons.util;

import static org.junit.Assert.*;

import java.time.LocalDateTime;
import org.junit.Test;

public class TimeUtilTest {
    
    @Test (expected = AssertionError.class)
    public void printFormattedTime_nullEndTime() {
        TimeUtil.printFormattedTime(LocalDateTime.now(), null);
    }
    
    @Test
    public void printFormattedTime_hoursBeforeDeadlines() {
        for (long hoursLeft = 2; hoursLeft < 24; hoursLeft++) {
            LocalDateTime inputTime = LocalDateTime.now().plusHours(hoursLeft).plusMinutes(1);
            String input = TimeUtil.printFormattedTime(null, inputTime);
            String output = "in " + String.valueOf(hoursLeft) + " hours";
            assertEquals(input, output);
        }
    }

}
