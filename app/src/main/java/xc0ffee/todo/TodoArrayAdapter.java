package xc0ffee.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TodoArrayAdapter extends ArrayAdapter<String> {

    List<String> mItems = new ArrayList<>();

    private final Context mContext;

    public TodoArrayAdapter(Context context, List<String> list) {
        super(context, 0);
        mItems.addAll(list);

        mContext = context;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public String getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getPosition(String item) {
        /* Todo: Ensure duplicate strings are not added to the list */
        return mItems.indexOf(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView != null && convertView instanceof TextView)
            return convertView;

        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.list_item, parent, false);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText(getItem(position));
        return view;
    }

    @Override
    public void add(String item) {
        mItems.add(item);
        notifyDataSetChanged();
    }
}
