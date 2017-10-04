package net.pinaz993.studenttracker;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ClassListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        InputStream jsonFileStream = getApplicationContext().getResources().openRawResource(R.raw.test_students);
        byte[] buffer = null;
        String jsonString = null;
        JSONObject studentData = null;
        try {
            buffer = new byte[jsonFileStream.available()];
            jsonFileStream.read(buffer);
            jsonFileStream.close();
            jsonString = new String(buffer, "UTF-8");
            studentData = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ClassList studentList = new ClassList(studentData);
        StudentPaneAdapter studentPaneList = new StudentPaneAdapter(getApplicationContext(),
                                                                    studentList.getStudentList());
        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(studentPaneList);

    }
}
