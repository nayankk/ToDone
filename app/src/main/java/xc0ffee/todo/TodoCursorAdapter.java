package xc0ffee.todo;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class TodoCursorAdapter extends CursorAdapter {

    private final OnDeleteListItem mListener;

    public interface OnDeleteListItem {
        void onDeleteListItem(int id);
    }

    public TodoCursorAdapter(Context context, Cursor cursor, OnDeleteListItem listener) {
        super(context, cursor, 0);
        mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        v.findViewById(R.id.strike_through).setVisibility(View.INVISIBLE);
        return v;
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

        Integer id = cursor.getInt(cursor.getColumnIndexOrThrow(TodoDatabase.KEY_ID));
        AppCompatCheckBox cb = (AppCompatCheckBox) view.findViewById(R.id.del_cb);
        ViewHolder vh = new ViewHolder(id, view);
        cb.setTag(vh);
        cb.setChecked(false);
        view.findViewById(R.id.strike_through).setVisibility(View.INVISIBLE);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewHolder vh = (ViewHolder) v.getTag();
                if (mListener != null && vh != null) {
                    mListener.onDeleteListItem(vh.getId());
                    vh.getView().findViewById(R.id.strike_through).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private static class ViewHolder {
        private View view;
        private int id;

        public ViewHolder(int id, View view) {
            this.view = view;
            this.id = id;
        }

        public  View getView() {
            return view;
        }

        public int getId() {
            return id;
        }
    }
}
