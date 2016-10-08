package seedu.todo.commons.util;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Utility methods that deals with time.
 */
public class TimeUtil {
    
    /* Constants */
    private static final String PREFIX_BEFORE_DEADLINE = "in ";
    
    private static final String HOUR_SINGLE_UNIT = " hour";
    private static final String HOURS_MULTIPLE_UNIT = " hours";
    private static final String MINUTE_SINGLE_UNIT = " minute";
    private static final String MINUTES_MULTIPLE_UNIT = " minutes";
    
    private static final String DUE_NOW = "right now";
    
    /* Variables */
    private final Clock clock;
    
    /**
     * Constructs a TimeUil object using system clock.
     */
    public TimeUtil () {
        this.clock = Clock.systemDefaultZone();
    }
    
    /**
     * Construct a TimeUtil object with a custom clock.
     * Can be used for dependency injection in testing (to override system time)
     */
    public TimeUtil (Clock clock) {
        this.clock = clock;
    }
    
    
    
    /**
     * Gets the task deadline expression for the UI.
     * @param endTime ending time
     * @return a formatted deadline String
     */
    public String getTaskDeadlineString(LocalDateTime endTime) {
        assert(endTime != null);
        
        LocalDateTime currentTime = LocalDateTime.now(clock);
        Duration durationCurrentToEnd = Duration.between(currentTime, endTime);
        
        long hoursToDeadline = durationCurrentToEnd.toHours();
        long minutesToDeadline = durationCurrentToEnd.toMinutes();
        
        if (minutesToDeadline == 0) {
            return DUE_NOW;
        }
        
        if (hoursToDeadline == 0) {
            
            if (minutesToDeadline == 1) {
                return PREFIX_BEFORE_DEADLINE + "1" + MINUTE_SINGLE_UNIT;
            } else {
                return PREFIX_BEFORE_DEADLINE + String.valueOf(minutesToDeadline) + MINUTES_MULTIPLE_UNIT;
            }
                      
        } else if (hoursToDeadline == 1) {
            return PREFIX_BEFORE_DEADLINE + "1" + HOUR_SINGLE_UNIT;
        } else {
            return PREFIX_BEFORE_DEADLINE + String.valueOf(hoursToDeadline) + HOURS_MULTIPLE_UNIT;
        }
    }
    
    
    public String getEventTimeString(LocalDateTime startTime, LocalDateTime endTime) {
        assert(startTime != null);
        assert(endTime != null);
        assert(startTime.isBefore(endTime));
        
        return null;
    }
    
}
