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
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.databinding.ActivityLoginBinding;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.User;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils.TimeoutService;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        loginViewModel.firebase.setValue(firebaseAuth);
        loginViewModel.progressBar.setValue(binding.pbLoginBackground);

        binding.setLoginViewModel(loginViewModel);
        observe();
    }

    private void observe() {

        loginViewModel.errorEmail.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                if (resource != null) {
                    String text = getApplicationContext().getString(resource);
                    binding.tilEmail.setError(text);
                }
            }
        });

        loginViewModel.errorPassword.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer resource) {
                if (resource != null) {
                    String text = getApplicationContext().getString(resource);
                    binding.tilPassword.setError(text);
                }
            }
        });

        loginViewModel.isRegisterClicked().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean clicked) {
                if (clicked) {
                    loginViewModel.resetRegisterClicked();
                    startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
                }
            }
        });

        loginViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                binding.pbLoginBackground.setVisibility(View.GONE);
                if (user != null) {

                    SharedPreferences pref = getSharedPreferences(getApplicationContext().getString(R.string.preferences), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("email", user.getEmail());
                    editor.putBoolean("timeout", false);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("email", user.getEmail());

                    startService(new Intent(getApplicationContext(), TimeoutService.class));
                    startActivity(intent);
                } else Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.error_wrong_email_and_password),Toast.LENGTH_SHORT).show();
            }
        });
    }
}
