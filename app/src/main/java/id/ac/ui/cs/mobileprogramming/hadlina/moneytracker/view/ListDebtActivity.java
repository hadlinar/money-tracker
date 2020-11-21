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
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityDebtListBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view.adapter.DebtAdapter;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.DebtViewModel;

public class ListDebtActivity extends AppCompatActivity implements DebtAdapter.OnItemClickListener {
    private ActivityDebtListBinding binding;
    private DebtViewModel viewModel;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_debt_list);
        viewModel = ViewModelProviders.of(this).get(DebtViewModel.class);
        viewModel.setAdapterClickListener(this);
        viewModel.email.setValue(preferences.getString("email", ""));

        binding.setViewModel(viewModel);
        binding.rvDebtList.setLayoutManager(new LinearLayoutManager(this));
        binding.rvDebtList.setAdapter(viewModel.getAdapter());

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        observe();
    }

    private void observe() {
        viewModel.getDebts().observe(this, new Observer<List<Debt>>() {
            @Override
            public void onChanged(List<Debt> debt) {
                viewModel.setDebtList(debt);
            }
        });

        viewModel.isNewDebtClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    viewModel.resetNewDebtClicked();
                    startActivity(new Intent(getApplicationContext(), CreateDebtActivity.class));
                }
            }
        });
    }

    @Override
    public void onItemClick(View view, Debt debt) {
        Intent intent = new Intent(getApplicationContext(), DetailDebtActivity.class);
        intent.putExtra("debt", debt);
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
