package net.pinaz993.studenttracker;

import org.joda.time.Duration;
import org.joda.time.Instant;
import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class AttendanceInterval{
    Duration duration;
    Instant start;
    Interval interval;

    static long getCurrentStart(){
        return getCurrent().start.getMillis();
    }

    static AttendanceInterval getCurrent() {
        Instant now = new Instant();
        return getFromInstant(now);
    }

    static AttendanceInterval getFromInstant(Instant then){
        LocalDate now = then.toDateTime().toLocalDate();
        Instant newStart;
        if (SettingsHandler.get().weekly) {
            now = now.dayOfWeek().withMinimumValue();
            newStart = now.toDateTimeAtStartOfDay().toInstant();
        } else if(SettingsHandler.get().daily) {
            newStart = now.toDateTimeAtStartOfDay().toInstant();
        } else {
            newStart = new Instant(0); //Shouldn't ever happen, but is easy to test for.
        }
        return new AttendanceInterval(newStart);
    }

    // To ensure proper alignment, all initializations will take place in static methods.
    private AttendanceInterval(Instant start) {
        this.start = start;
        duration = SettingsHandler.get().attendanceIntervalDuration;
        interval = new Interval(start, duration);
    }

    public boolean isInInterval(Instant test) {
        return interval.contains(test);
    }

    public Instant getStart() {
       return start;
    }
}
