package net.pinaz993.studenttracker;

import java.sql.Timestamp;

/**
 * Created by Patrick Shannon on 8/30/2017.
 * This packacge is for the Student Object, and all related objects.
 */

public class Student {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String studentID;
    private boolean delinquent; // condition for read formatting on the student's name

    /**
     *
     * @param firstName First name of student
     * @param lastName Last name of student
     * @param email Email of the student, can be null
     * @param studentID A value unique to each student. Will be used as a primary key.
     */

    Student(String firstName, String lastName, String email, String studentID) {
        //Using params, we grab the information needed to represent the student.
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentID = studentID;
        //TODO: Finish constructor for Student
    }

    /**
     *
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
        //TODO: Finish email-less constructor for Student
    }

    /**
     * Takes attendance data and records it in storage
     * @param present If the student was there at all
     * @param lateArrival If the student arrived late to class
     * @param earlyDeparture If the student left class early
     * @param time Timestamp object for recording date and time of record
     */
    public void recordAttendace(boolean present, boolean lateArrival,
                                boolean earlyDeparture, Timestamp time){
        //If not present, just mark that.

        //else if present, but late or early leave, record that

        //else, record present

        //Use db to save record with timestamp

        //TODO: Finish Student.recordAttendance()
    }

    public void compileAttendanceReport(){
        //Should compile total number of days absent, days late, days left early
        //For use in visual report compilation

        //Go to db and fetch all records pertaining to the current student

        //Add up all days absent

        //Add up all days present and
        //TODO: Finish Student.compileAttendanceReport()
    }


    public boolean isDelinquent() {
        return delinquent;
    }

    public void setDelinquent(boolean delinquent) {
        this.delinquent = delinquent;
    }
}


