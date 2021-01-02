package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.TransactionViewModel;

public class CreateTransactionActivity extends AppCompatActivity{

    private ActivityTransactionCreateBinding binding;
    private TransactionViewModel viewModel;
    private SharedPreferences preferences;

    public static final int REQUEST_CAMERA = 10;
    public static final int WRITE_CAMERA = 11;
    public static final int CAPTURE_CAMERA = 12;

    public static final int REQUEST_GALLERY = 13;
    public static final int WRITE_GALLERY = 14;
    public static final int CAPTURE_GALLERY = 15;

    private BottomSheetBehavior selectPhotoBehaviour;
    private SimpleDraweeView swView;

    public File filen = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_create);

        LinearLayout llSelect = findViewById(R.id.select_photo);
        selectPhotoBehaviour = BottomSheetBehavior.from(llSelect);

        swView = findViewById(R.id.img1);

        swView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectPhotoBehaviour.getState() != BottomSheetBehavior.STATE_EXPANDED){
                    selectPhotoBehaviour.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    selectPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        TextView txtcamera = findViewById(R.id.dlg_camera);
        TextView txtgallery = findViewById(R.id.dlg_gallery);

        txtcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                checkPermissionCW();
            }
        });

        txtgallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPhotoBehaviour.setState(BottomSheetBehavior.STATE_COLLAPSED);
                checkPermissionRG();
            }
        });

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_create);
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        viewModel.email.setValue(preferences.getString("email", ""));
        viewModel.negativeBalance.setValue(false);

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

    private boolean isFinished() {
        return preferences.getBoolean("timeout", false);
    }

    private void checkPermissionCA(){
        int permissionCheck = ContextCompat.checkSelfPermission(CreateTransactionActivity.this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    CreateTransactionActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA);
        } else {
            catchPhoto();
        }
    }
    private void checkPermissionCW(){
        int permissionCheck = ContextCompat.checkSelfPermission(CreateTransactionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    CreateTransactionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_CAMERA);
        } else {
            checkPermissionCA();
        }
    }

    private void catchPhoto() {
        filen = getFile();
        if(filen!=null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                Uri photocUri = Uri.fromFile(filen);
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photocUri);
                startActivityForResult(intent, CAPTURE_CAMERA);
            } catch (ActivityNotFoundException e) {

            }
        } else {
            Toast.makeText(CreateTransactionActivity.this, "please check your sdcard status", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissionRG(){
        int permissionCheck = ContextCompat.checkSelfPermission(CreateTransactionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    CreateTransactionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
        } else {
            checkPermissionWG();
        }
    }
    private void checkPermissionWG(){
        int permissionCheck = ContextCompat.checkSelfPermission(CreateTransactionActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        // int permissionCheck2 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    CreateTransactionActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_GALLERY);
        } else {
            getPhotos();
        }
    }
    private void getPhotos() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_GALLERY);
    }

    public File getFile(){

        File fileDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        if (!fileDir.exists()){
            if (!fileDir.mkdirs()){
                return null;
            }
        }


        File mediaFile = new File(fileDir.getPath() + File.separator + "temp.jpg");
        return mediaFile;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Log.e("msg", "photo not get");
            return;
        }

        switch (requestCode) {

            case CAPTURE_CAMERA:

                swView.setImageURI(Uri.parse("file:///" + filen));
                break;


            case REQUEST_GALLERY:
                try {

                    InputStream inputStream = getContentResolver().openInputStream(data.getData());
                    filen = getFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(filen);
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, bytesRead);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                    swView.setImageURI(Uri.parse("file:///" + filen));//fresco library

                } catch (Exception e) {

                    Log.e("", "Error while creating temp file", e);
                }
                break;

        }
    }

    public void onRequestPermissionsResult (int requestCode, String[] permissions,  int[] grantResults)
    {

        switch (requestCode) {
            case REQUEST_CAMERA:
                catchPhoto();
                break;
            case WRITE_CAMERA:
                checkPermissionCA();
                break;
            case REQUEST_GALLERY:
                checkPermissionWG();
                break;
            case WRITE_GALLERY:
                getPhotos();
                break;
        }
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
