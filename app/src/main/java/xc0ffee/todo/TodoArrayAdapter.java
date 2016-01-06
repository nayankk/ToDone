package xc0ffee.todo;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

public class TodoArrayAdapter extends ArrayAdapter<String> {

    public TodoArrayAdapter(Context context, int resource) {
        super(context, resource);
    }

    public TodoArrayAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }
}
