package net.pinaz993.studenttracker;

import android.support.annotation.Nullable;

import java.sql.Timestamp;


/**
 * Created by Patrick Shannon on 8/30/2017.
 * This package is for the Student Object, and all related objects.
 * The student object holds information about the student, and is responsible for keeping track of all
 * records pertaining to the student, such as behavior and attendance.
 *
 * The student is stored using the Paper library by pilgr. A static method is provided to retrieve
 * the object from storage. Each student object is responsible for saving itself to storage when
 * told.
 *
 * When data needs to be recorded about the student, the object records this data in an SQL database
 * on the device. All student records share the same database. Each record is accompanied by a
 * Student ID string, a Class ID string, and a timestamp.
 *
 * If a record needs to be edited, the object will query for the record and update it. It will also
 * be able to delete records.
 */

public class Student {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String studentID;

    //<editor-fold desc="Constructors">

    /**
     * Constructor with email address
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
    public void recordAttendence(String classID, @Nullable AttendanceInterval interval,
                                 boolean present, boolean lateArrival,
                                 boolean earlyDeparture, boolean excused) {
        //TODO: Implement Student.recordAttendance()

        //Table is already set up, so lets check the data
        //If the timestamp is null, set it to now()

        //If present is false, late arrival and early departure are automatically false

        //Store it, using AttendanceInterval.getStart() in the timestamp column

    }

    /**
     * Change a specified record of attendance. Specified by the following:
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
                                 boolean present, @Nullable boolean lateArrival,
                                 @Nullable boolean earlyDeparture, @Nullable boolean excused) {
        //TODO: Implement Student.updateAttendance()

        //If present is false, late arrival and early departure are automatically false

        //If any values are null, make sure they are not changed in the record

        //Change it.

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
        //TODO: Implement Student.updateRecentAttendance()

        //Find the record (there should only be one) within the last attendance interval, and change
        //it. The attendance period is defined elsewhere, with a default of one day.
    }

    /**
     * Deletes all record for this student, possibly for this student and a given class
     *
     * @param classID The class the student was to attend
     */
    public void deleteAttendance(@Nullable String classID) {
        //TODO: Implement Student.deleteAttendance()

        if (classID != null) {
            //delete all records for this student and that class ID.
        } else {
            //delete all attendance records for this student... period.
        }
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
        //TODO: Implement Student.RecordExists

        //Query the database to find a record for this student with this class id and in that
        //attendance interval
        return false;
    }

    public AttendanceSummary compileAttendanceSummery() {
        //TODO: Implement Student.compileAttendanceSummery()

        //query the database gathering all records for the student.
        //add up all days absent, days present, late arrivals, and early departures.
        //for each category, note the number of these that are excused
        return null;
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
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getID() {
        return studentID;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    //</editor-fold>


    /**
     * Used to summarise all attendance records recorded for a student.
     * Will not be initialised on creation. Will only be created when needed.
     * TODO: Integrate with SQL Implementation of attendance records
     */
    private class AttendanceSummary {
        int totalRecords, Absences, excusedAbsenses;
        int daysPresent;
        int lateArrivals;
        int excusedLateArrivals;
        int earlyDepartures;
        int excusedEarlyDepartures;
    }


    /**
     * An object for storing an interval of attendance. Can be a week long, or a day long.
     * This is used to make sure that there are never two records for the same attendance interval,
     * classID and studentID in the database. A global setting determines the duration of the
     * attendance interval, which is 24h, by default.
     * All intervals must be alligned to some baseline, Sunday, January 1, 2017 by default.
     */
}

