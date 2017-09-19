package net.pinaz993.studenttracker;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import io.paperdb.Paper;

import static android.provider.Settings.Global.getString;

/**
 * Created by Patrick Shannon on 8/30/2017.
 * This packacge is for the Student Object, and all related objects.
 */

public class Student {
    private final static String BOOK_ID = "students";
    private final String firstName;
    private final String lastName;
    private final String email;
    final String studentID;
    private boolean delinquent; // condition for read formatting on the student's name
    private ArrayList<AttendanceRecord> attendanceRecords;
    private transient AttendanceSummary summary;

    public static Student retrieve(String studentID) {
        return Paper.book(BOOK_ID).read(studentID);
    }

    /**
     *
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
        this.attendanceRecords = new ArrayList<>();
        this.summary = null;
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
        this.attendanceRecords = new ArrayList<>();
        this.summary = null;
    }
    /**
     * Takes attendance data and records it in storage
     * @param present If the student was there at all
     * @param lateArrival If the student arrived late to class
     * @param earlyDeparture If the student left class early
     */
    public void recordAttendance(boolean present, boolean lateArrival, boolean earlyDeparture){
        this.attendanceRecords.add(new AttendanceRecord(present, lateArrival, earlyDeparture));
    }

    public void recordDatedAttendance(boolean present, boolean lateArrival, boolean earlyDeparture,
                                      Date timestamp) {
        AttendanceRecord record = new AttendanceRecord(timestamp, present, lateArrival,
                                                       earlyDeparture);
        this.attendanceRecords.add(record);
    }

    public void compileAttendanceSummary(){
        /*
         * This is a method because we don't wish to perform each time student object is created.
         */
        this.summary = new AttendanceSummary(this.attendanceRecords);
    }

    public boolean isDelinquent() {
        return delinquent;
    }

    public void setDelinquent(boolean delinquent) {
        this.delinquent = delinquent;
    }

    public void save() {
        Paper.book(BOOK_ID).write(studentID, this);
    }

    public String getFirstName() {return firstName;}

    public String getLastName() {return lastName;}

    public String getEmail() {return email;}

    public String getStudentID() {return studentID;}

    public String getFullName() {return firstName + " " + lastName;}

    //TODO: Write a record replacement method in case of inaccuracies
}

class AttendanceRecord {
    //TODO: How do we know what class the student was present/absent for?
    private Date timestamp;
    boolean present, lateArrival, earlyDeparture;

    AttendanceRecord(boolean present, boolean lateArrival, boolean earlyDeparture) {
        this.present = present;
        this.lateArrival = lateArrival;
        this.earlyDeparture = earlyDeparture;
        this.timestamp = new Date();
    }

    AttendanceRecord(Date timestamp, boolean present, boolean lateArrival, boolean earlyDeparture) {
        this.timestamp = timestamp;
        this.present = present;
        this.lateArrival = lateArrival;
        this.earlyDeparture = earlyDeparture;
    }
}

class AttendanceSummary {
    int daysAbsent;
    int daysPresent;
    int lateArrivals;
    int earlyDepartures;

    AttendanceSummary(ArrayList<AttendanceRecord> attendanceRecords){
        Iterator<AttendanceRecord> I = attendanceRecords.listIterator();
        AttendanceRecord currentRecord;
        while(I.hasNext()) {
            currentRecord = I.next();
            if(currentRecord.present) daysPresent++;
            else daysAbsent++;
            if(currentRecord.lateArrival) lateArrivals++;
            if(currentRecord.earlyDeparture) earlyDepartures++;
        }
    }
}