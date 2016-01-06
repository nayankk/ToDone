package xc0ffee.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

public class TodoDatabase {

    private final Context mContext;
    private DatabaseHelper mDbHelper = null;
    private SQLiteDatabase mDb = null;

    public static final String KEY_ID = "_id";
    public static final String KEY_TODO = "todo";
    public static final String KEY_STATUS = "status";
    public static final String DB_TABLE_NAME = "todo_table";

    private static final String[] PROJECTION = new String[]{
            KEY_ID,
            KEY_TODO,
            KEY_STATUS
    };

    private static class DatabaseHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "todo_db";
        private static final int DB_VERSION = 1;

        private static final String DB_CREATE =
                "CREATE TABLE IF NOT EXISTS " + DB_TABLE_NAME +
                        " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        KEY_TODO + "," +
                        KEY_STATUS + ");";

        private static final String DB_DROP =
                "DROP TABLE IF EXISTS " + DB_TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DB_DROP);
            db.execSQL(DB_CREATE);
        }
    }

    public TodoDatabase(Context context) {
        mContext = context;
    }

    public TodoDatabase open() throws SQLException {
        mDbHelper = new DatabaseHelper(mContext);
        if (mDbHelper != null)
            mDb = mDbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
            mDb = null;
        }
    }

    public long insert(TodoItem item) {
        ContentValues values = new ContentValues();
        values.put(KEY_TODO, item.getTodoText());
        values.put(KEY_STATUS, item.getStatus().ordinal());
        return mDb.insert(DB_TABLE_NAME, null, values);
    }

    public Cursor getTodos() throws SQLException {
        Cursor cursor = mDb.query(DB_TABLE_NAME, PROJECTION, null, null, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}
