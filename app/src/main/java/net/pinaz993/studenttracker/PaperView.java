package net.pinaz993.studenttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PaperView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_view);

        final EditText nameInput = (EditText)findViewById(R.id.nameInput);
        final Button viewBtn = (Button)findViewById(R.id.viewBtn);
        final TextView wordView = (TextView)findViewById(R.id.wordView);
        final TextView integerView = (TextView)findViewById(R.id.integerView);
        final TextView decimalView = (TextView)findViewById(R.id.decimalView);

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PaperTest object = PaperTest.getObject(nameInput.getText().toString());
                wordView.setText(object.getWord());
                integerView.setText(object.getInteger());
                decimalView.setText(object.getDecimal().toString());
            }
        });
    }
}
