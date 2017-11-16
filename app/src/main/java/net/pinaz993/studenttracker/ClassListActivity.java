package net.pinaz993.studenttracker;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class ClassListActivity extends AppCompatActivity implements
        ExpandableLayout.OnExpansionUpdateListener, AdapterView.OnItemClickListener, View.OnClickListener {
    private String classID;
    private DatabaseHandler dbh = DatabaseHandler.getInstance();
    private SettingsHandler settings = SettingsHandler.getInstance();
    private ExpandableLayout optionContainer, studentListContainer;
    Button dropMenuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(classID);

        Bundle extras = getIntent().getExtras();
        classID = (extras != null) ? extras.getString("CLASS_ID_KEY") : null;
        if (classID == null) {
            // activate the class selector dialog, use it to return a valid classID
        }

        optionContainer = (ExpandableLayout) findViewById(R.id.option_container);
        optionContainer.setOnExpansionUpdateListener(this);

        studentListContainer = (ExpandableLayout)findViewById(R.id.student_list_container);

        ListView studentList = (ListView) findViewById(R.id.student_list);
        Student[] students = getStudentsInClass();
        StudentPaneAdapter studentPaneAdapter = new StudentPaneAdapter(this, students);
        studentList.setAdapter(studentPaneAdapter);

        String[] classes = getAllClasses();
        ListView optionsList = (ListView) findViewById(R.id.options_list);
        optionsList.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, classes));
        optionsList.setOnItemClickListener(this);

        settings.setLastActivityRun(SettingsHandler.ACTIVITY.CLASS_LIST);
        settings.setLastClassID(classID);
    }

    //<editor-fold desc="Get Stuff From Database">
    private String[] getAllClasses(){
        Cursor c = dbh.getAllClasses();
        c.moveToFirst();
        ArrayList<String> classes = new ArrayList<>();
        do{
            String newClass = c.getString(c.getColumnIndex(DatabaseHandler.StudentClassMappingTableSchema.CLASS_ID_COL));
            classes.add(newClass);
        } while (c.moveToNext());
        c.close();
        return classes.toArray(new String[]{});
    }

    private Student[] getStudentsInClass(){
        Cursor c = dbh.getStudentsInClass(classID);
        Student[] students = new Student[c.getCount()];
        while (c.moveToNext()) {
            String studentID = c.getString(c.getColumnIndex(
                    DatabaseHandler.StudentClassMappingTableSchema.STUDENT_ID_COL));
            students[c.getPosition()] = dbh.retrieveStudent(studentID);
        }
        c.close();
        return students;
    }
    //</editor-fold>

    //<editor-fold desc="Listener Callbacks">
    @Override
    public void onExpansionUpdate(float expansionFraction, int state){
        dropMenuButton.setRotation((int)(expansionFraction * 180));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.class_list_menu, menu);
        MenuItem actionList = menu.findItem(R.id.action_list);
        actionList.setActionView(R.layout.arrow);
        dropMenuButton = (Button) actionList.getActionView();
        dropMenuButton.setOnClickListener(this);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onClick(View v) {
        optionContainer.toggle(true);
        studentListContainer.toggle(true);
    }
    //</editor-fold>
}
