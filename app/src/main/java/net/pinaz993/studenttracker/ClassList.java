package net.pinaz993.studenttracker;

import java.util.ArrayList;
import java.util.Iterator;

import io.paperdb.Paper;

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

    public static ClassList retrieve(String classListID){
        /*
        grab the classList from Paper with this ID
         */
        return Paper.book(BOOK_ID).read(classListID);
    }

    public void save(){
        /*
        save this object to disk with array list of student ids
         */
        Paper.book(BOOK_ID).write(this.classListID, this);
    }

    public void compileStudents() {
        /*
        read each student from disk and insert them into transient student array list
         */
        Iterator<String> studentIDsIterator = studentIDs.listIterator();
        while(studentIDsIterator.hasNext()) {
            students.add(Student.retrieve(studentIDsIterator.next()));
        }
    }

    public void putToBed(){
        /*
        serialize each student, committing student IDs to memory in array list
        Kill student objects
        Kill transient array list of student objects
        serialize this
        suicide

         */

        Iterator<Student> studentsIterator = students.listIterator();
        Student student;
        while(studentsIterator.hasNext()) {
            student = studentsIterator.next();
            studentIDs.add(student.studentID);
            student.save();
        }
        // ensure student pointer is null as well as pointer to array list of students, then
        // call garbage collector on them.
        student = null;
        students = null;
        System.gc();
        save();
    }

}
