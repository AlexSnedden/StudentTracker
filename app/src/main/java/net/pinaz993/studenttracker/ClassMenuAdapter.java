package net.pinaz993.studenttracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by Patrick Shannon on 11/8/2017.
 */

public class ClassMenuAdapter extends ArrayAdapter {
    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     */
    public ClassMenuAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
