package xc0ffee.todo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;

public class ToDoMainActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 1;

    public final static String KEY_TASK_NAME = "task-name";
    public final static String KEY_TASK_DESC = "task-desc";
    public final static String KEY_TASK_DATE = "due-date";
    public final static String KEY_TASK_PRIO = "task-prio";

    private TodoDatabase mTodoDb = null;
    private TodoCursorAdapter mAdapter = null;
    private Cursor mCursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView listview = (ListView) findViewById(R.id.list_view);

        /* Todo: Do all db operations in AsyncTask */
        try {
            mTodoDb = new TodoDatabase(this).open();
            mCursor = mTodoDb.getTodos();
        } catch (SQLException e) {
            // Ignore!
        }

        mAdapter = new TodoCursorAdapter(this, mCursor);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = (Cursor) mAdapter.getCursor();
                cursor.moveToPosition(position);
                launchTodoEditActivity(cursor);
            }
        });

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTodoEditActivity();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && mTodoDb != null) {
            TodoItem item = new TodoItem.Builder(data.getStringExtra(KEY_TASK_NAME)).
                    description(data.getStringExtra(KEY_TASK_DESC)).
                    dueDate(data.getStringExtra(KEY_TASK_DATE)).
                    priority(TodoItem.Priority.values()[data.getIntExtra(KEY_TASK_PRIO,
                            TodoItem.Priority.PRIOROTY_MEDIUM.ordinal())]).build();
            mTodoDb.insert(item);
            updateCursor();
        }
    }

    private void updateCursor() {
        mCursor.close();
        try {
            mCursor = mTodoDb.getTodos();
            mAdapter.changeCursor(mCursor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void launchTodoEditActivity(Cursor cursor) {
        Bundle data = new Bundle();
        data.putString(KEY_TASK_NAME, cursor.getString(
                cursor.getColumnIndexOrThrow(TodoDatabase.KEY_TODO)));
        Intent intent = new Intent(ToDoMainActivity.this, TodoNewActivity.class);
        intent.putExtras(data);
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void launchTodoEditActivity() {
        Bundle data = new Bundle();
        data.putString(KEY_TASK_NAME, new String());
        Intent intent = new Intent(ToDoMainActivity.this, TodoNewActivity.class);
        intent.putExtras(data);
        startActivityForResult(intent, REQUEST_CODE);
    }
}
