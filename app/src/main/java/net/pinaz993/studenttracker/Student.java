package net.pinaz993.studenttracker;
import android.support.annotation.Nullable;

import java.sql.Timestamp;

import io.paperdb.Paper;


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
    private final static String BOOK_ID = "students";
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String studentID;
    private boolean delinquent; // condition for read formatting on the student's name

    public static Student retrieve(String studentID) {
        return Paper.book(BOOK_ID).read(studentID);
    }

    /**
     * Constructor with email address
     * @param firstName First name of student
     * @param lastName Last name of student
     * @param email Email of the student, can be null
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
     * @param lastName Last name of Student
     * @param studentID A value unique to each student. Will be used as a primary key
     */
    Student(String firstName, String lastName, String studentID) {
        //Same as above, sans email
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentID = studentID;
        this.email = null;
    }

    /**
     * Record attendance with the following values:
     * @param classID The class the student was to attend
     * @param timestamp The time and date of attendance. Can be null, will be set to now()
     * @param present Was the student present?
     * @param lateArrival Did the student arrive late?
     * @param earlyDeparture Did the student leave class early?
     * @param excused Was the behavior excused?
     *
     * Can also be used to record attendance after the fact.
     */
    public void recordAttendence(String classID, @Nullable Timestamp timestamp,
                                 boolean present, boolean lateArrival,
                                 boolean earlyDeparture, boolean excused) {
        //TODO: Implement Student.recordAttendance()

        //Table is already set up, so lets check the data
        //If the timestamp is null, set it to now()

        //If present is false, late arrival and early departure are automatically false

        //Store it.

    }

    /**
     * Change a specified record of attendance. Specified by the following:
     * @param classID The class the student was to attend
     * @param timestamp The time and date of attendance.  Cannot be null
     * Changes the following values in the record:
     * @param present Was the student present?
     * @param lateArrival Did the student arrive late?
     * @param earlyDeparture Did the student leave class early?
     * @param excused Was the behavior excused?
     */
    public void changeAttendance(String classID, Timestamp timestamp,
                                 boolean present, @Nullable boolean lateArrival,
                                 @Nullable boolean earlyDeparture, @Nullable boolean excused) {
        //TODO: Implement Student.changeAttendance()

        //If present is false, late arrival and early departure are automatically false

        //If any values are null, make sure they are not changed in the record

        //Change it.

    }

    /**
     *
     * @param classID The class the student was to attend
     * @param present Was the student present?
     * @param lateArrival Did the student arrive late?
     * @param earlyDeparture Did the student leave class early?
     * @param excused Was the behavior excused?
     */
    public void changeRecentAttendance(String classID, boolean present, boolean lateArrival,
                                       boolean earlyDeparture, boolean excused) {
        //TODO: Implement Student.changeRecentAttendance()

        //Find the record (there should only be one) within the last attendance period, and change
        //it. The attendance period is defined elsewhere, with a default of one day.

    }



    public void save() {
        Paper.book(BOOK_ID).write(studentID, this);
    }

    //<editor-fold desc="Setters and Getters">
    public boolean isDelinquent() {return delinquent;}

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getEmail() {return email;}

    public String getStudentID() {return studentID;}

    public String getFullName() {return firstName + " " + lastName;}
    //</editor-fold>


    /**
     * Used to summarise all attendance records recorded for a student.
     * Will not be initialised on creation. Will only be created when needed.
     * TODO: Integrate with SQL Implementation of attendance records
     */
    private class AttendanceSummary {
        int daysAbsent;
        int daysPresent;
        int lateArrivals;
        int earlyDepartures;

        AttendanceSummary() {}
    }
}