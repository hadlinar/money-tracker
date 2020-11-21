package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityTransactionDetailBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.TransactionViewModel;

public class DetailTransactionActivity extends AppCompatActivity {
    private ActivityTransactionDetailBinding binding;
    private TransactionViewModel viewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_detail);
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);

        Transaction transaction = getIntent().getParcelableExtra("transaction");

        binding.setTransaction(transaction);
        binding.setAmount(Integer.toString(transaction.getAmount()));
        if (transaction.getType().equalsIgnoreCase("earnings")) binding.tvItemType.setText(R.string.transaction_type_income);
        else binding.tvItemType.setText(R.string.transaction_type_expense);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewModel.setTransactionLiveData(transaction);
        observe();
    }

    private void observe() {
        viewModel.newTransaction().observe(this, new Observer<Transaction>() {
            @Override
            public void onChanged(Transaction transaction) {
                if (transaction != null) {
                    switch (transaction.getCategory().toLowerCase()) {
                        case "food":
                            binding.itemImage.setImageResource(R.drawable.ic_restaurant_24dp);
                            binding.tvCategory.setText(getString(R.string.category_food));
                            break;
                        case "vehicle":
                            binding.itemImage.setImageResource(R.drawable.ic_car_24dp);
                            binding.tvCategory.setText(getString(R.string.category_vehicle));
                            break;
                        case "entertainment":
                            binding.itemImage.setImageResource(R.drawable.ic_videogame_24dp);
                            binding.tvCategory.setText(getString(R.string.category_entertainment));
                            break;
                        case "income":
                            binding.itemImage.setImageResource(R.drawable.ic_money_24dp);
                            binding.tvCategory.setText(getString(R.string.category_income));
                            break;
                        default:
                            binding.itemImage.setImageResource(R.drawable.ic_not_found_24dp);
                            break;
                    }
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
}
