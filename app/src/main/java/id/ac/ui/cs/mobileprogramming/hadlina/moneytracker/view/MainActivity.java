package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityMainBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.MainViewModel;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final int MY_PERMISSIONS_REQUEST_READ_CALENDAR = 5;
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mainViewModel.setEmail(getIntent().getStringExtra("email"));
        mainViewModel.setName(getIntent().getStringExtra("email"));
        binding.setViewmodel(mainViewModel);

        getPrimaryCalendar();
        observe();
    }

    private void observe() {
        mainViewModel.isTransactionClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    mainViewModel.resetTransactionClicked();
                    startActivity(new Intent(getApplicationContext(), ListTransactionActivity.class));
                }
            }
        });

        mainViewModel.getAccount().observe(this, new Observer<Account>() {
            @Override
            public void onChanged(Account account) {
                if(account != null) {
                    binding.setAmount(String.valueOf(account.getAmount()));
                }
            }
        });
        mainViewModel.isDebtClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    mainViewModel.resetDebtClicked();
                    if (isFinished()) {
                        startActivity(new Intent(getApplicationContext(), TimeoutActivity.class));
                        return;
                    }
                    startActivity(new Intent(getApplicationContext(), ListDebtActivity.class));
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

    @Override
    public void onBackPressed() {
        Intent exit = new Intent(Intent.ACTION_MAIN);
        exit.addCategory(Intent.CATEGORY_HOME);
        exit.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(exit);
    }

    public void onAddButtonClicked(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setOnMenuItemClickListener(this);
        inflater.inflate(R.menu.menu_add_new, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_newTransaction:
                startActivity(new Intent(this, CreateTransactionActivity.class));
                return true;
            case R.id.item_newDebt:
                startActivity(new Intent(this, CreateDebtActivity.class));
                return true;
            default: return false;
        }
    }

    public void getPrimaryCalendar() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, MY_PERMISSIONS_REQUEST_READ_CALENDAR);
        }
        final String[] EVENT_PROJECTION = new String[]{
                CalendarContract.Calendars._ID,
                CalendarContract.Calendars.ACCOUNT_NAME,
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                CalendarContract.Calendars.OWNER_ACCOUNT
        };

        final int PROJECTION_ID_INDEX = 0;
        final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        final int PROJECTION_DISPLAY_NAME_INDEX = 2;
        final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        ContentResolver contentResolver = getContentResolver();
        String selection = CalendarContract.Calendars.VISIBLE + " = 1 AND " + CalendarContract.Calendars.IS_PRIMARY + "=1";
        Cursor cur = contentResolver.query(CalendarContract.Calendars.CONTENT_URI, EVENT_PROJECTION, selection, null, null);

        if (cur != null && cur.getCount() > 0) {
            cur.moveToNext();
            long calID;
            String displayName;
            String accountName;
            String ownerName;

            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("calendarID", calID);
            editor.putString("displayName", displayName);
            editor.putString("accountName", accountName);
            editor.putString("ownerName", ownerName);
            editor.apply();
        }
        if (cur != null) cur.close();

    }
}
