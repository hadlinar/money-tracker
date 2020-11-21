package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityTransactionListBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view.adapter.TransactionAdapter;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.TransactionViewModel;

public class ListTransactionActivity extends AppCompatActivity implements TransactionAdapter.OnItemClickListener {

    private ActivityTransactionListBinding binding;
    private TransactionViewModel viewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_transaction_list);
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        viewModel.setAdapterClickListener(this);
        viewModel.email.setValue(preferences.getString("email", ""));

        binding.setTransactionViewModel(viewModel);
        binding.rvTransactionList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvTransactionList.setAdapter(viewModel.getAdapter());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        observe();
    }

    private void observe() {
        viewModel.getTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                viewModel.setTransactionList(transactions);
            }
        });

        viewModel.isNewTransactionClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    viewModel.resetNewTransactionClicked();
                    startActivity(new Intent(getApplicationContext(), CreateTransactionActivity.class));
                }
            }
        });

    }


    @Override
    public void onItemClick(View view, Transaction transaction) {
        Intent intent = new Intent(getApplicationContext(), DetailTransactionActivity.class);
        intent.putExtra("transaction", transaction);
        startActivity(intent);

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
