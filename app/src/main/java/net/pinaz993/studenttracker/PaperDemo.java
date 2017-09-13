package net.pinaz993.studenttracker;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class PaperDemo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_demo);

        final EditText wordInput = (EditText)findViewById(R.id.wordInput);
        final EditText integerInput = (EditText)findViewById(R.id.integerInput);
        final EditText decimalInput = (EditText)findViewById(R.id.decimalInput);
        final EditText nameInput = (EditText)findViewById(R.id.nameInput);
        final Button createBtn = (Button)findViewById(R.id.createBtn);
        final Snackbar success = Snackbar.make(createBtn, "Object created.", Snackbar.LENGTH_SHORT);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameInput.getText().toString();
                String word = wordInput.getText().toString();
                Integer integer = Integer.parseInt(integerInput.getText().toString());
                Double decimal = Double.parseDouble(decimalInput.getText().toString());

                PaperTest object = new PaperTest(integer, decimal, word, name);
                success.show();
            }
        });
    }
}
