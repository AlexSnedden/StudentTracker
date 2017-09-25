package net.pinaz993.studenttracker;

import java.util.Date;

public class AttendanceRecord {
    //TODO: How do we know what class the student was present/absent for?
    private Date timestamp;
    private boolean present, lateArrival, earlyDeparture;
    private String classID;

    AttendanceRecord(boolean present, boolean lateArrival, boolean earlyDeparture, String classID) {
        this.present = present;
        this.lateArrival = lateArrival;
        this.earlyDeparture = earlyDeparture;
        this.timestamp = new Date();
        this.classID = classID;
    }

    AttendanceRecord(Date timestamp, boolean present, boolean lateArrival, boolean earlyDeparture, String classID) {
        this.timestamp = timestamp;
        this.present = present;
        this.lateArrival = lateArrival;
        this.earlyDeparture = earlyDeparture;
        this.classID = classID;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public boolean isLateArrival() {
        return lateArrival;
    }

    public void setLateArrival(boolean lateArrival) {
        this.lateArrival = lateArrival;
    }

    public boolean isEarlyDeparture() {
        return earlyDeparture;
    }

    public void setEarlyDeparture(boolean earlyDeparture) {
        this.earlyDeparture = earlyDeparture;
    }

    public String getClassID() {
        return classID;
    }

    public void setClassID(String classID) {
        this.classID = classID;
    }
}
