package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityNewAccountBalanceBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.RegisterViewModel;

public class NewAccountBalanceActivity extends AppCompatActivity {

    ActivityNewAccountBalanceBinding binding;
    RegisterViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_account_balance);
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        viewModel.email.setValue(getIntent().getStringExtra("email"));
        binding.setViewModel(viewModel);

        observe();
    }

    private void observe() {
        viewModel.isCreateClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    viewModel.resetCreateClicked();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("email", getIntent().getStringExtra("email"));
                    startActivity(intent);
                }
            }
        });
    }
}
