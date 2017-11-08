package net.pinaz993.studenttracker;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class AttendanceInterval{
    Duration duration;
    Instant start;
    Interval interval;
    SettingsHandler settings;

    static long getCurrentStart(SettingsHandler settings){
        return getCurrent(settings).start.getMillis();
    }

    static AttendanceInterval getCurrent(SettingsHandler settings) {
        Instant now = new Instant();
        return getFromInstant(now,settings);
    }

    static AttendanceInterval getFromInstant(Instant then, SettingsHandler settings){
        LocalDate now = then.toDateTime().toLocalDate();
        Instant newStart;
        if (settings.weekly) {
            now = now.dayOfWeek().withMinimumValue();
            newStart = now.toDateTimeAtStartOfDay().toInstant();
        } else if(settings.daily) {
            newStart = now.toDateTimeAtStartOfDay().toInstant();
        } else {
            newStart = new Instant(0); //Shouldn't ever happen, but is easy to test for.
        }
        return new AttendanceInterval(newStart, settings);
    }

    // To ensure proper alignment, all initializations will take place in static methods.
    private AttendanceInterval(Instant start, SettingsHandler settings) {
        this.start = start;
        duration = settings.attendanceIntervalDuration;
        interval = new Interval(start, duration);
    }

    public boolean isInInterval(Instant test) {
        return interval.contains(test);
    }

    public Instant getStart() {
       return start;
    }
}
