package net.pinaz993.studenttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Paper.init(this);
    }
}
