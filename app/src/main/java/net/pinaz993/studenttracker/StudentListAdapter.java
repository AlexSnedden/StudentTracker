package net.pinaz993.studenttracker;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.chauthai.swipereveallayout.ViewBinderHelper;

/**
 * Created by Patrick Shannon on 9/20/2017.
 *
 */

public class StudentListAdapter extends ArrayAdapter<String> {
    private Student[] students;

    public StudentListAdapter(Context context, Student[] students) {
        super(context, R.layout.student_pane_template);
        System.out.println("Constructor Called for StudentListAdapter");
        this.students = students;
        ViewBinderHelper binderHelper = new ViewBinderHelper();
        binderHelper.setOpenOnlyOne(true);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return new StudentPane(this.getContext(), students[position]);
    }
}