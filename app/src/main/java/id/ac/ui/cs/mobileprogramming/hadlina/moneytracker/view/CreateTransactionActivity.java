package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityTransactionCreateBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils.IntentService;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.TransactionViewModel;

public class CreateTransactionActivity extends AppCompatActivity{

    private ActivityTransactionCreateBinding binding;
    private TransactionViewModel viewModel;
    private SharedPreferences preferences;
    Button btnTakePhoto;
    ImageView imageView;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_create);
        startService(new Intent(CreateTransactionActivity.this, IntentService.class));

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_create);
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        viewModel.email.setValue(preferences.getString("email", ""));
        viewModel.negativeBalance.setValue(false);
        btnTakePhoto = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        EnableRuntimePermission();
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 7);
            }
        });

        binding.setViewModel(viewModel);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.categories, R.layout.spinner_category_item);
        adapter.setDropDownViewResource(R.layout.spinner_category_item);
        binding.spinnerCategory.setAdapter(adapter);
        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                if (pos == 0) viewModel.category.setValue(null);
                else viewModel.category.setValue(adapterView.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        observe();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(CreateTransactionActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(CreateTransactionActivity.this,"access camera permission", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(CreateTransactionActivity.this,new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] result) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (result.length > 0 && result[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(CreateTransactionActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CreateTransactionActivity.this, "Can't access camera", Toast.LENGTH_LONG).show();
                }
                break;
        }
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

    private void observe() {
        viewModel.newTransaction().observe(this, new Observer<Transaction>() {
            @Override
            public void onChanged(Transaction transaction) {
                if (transaction != null) {
                    Intent intent = new Intent(getApplicationContext(), DetailTransactionActivity.class);
                    intent.putExtra("transaction", transaction);
                    startActivity(intent);
                    finish();
                    stopService(new Intent(CreateTransactionActivity.this, IntentService.class));
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

                    new DatePickerDialog(CreateTransactionActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            String date = day + " " + viewModel.monthToString(month) + " " + year;
                            viewModel.date.setValue(date);
                            binding.edTransactionDate.setText(date);
                        }

                    }, year, month, day).show();
                }
            }
        });

        viewModel.negativeBalance.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean negative) {
                if (negative) {
                    Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_transaction_create), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
