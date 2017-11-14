package net.pinaz993.studenttracker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import net.cachapa.expandablelayout.ExpandableLayout;

public class CollapseTest extends AppCompatActivity {
    static final String[] FRUITS = new String[] { "Apple", "Avocado", "Banana",
            "Blueberry", "Coconut", "Durian", "Guava", "Kiwifruit",
            "Jackfruit", "Mango", "Olive", "Pear", "Sugar-apple" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapse_test);
        final ExpandableLayout menuContainer = (ExpandableLayout)findViewById(R.id.option_container);

        ListView optionsList = (ListView)findViewById(R.id.options_list);
        optionsList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FRUITS));

        Button dropMenuBtn = (Button) findViewById(R.id.drop_menu_btn);
        dropMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuContainer.toggle(true);
            }
        });

        ListView list = (ListView)findViewById(R.id.elements_list);
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FRUITS));

    }
}
