package net.pinaz993.studenttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.cachapa.expandablelayout.ExpandableLayout;

public class ClassListActivity extends AppCompatActivity {
    String classID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        final ExpandableLayout optionContainer = (ExpandableLayout)findViewById(R.id.option_container);
        Button dropMenuBtn = (Button)findViewById(R.id.drop_menu_btn);
        dropMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionContainer.toggle(true);
            }
        });
        Bundle b = getIntent().getExtras();
        classID = (b != null) ? b.getString("classID") : null;
    }
}
