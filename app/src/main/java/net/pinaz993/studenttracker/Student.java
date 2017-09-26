package net.pinaz993.studenttracker;
import io.paperdb.Paper;


/**
 * Created by Patrick Shannon on 8/30/2017.
 * This packacge is for the Student Object, and all related objects.
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
    }
    /**
     * Takes attendance data and records it in storage
    public void recordAttendance(){
            //TODO: Replace with SQL implementation, because querying
    }

    public void compileAttendanceSummary(){
        /*
         * This is a method because we don't wish to perform each time student object is created.
         */

    public boolean isDelinquent() {return delinquent;}

    public void setDelinquent(boolean delinquent) {this.delinquent = delinquent;}


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


class AttendanceSummary {
    int daysAbsent;
    int daysPresent;
    int lateArrivals;
    int earlyDepartures;

    AttendanceSummary() {}
}