package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityDebtCreateBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils.IntentService;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.DebtViewModel;

public class CreateDebtActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 1;
    private ActivityDebtCreateBinding binding;
    private DebtViewModel viewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_create);
        startService(new Intent(CreateDebtActivity.this, IntentService.class));

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_debt_create);
        viewModel = ViewModelProviders.of(this).get(DebtViewModel.class);

        binding.setViewModel(viewModel);
        viewModel.email.setValue(preferences.getString("email", ""));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        }

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        observe();
    }

    private void observe() {
        viewModel.newDebt().observe(this, new Observer<Debt>() {
            @Override
            public void onChanged(Debt debt) {
                if (debt != null) {
                    createEvent(debt);
                    Intent intent = new Intent(getApplicationContext(), DetailDebtActivity.class);
                    intent.putExtra("debt", debt);
                    startActivity(intent);
                    finish();
                    stopService(new Intent(CreateDebtActivity.this, IntentService.class));
                }
            }
        });

        viewModel.isSelectDateClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    viewModel.resetSelectDateClicked();

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    new DatePickerDialog(CreateDebtActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String date = day + " " + viewModel.monthToString(month+1) + " " + year;
                            viewModel.dueDate.setValue(date);
                            binding.edDebtDate.setText(date);
                        }
                    }, year, month, day).show();
                }
            }
        });
    }

    private boolean isFinished() {
        return preferences.getBoolean("timeout", false);
    }

    @Override
    protected void onResume() {
        if (isFinished()) {
            startActivity(new Intent(this, TimeoutActivity.class));
        }
        super.onResume();
    }

    private void createEvent(Debt debt) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
        }

        String[] date = debt.getDueDate().split(" ");
        long calID = preferences.getLong("calendarID", 0);
        long duration;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(Integer.parseInt(date[2]), viewModel.monthToInt(date[1]) - 1, Integer.parseInt(date[0]));
        duration = beginTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.ALL_DAY, 1);
        values.put(CalendarContract.Events.DTSTART, duration);
        values.put(CalendarContract.Events.DTEND, duration);
        values.put(CalendarContract.Events.TITLE, debt.getTitle());
        values.put(CalendarContract.Events.DESCRIPTION, "Deadline for debt to " + debt.getCreditor());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(CalendarContract.Events.ORGANIZER, preferences.getString("accountName", "Anonymous"));
        values.put(CalendarContract.Events.SELF_ATTENDEE_STATUS, CalendarContract.Events.STATUS_CONFIRMED);
        values.put(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_FREE);

        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        long eventID = Long.parseLong(uri.getLastPathSegment());
        Log.i("CalendarProvider", "Event Created, the event id is: " + eventID);
        createReminder(eventID, values, cr);
    }

    private void createReminder(long eventID, ContentValues values, ContentResolver cr) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_CALENDAR}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);

        } else {
            values.clear();
            values.put(CalendarContract.Reminders.EVENT_ID, eventID);
            values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
            values.put(CalendarContract.Reminders.MINUTES, 18*60);

            Uri uri = cr.insert(CalendarContract.Reminders.CONTENT_URI, values);
            long reminderID = Long.parseLong(uri.getLastPathSegment());
            Log.i("CalendarProvider", "Reminder Created, the reminder id is: " + reminderID);
        }
    }
}
