package net.pinaz993.studenttracker;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Patrick Shannon on 8/30/2017.
 */

public class ClassList {
    /**
     * A list of students in a single class.
     */
    final static String BOOK_ID = "ClassLists";
    private String classListID;
    private transient ArrayList<Student> students;
    private ArrayList<String> studentIDs;

    ClassList(String classListID) {
        this.classListID = classListID;
        this.students = new ArrayList<Student>();
        this.studentIDs = new ArrayList<String>();
    }

    public void importStudents(String csvStudentIDData) {
        CharArrayReader csvStudentIDDataReader = new CharArrayReader(csvStudentIDData.toCharArray());
    }

    public void addStudent(Student student) {
        students.add(student);
        studentIDs.add(student.getStudentID());
    }

    public void removeStudent(Student student) {
        /* assumes student points to the same object that needs to be removed */
        Iterator<Student> studentsIterator = students.listIterator();
        Student currentStudent;
        while(studentsIterator.hasNext()) {
            currentStudent = studentsIterator.next();
            if(currentStudent == student) {
                students.remove(currentStudent);
                break;
            }
        }
    }

    public void populateStudents() {
        /*
        read each student from disk and insert them into transient student array list
         */
    }

}