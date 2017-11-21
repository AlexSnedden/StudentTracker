package net.pinaz993.studenttracker;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import java.sql.Timestamp;

/**
 * Created by Patrick Shannon on 8/30/2017.
 * This package is for the Student Object, and all related objects.
 * The student object holds information about the student, and is responsible for keeping track of all
 * records pertaining to the student, such as behavior and attendance.
 * <p>
 * The student is stored using the Paper library by pilgr. A static method is provided to retrieve
 * the object from storage. Each student object is responsible for saving itself to storage when
 * told.
 * <p>
 * When data needs to be recorded about the student, the object records this data in an SQL database
 * on the device. All student records share the same database. Each record is accompanied by a
 * Student ID string, a Class ID string, and a timestamp.
 * <p>
 * If a record needs to be edited, the object will query for the record and update it. It will also
 * be able to delete records.
 */

public class Student {
    private final static String BOOK_ID = "students";
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String studentID;
    private final DatabaseHandler dbh = DatabaseHandler.getInstance();

    //<editor-fold desc="Constructors">

    /**
     * Constructor with email address
     *
     * @param firstName First name of student
     * @param lastName  Last name of student
     * @param email     Email of the student, can be null
     * @param studentID A value unique to each student. Will be used as a primary key.
     */
    Student(String firstName, String lastName, String studentID, String email) {
        //Using params, we grab the information needed to represent the student.
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentID = studentID;
    }

    /**
     * Constructor with no email address
     *
     * @param firstName First name of student
     * @param lastName  Last name of Student
     * @param studentID A value unique to each student. Will be used as a primary key
     */
    Student(String firstName, String lastName, String studentID) {
        //Same as above, sans email
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
        this.email = null;
    }
    //</editor-fold>

    //<editor-fold desc="Attendance Record Handling">
    /**
     * Record attendance with the following values:
     *
     * @param classID        The class the student was to attend
     * @param interval       The time and date of attendance. Can be null, will be set to the current one
     * @param present        Was the student present?
     * @param lateArrival    Did the student arrive late?
     * @param earlyDeparture Did the student leave class early?
     * @param excused        Was the behavior excused?
     *                       <p>
     *                       Can also be used to record attendance after the fact.
     */
    public void recordAttendence(String classID, AttendanceInterval interval,
                                 boolean present, boolean lateArrival,
                                 boolean earlyDeparture, boolean excused) {
        dbh.recordAttendance(studentID, classID, interval.getStart().getMillis(),
                present, lateArrival, earlyDeparture, excused);
    }

    /**
     * Change a specified record of attendance. Assumes record already exists. Specified by the following:
     *
     * @param classID        The class the student was to attend
     * @param interval       Attendance interval in question.  Cannot be null
     *                       Changes the following values in the record:
     * @param present        Was the student present?
     * @param lateArrival    Did the student arrive late?
     * @param earlyDeparture Did the student leave class early?
     * @param excused        Was the behavior excused?
     */
    public void updateAttendance(String classID, AttendanceInterval interval,
                                 @Nullable Boolean present, @Nullable Boolean lateArrival,
                                 @Nullable Boolean earlyDeparture, @Nullable Boolean excused) {
        Cursor c = dbh.getCurrentAttendanceRecordForStudentInClass(studentID, classID);
        c.moveToFirst();
        //<editor-fold desc="Grab current values if params are null.">
        if(present == null) present = c.getInt(c.getColumnIndex(
                DatabaseHandler.AttendanceRecordsTableSchema.PRESENT_COL)) != 0;

        if(lateArrival == null) lateArrival = c.getInt(c.getColumnIndex(
                DatabaseHandler.AttendanceRecordsTableSchema.LATE_ARRIVAL_COL)) != 0;

        if(earlyDeparture == null) earlyDeparture = c.getInt(c.getColumnIndex(
                DatabaseHandler.AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL)) != 0;

        if(excused == null) excused = c.getInt(c.getColumnIndex(
                DatabaseHandler.AttendanceRecordsTableSchema.EXCUSED_COL)) != 0;
        //</editor-fold>
        dbh.updateAttendanceRecord(this.studentID, classID, interval.getStart().getMillis(),
                present, lateArrival, earlyDeparture, excused);

    }

    /**
     * @param classID        The class the student was to attend
     * @param present        Was the student present?
     * @param lateArrival    Did the student arrive late?
     * @param earlyDeparture Did the student leave class early?
     * @param excused        Was the behavior excused?
     */
    public void updateRecentAttendance(String classID, boolean present, boolean lateArrival,
                                       boolean earlyDeparture, boolean excused) {
        updateAttendance(classID, AttendanceInterval.getCurrent(), present, lateArrival,
                earlyDeparture, excused);
    }

    /**
     * Deletes all record for this student, possibly for this student in ANY class.
     */
    public void deleteAttendance() {
        Cursor c = dbh.getClassesForStudent(this.studentID);

    }

    /**
     * Deletes all attendance records for this student and the given class.
     */
    public void deleteAttendanceForClass(String classID) {
        Cursor c = dbh.getAttendanceRecordsForStudentInClass(this.studentID, classID);
        c.moveToFirst();
        do {
            long interval = c.getInt(c.getColumnIndex(
                    DatabaseHandler.AttendanceRecordsTableSchema.INTERVAL_COL));
            dbh.deleteAttendanceRecord(this.studentID, classID, interval);
            interval = 0;
        } while (c.moveToNext());

    }

    /**
     * Queries the database to see if a record exists for this student, classID and attendance
     * interval.
     *
     * @param classID  the class the student was to attend
     * @param interval the attendance interval when the student was to attend the class
     * @return Does the record exist?
     */
    public boolean attendanceRecordExists(String classID, AttendanceInterval interval) {
        return dbh.attendanceRecordExists(this.studentID, classID, interval.getStart().getMillis());
    }

