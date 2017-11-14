package net.pinaz993.studenttracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Patrick Shannon on 11/14/2017.
 */

public class BehaviorListBox extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View content = inflater.inflate(R.layout.behavior_box_list_template, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.behavior_box_title);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Implement onClick for positive button
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //TODO: Implement onCLick for negative button
            }
        });

        builder.setView(content);
        ListView positiveBehaviorList = content.findViewById(R.id.behaviors_positive),
                neutralBehaviorList = content.findViewById(R.id.behaviors_neutral),
                negativeBehaviorList = content.findViewById(R.id.behaviors_negetive);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Behavior behavior = (Behavior) parent.getAdapter().getItem(position);
                behavior.checked = !behavior.checked;
                BehaviorAdapter adapter = (BehaviorAdapter) parent.getAdapter();
                adapter.notifyDataSetChanged();
            }
        };

        positiveBehaviorList.setOnItemClickListener(listener);
        negativeBehaviorList.setOnItemClickListener(listener);
        neutralBehaviorList.setOnItemClickListener(listener);

        positiveBehaviorList.setAdapter(new BehaviorAdapter(getActivity(), Behavior.Positivity.POSITIVE, positiveBehaviorList));
        neutralBehaviorList.setAdapter(new BehaviorAdapter(getActivity(), Behavior.Positivity.NEUTRAL, neutralBehaviorList));
        negativeBehaviorList.setAdapter(new BehaviorAdapter(getActivity(), Behavior.Positivity.NEGATIVE, negativeBehaviorList));

        return builder.create();
    }


}

class BehaviorAdapter extends ArrayAdapter{
    Context context;
    private static DatabaseHandler dbh = DatabaseHandler.getInstance();

    /**
     * Constructor
     *
     * @param context  The current context.
     */
    public BehaviorAdapter(@NonNull Context context, Behavior.Positivity  positivity, ViewGroup parent) {
        super(context, R.layout.behavior_box_row_item, collateBehaviorsByPositivity(positivity, context));
        this.context = context;
        if(getCount() == 0) parent.setVisibility(View.INVISIBLE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        final View result;

        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.behavior_box_row_item, parent, false);

            holder.behaviorNameTxt = convertView.findViewById(R.id.behavior_name_txt);
            holder.behaviorSelectedCheckbox = convertView.findViewById(R.id.behavior_selected_checkbox);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Behavior behavior = (Behavior) getItem(position);
        if (behavior == null) throw new IllegalStateException();
        holder.behaviorNameTxt.setText(behavior.behaviorName);
        holder.behaviorSelectedCheckbox.setChecked(behavior.checked);

        return result;
    }

    public static Behavior[] collateBehaviorsByPositivity(Behavior.Positivity pos, Context context) {
        Behavior[] b = dbh.getBehaviorObjects(context);
        ArrayList<Behavior> rtn = new ArrayList<Behavior>();
        for (Behavior aB : b) {
            if (aB.behaviorPositivity == pos) rtn.add(aB);
        }
        return rtn.toArray(new Behavior[]{});
    }

    private class ViewHolder{
        CheckBox behaviorSelectedCheckbox;
        TextView behaviorNameTxt;
    }
}
