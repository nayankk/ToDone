package xc0ffee.todo;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TodoCursorAdapter extends CursorAdapter {

    public TodoCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.text);
        int index = cursor.getColumnIndexOrThrow(TodoDatabase.KEY_TODO);
        tv.setText(cursor.getString(index));

        TextView prioTv = (TextView) view.findViewById(R.id.prio);
        TodoItem.Priority priority = TodoItem.Priority.values()[
                cursor.getInt(cursor.getColumnIndexOrThrow(TodoDatabase.KEY_PRIO))];
        if (priority == TodoItem.Priority.PRIOROTY_HIGH) {
            prioTv.setText(R.string.high);
            prioTv.setTextColor(ContextCompat.getColor(context, R.color.color_high));
        } else if (priority == TodoItem.Priority.PRIOROTY_LOW) {
            prioTv.setText(R.string.low);
            prioTv.setTextColor(ContextCompat.getColor(context, R.color.color_low));
        } else {
            prioTv.setText(R.string.medium);
            prioTv.setTextColor(ContextCompat.getColor(context, R.color.color_med));
        }
    }

}
