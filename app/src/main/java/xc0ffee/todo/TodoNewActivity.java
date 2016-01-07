package xc0ffee.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TodoNewActivity extends AppCompatActivity {

    private EditText mTaskName;
    private EditText mDescription;
    private EditText mDatePicker;
    private CheckBox mCbHigh;
    private CheckBox mCbMed;
    private CheckBox mCbLow;

    private String mTask = null;
    private String mDesc = null;
    private String mDate = null;
    private TodoItem.Priority mPriority = TodoItem.Priority.PRIOROTY_MEDIUM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_new);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTaskName = (EditText) findViewById(R.id.task_name);
        mDescription = (EditText) findViewById(R.id.desc);

        mDatePicker = (EditText) findViewById(R.id.due_date);
        final Calendar newCalendar = Calendar.getInstance();
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TodoNewActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        TodoNewActivity.this.onDateSet(newDate);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mCbHigh = (CheckBox) findViewById(R.id.cb_high);
        mCbHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbMed.setChecked(false);
                mCbLow.setChecked(false);
            }
        });

        mCbMed = (CheckBox) findViewById(R.id.cb_med);
        mCbMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbLow.setChecked(false);
                mCbHigh.setChecked(false);
            }
        });

        mCbLow = (CheckBox) findViewById(R.id.cb_low);
        mCbLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCbHigh.setChecked(false);
                mCbMed.setChecked(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                commitData();
                if (unsavedChangesExists())
                    showExitConfirmation();
                else
                    exitApp(RESULT_CANCELED);
                return true;
            case R.id.action_save:
                save();
                return true;
        }
        return false;
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(this)
                .setMessage(R.string.exit_conf)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        exitApp(RESULT_CANCELED);
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public void onBackPressed() {
        commitData();
        if (unsavedChangesExists())
            showExitConfirmation();
        else
            exitApp(RESULT_CANCELED);
    }

    private void exitApp(int result) {
        Intent data = new Intent();
        if (result == RESULT_OK) {
            data.putExtra(ToDoMainActivity.KEY_TASK_NAME, mTask);
            data.putExtra(ToDoMainActivity.KEY_TASK_DESC, mDesc);
            data.putExtra(ToDoMainActivity.KEY_TASK_DATE, mDate);
            data.putExtra(ToDoMainActivity.KEY_TASK_PRIO, mPriority.ordinal());
        }
        setResult(result, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_task, menu);
        return true;
    }

    private void save() {
        commitData();
        if (mTask.isEmpty()) {
            final Dialog dialog = new AlertDialog.Builder(this)
                    .setMessage(R.string.no_task_name)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        } else {
            exitApp(RESULT_OK);
        }
    }

    private void onDateSet(Calendar newDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        mDatePicker.setText(formatter.format(newDate.getTime()));
    }

    private void commitData() {
        mTask = mTaskName.getText().toString();
        mDesc = mDescription.getText().toString();
        mDate = mDatePicker.getText().toString();
        mPriority = (mCbHigh.isChecked() ? TodoItem.Priority.PRIOROTY_HIGH :
                (mCbLow.isChecked() ? TodoItem.Priority.PRIOROTY_MEDIUM :
                        TodoItem.Priority.PRIOROTY_LOW));
    }

    private boolean unsavedChangesExists() {
        if (!mTask.isEmpty() || !mDate.isEmpty() || !mDesc.isEmpty())
            return true;
        return false;
    }
}