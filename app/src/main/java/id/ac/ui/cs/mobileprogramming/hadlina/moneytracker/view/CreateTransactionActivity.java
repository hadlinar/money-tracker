package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityTransactionCreateBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.TransactionViewModel;

public class CreateTransactionActivity extends AppCompatActivity {

    private ActivityTransactionCreateBinding binding;
    private TransactionViewModel viewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_create);

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
