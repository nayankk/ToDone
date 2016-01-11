package xc0ffee.todo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TodoNewDialogFragment extends DialogFragment {

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

    private int mId = -1;

    private TodoEditCompleteListener mListener = null;

    public interface TodoEditCompleteListener {
        void onNewTodoCreated(TodoItem item);
        void onTodoUpdated(int id, TodoItem item);
    }

    public TodoNewDialogFragment() {
    }

    public static TodoNewDialogFragment getNewInstance(Bundle data, TodoEditCompleteListener listener) {
        TodoNewDialogFragment frag = new TodoNewDialogFragment();
        frag.setActionCompletionListener(listener);
        frag.setArguments(data);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.new_todo, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String title = getArguments().getString(ToDoMainActivity.KEY_TITLE,
                getActivity().getResources().getString(R.string.new_task));
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle(title);
        toolbar.inflateMenu(R.menu.menu_new_task);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_save) {
                    save();
                    return true;
                }
                return false;
            }
        });

        Bundle data = getArguments();

        mTaskName = (EditText) view.findViewById(R.id.task_name);
        mTask = data.getString(ToDoMainActivity.KEY_TASK_NAME);
        mTaskName.setText(mTask);
        mTaskName.requestFocus();

        mDescription = (EditText) view.findViewById(R.id.desc);
        mDesc = data.getString(ToDoMainActivity.KEY_TASK_DESC);
        mDescription.setText(mDesc);

        mDatePicker = (EditText) view.findViewById(R.id.due_date);
        mDate = data.getString(ToDoMainActivity.KEY_TASK_DATE);
        mDatePicker.setText(mDate);
        final Calendar newCalendar = Calendar.getInstance();
        mDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        TodoNewDialogFragment.this.onDateSet(newDate);
                    }
                }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        mCbHigh = (CheckBox) view.findViewById(R.id.cb_high);
        mCbHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCbHigh();
            }
        });

        mCbMed = (CheckBox) view.findViewById(R.id.cb_med);
        mCbMed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCbMed();
            }
        });

        mCbLow = (CheckBox) view.findViewById(R.id.cb_low);
        mCbLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCbLow();
            }
        });

        mPriority = TodoItem.Priority.values()[data.getInt(
                ToDoMainActivity.KEY_TASK_PRIO, TodoItem.Priority.PRIOROTY_MEDIUM.ordinal())];
        if (mPriority == TodoItem.Priority.PRIOROTY_HIGH)
            setCbHigh();
        else if (mPriority == TodoItem.Priority.PRIOROTY_MEDIUM)
            setCbMed();
        else
            setCbLow();

        mId = data.getInt(ToDoMainActivity.KEY_ID, -1);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                if (unsavedChangesExists())
                    showExitConfirmation();
                else
                    dismiss();
            }
        };
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        super.onResume();
    }

    private void setActionCompletionListener(TodoEditCompleteListener listener) {
        mListener = listener;
    }

    private void setCbHigh() {
        mCbHigh.setChecked(true);
        mCbMed.setChecked(false);
        mCbLow.setChecked(false);
    }

    private void setCbMed() {
        mCbHigh.setChecked(false);
        mCbMed.setChecked(true);
        mCbLow.setChecked(false);
    }

    private void setCbLow() {
        mCbHigh.setChecked(false);
        mCbMed.setChecked(false);
        mCbLow.setChecked(true);
    }

    private void onDateSet(Calendar newDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        mDatePicker.setText(formatter.format(newDate.getTime()));
    }

    private void save() {
        commitData();
        if (mTask.isEmpty()) {
            final Dialog dialog = new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.no_task_name)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            dialog.show();
        } else {
            TodoItem item = new TodoItem.Builder(mTask)
                    .description(mDesc)
                    .dueDate(mDate)
                    .priority(TodoItem.Priority.values()[mPriority.ordinal()])
                    .build();
            if (mId == -1)
                mListener.onNewTodoCreated(item);
            else
                mListener.onTodoUpdated(mId, item);
            dismiss();
        }
    }

    private void commitData() {
        mTask = mTaskName.getText().toString();
        mDesc = mDescription.getText().toString();
        mDate = mDatePicker.getText().toString();
        mPriority = (mCbHigh.isChecked() ? TodoItem.Priority.PRIOROTY_HIGH :
                (mCbLow.isChecked() ? TodoItem.Priority.PRIOROTY_LOW :
                        TodoItem.Priority.PRIOROTY_MEDIUM));
    }

    private boolean unsavedChangesExists() {
        if (!mTask.equals(mTaskName.getText().toString()) ||
                !mDesc.equals(mDescription.getText().toString()) ||
                !mDate.equals(mDatePicker.getText().toString()))
            return true;
        return false;
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(getActivity())
                .setMessage(R.string.exit_conf)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                }).setNegativeButton("No", null).show();
    }
}
