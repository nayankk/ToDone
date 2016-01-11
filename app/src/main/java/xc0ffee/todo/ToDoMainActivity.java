package xc0ffee.todo;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ToDoMainActivity extends AppCompatActivity
        implements TodoCursorAdapter.OnDeleteListItem,
        TodoNewDialogFragment.TodoEditCompleteListener {

    public final static String KEY_TASK_NAME = "task-name";
    public final static String KEY_TASK_DESC = "task-desc";
    public final static String KEY_TASK_DATE = "due-date";
    public final static String KEY_TASK_PRIO = "task-prio";
    public final static String KEY_ID = "position";
    public final static String KEY_TITLE = "title";

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

        /* Todo: Do all db operations outside main thead */
        try {
            mTodoDb = new TodoDatabase(this).open();
            mCursor = mTodoDb.getTodos();
        } catch (SQLException e) {
            // Ignore!
        }

        mAdapter = new TodoCursorAdapter(this, mCursor, this);
        listview.setAdapter(mAdapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
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
    protected void onDestroy() {
        super.onDestroy();
        mTodoDb.close();
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
        data.putString(KEY_TASK_DESC, cursor.getString(
                cursor.getColumnIndexOrThrow(TodoDatabase.KEY_DESC)));
        data.putString(KEY_TASK_DATE, cursor.getString(
                cursor.getColumnIndexOrThrow(TodoDatabase.KEY_DUE)));
        data.putInt(KEY_TASK_PRIO, cursor.getInt(
                cursor.getColumnIndexOrThrow(TodoDatabase.KEY_PRIO)));
        data.putInt(KEY_ID, cursor.getInt(
                cursor.getColumnIndexOrThrow(TodoDatabase.KEY_ID)));
        data.putString(KEY_TITLE, getResources().getString(R.string.edit_task));
        TodoNewDialogFragment fragment = TodoNewDialogFragment.getNewInstance(data, this);
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, "fragment_edit_name");
    }

    private void launchTodoEditActivity() {
        Bundle data = new Bundle();
        data.putString(KEY_TASK_NAME, new String());
        data.putString(KEY_TASK_DATE, new String());
        data.putString(KEY_TASK_DESC, new String());
        data.putInt(KEY_TASK_PRIO, TodoItem.Priority.PRIOROTY_MEDIUM.ordinal());
        data.putString(KEY_TITLE, getResources().getString(R.string.new_task));

        TodoNewDialogFragment fragment = TodoNewDialogFragment.getNewInstance(data, this);
        FragmentManager fm = getSupportFragmentManager();
        fragment.show(fm, "fragment_edit_name");
    }

    @Override
    public void onDeleteListItem(final int id) {
        final ScheduledExecutorService worker =
                Executors.newSingleThreadScheduledExecutor();
        worker.schedule(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mTodoDb.delete(id);
                        updateCursor();
                    }
                });
            }
        }, 1, TimeUnit.SECONDS);
    }

    @Override
    public void onNewTodoCreated(TodoItem item) {
        mTodoDb.insert(item);
        updateCursor();
    }

    @Override
    public void onTodoUpdated(int id, TodoItem item) {
        mTodoDb.update(id, item);
        updateCursor();
    }
}
