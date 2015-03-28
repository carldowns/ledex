package system;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

/**
 * Joda time interface
 * @see http://www.joda.org/joda-time/userguide.html
 * instant + duration = instant
 * instant + period = instant
 */

public class CmdTimer {

    @JsonProperty
    DateTime dt = new DateTime(); // universal time zone

    public void plusDays (int days) {
        dt = dt.plusDays(days);
    }

    public void plusMinutes (int minutes) {
        dt = dt.plusMinutes(minutes);
    }

    public void plusSeconds (int seconds) {
        dt = dt.plusSeconds(seconds);
    }

    public void plusMillis (int millis) {
        dt = dt.plusMillis(millis);
    }

    // example format: "2004-12-13T21:39:45.618-08:00"
    public void at (String format) {
        dt = new DateTime(format);
    }

}