    public AttendanceSummary compileAttendanceSummery() {
        Cursor c = dbh.getAttendanceRecordsForStudent(this.studentID);
        AttendanceSummary summary = new AttendanceSummary();
        summary.totalRecords = c.getCount();
        for (int i = 1; i < c.getCount(); i++) {
            c.moveToPosition(i);
            boolean present, lateArrival, earlyDeparture, excused;
            present = c.getInt(c.getColumnIndex(
                    DatabaseHandler.AttendanceRecordsTableSchema.PRESENT_COL)) != 0;
            lateArrival = c.getInt(c.getColumnIndex(
                    DatabaseHandler.AttendanceRecordsTableSchema.LATE_ARRIVAL_COL)) != 0;
            earlyDeparture = c.getInt(c.getColumnIndex(
                    DatabaseHandler.AttendanceRecordsTableSchema.EARLY_DEPARTURE_COL)) != 0;
            excused = c.getInt(c.getColumnIndex(
                    DatabaseHandler.AttendanceRecordsTableSchema.EXCUSED_COL)) != 0;

            if(present) summary.daysPresent ++;
            else if(!excused) summary.absences ++;// Case: present is false and excused is false
            else summary.excusedAbsences ++; // Case: present is false and excused is true
            if(lateArrival && !excused) summary.lateArrivals ++;
            if(lateArrival && excused) summary.excusedLateArrivals ++;
            if(earlyDeparture && !excused) summary.earlyDepartures ++;
            if(earlyDeparture && excused) summary.excusedEarlyDepartures ++;
        }
        return summary;
    }
    //</editor-fold>

    //<editor-fold desc="BehaviorHandler Record Handling">

    /**
     * Record a notable instance of a behavior with the following information:
     *
     * @param classID    the ID of the class in which the behavior was observed
     * @param behaviorID the ID of the behavior that is observed
     * @param timestamp  the time and date of the observed behavior
     *                   This behavior is not determined within the student object. It is determined by the app itself
     *                   the object simply records what it is given.
     */
    public void recordBehavior(String classID, int behaviorID, Timestamp timestamp) {
        //TODO: Implement Student.recordBehavior()
    }

    /**
     * Deletes a behavior record from the database using the following information:
     *
     * @param classID    the ID of the class on which the behavior was observed
     * @param behaviorID the ID of the behavior that is observed
     * @param timestamp  the time and date of the observed behavior
     */
    public void deleteBehaviorRecord(String classID, int behaviorID, Timestamp timestamp) {
        //TODO:Implement Student.deleteBehaviorRecord()
    }
    //</editor-fold>

    //<editor-fold desc="Setters and Getters">
    /*public boolean isDelinquent() {
        return delinquent;
    }*/

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    //</editor-fold>


    //<editor-fold desc="StudentPaneAdapter functions">
    public void recordAbsentOrPresent(boolean present, String classID) {
        Cursor latestAttendanceRecord = dbh.getCurrentAttendanceRecordForStudentInClass(studentID, classID);
        if(latestAttendanceRecord != null) {
            /* There already exists an attendance record for the day */
            long interval = latestAttendanceRecord.getLong(latestAttendanceRecord.getColumnIndex("interval"));
            /* We do not want to modify these values for the row */
            boolean lateArrival, earlyDeparture, excused;
            if(latestAttendanceRecord.getInt(latestAttendanceRecord.getColumnIndex("lateArrival"))==1) {
                    lateArrival = true;
            } else {
                lateArrival = false;
            }
            if(latestAttendanceRecord.getInt(latestAttendanceRecord.getColumnIndex("earlyDeparture")) == 1) {
                earlyDeparture = true;
            } else {
                earlyDeparture = false;
            }
            if(latestAttendanceRecord.getInt(latestAttendanceRecord.getColumnIndex("excused")) == 1) {
                excused = true;
            } else {
                excused = false;
            }
            dbh.updateAttendanceRecord(studentID, classID, interval, present, lateArrival, earlyDeparture, excused);
        } else {
            /* create a new attendance record for right now */
            dbh.recordAttendance(studentID, classID, System.currentTimeMillis(), present, false, false, false);
        }
    }
    public void recordLateArrival(boolean lateArrival, String classID) {
        Cursor latestAttendanceRecord = dbh.getCurrentAttendanceRecordForStudentInClass(studentID, classID);
        long interval = latestAttendanceRecord.getLong(latestAttendanceRecord.getColumnIndex("interval"));
        if(latestAttendanceRecord != null) {
            /* We do not want to modify these values for the row */
            boolean present, earlyDeparture, excused;
            if(latestAttendanceRecord.getInt(latestAttendanceRecord.getColumnIndex("present"))==1) {
                present = true;
            } else {
                present = false;
            }
            if(latestAttendanceRecord.getInt(latestAttendanceRecord.getColumnIndex("earlyDeparture")) == 1) {
                earlyDeparture = true;
            } else {
                earlyDeparture = false;
            }
            if(latestAttendanceRecord.getInt(latestAttendanceRecord.getColumnIndex("excused")) == 1) {
                excused = true;
            } else {
                excused = false;
            }
            dbh.updateAttendanceRecord(studentID, classID, period,
        } else {

        }
    }
    //</editor-fold>

    /**
     * Used to summarise all attendance records recorded for a student.
     * Will not be initialised on creation. Will only be created when needed.
     * TODO: Integrate with SQL Implementation of attendance records
     */
    private class AttendanceSummary {
        int totalRecords, absences, excusedAbsences;
        int daysPresent;
        int lateArrivals;
        int excusedLateArrivals;
        int earlyDepartures;
        int excusedEarlyDepartures;
    }
}
