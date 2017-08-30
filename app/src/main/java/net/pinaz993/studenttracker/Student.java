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

    Student(String firstName, String lastName, String email, String studentID) {
        //Using params, we grab the information needed to represent the student.
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.studentID = studentID;
        //TODO: Finish Constructor
    }

    public void recordAttendace(boolean present, boolean lateArrival,
                                boolean earlyDeparture, Timestamp time){
        /**
         * This takes an AttendanceRecord object and stores the passed data in the Student's record.
         */
        //TODO: Finish recordAttendance
    }
}


