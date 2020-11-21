package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityRegisterBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.User;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils.TimeoutService;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        viewModel.firebase.setValue(firebaseAuth);
        viewModel.progressBar.setValue(binding.pbRegisterBackground);

        binding.setViewModel(viewModel);
        observe();
    }

    private void observe() {

        viewModel.errorEmail.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                if (resource != null) {
                    String text = getApplicationContext().getString(resource);
                    binding.tilEmail.setError(text);
                }
            }
        });

        viewModel.errorPassword.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                if (resource != null) {
                    String text = getApplicationContext().getString(resource);
                    binding.tilPassword.setError(text);
                }
            }
        });

        viewModel.isLoginClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    viewModel.resetLoginClicked();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }
            }
        });

        viewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.pbRegisterBackground.setVisibility(View.GONE);
                if (user != null) {

                    SharedPreferences pref = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", user.getEmail());
                    editor.putBoolean("timeout", false);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), NewAccountBalanceActivity.class);
                    intent.putExtra("email", user.getEmail());

                    startService(new Intent(getApplicationContext(), TimeoutService.class));
                    startActivity(intent);
                } else Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_register),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
