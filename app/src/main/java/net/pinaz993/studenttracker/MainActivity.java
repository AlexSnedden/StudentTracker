package net.pinaz993.studenttracker;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(getApplicationContext());
        ConstraintLayout mainLayout = (ConstraintLayout)findViewById(R.id.mainLayout);

        Student bob = new Student("Bobert", "Roberts", "12345");
//        StudentPane bobpane = new StudentPane(getApplicationContext(), bob);
//        mainLayout.addView(bobpane);



    }
    /* temporary function to test ui with students objects */
    private Student[] generateStudents(int numStudents) {
        ArrayList<Student> students = new ArrayList<>();
        for(int i=0; i < numStudents; i++) {
            students.add(new Student("Richie", "Rich", String.valueOf(i), "richierich@gmail.com"));
        }
        return (Student[])students.toArray();
    }
}
