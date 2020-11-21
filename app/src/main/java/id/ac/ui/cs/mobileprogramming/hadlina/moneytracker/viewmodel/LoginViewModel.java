package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel;

import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.User;

public class LoginViewModel extends ViewModel {
    public MutableLiveData<FirebaseAuth> firebase = new MutableLiveData<>();
    public MutableLiveData<LinearLayout> progressBar = new MutableLiveData<>();

    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();

    public MutableLiveData<Integer> errorEmail = new MutableLiveData<>();
    public MutableLiveData<Integer> errorPassword = new MutableLiveData<>();

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> registerClicked = new MutableLiveData<>();

    public MutableLiveData<User> getUser() {
        return userLiveData;
    }

    public void onLoginClicked() {
        if (isInputValid()) {
            progressBar.getValue().setVisibility(View.VISIBLE);
            firebase.getValue().signInWithEmailAndPassword(email.getValue(), password.getValue())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        User user = new User(email.getValue(), password.getValue());
                        userLiveData.setValue(user);
                    } else userLiveData.setValue(null);
                }
            });
        }
    }

    private boolean isInputValid() {
        boolean valid = true;
        if (email.getValue() == null || email.getValue().length() == 0) {
            errorEmail.setValue(R.string.error_blank_email);
            valid = false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.getValue()).matches()) {
            errorEmail.setValue(R.string.error_wrong_email);
            valid = false;
        }
        else errorEmail.setValue(null);

        if (password.getValue() == null || password.getValue().length() < 6) {
            errorPassword.setValue(R.string.error_password_length);
            valid = false;
        }
        else errorPassword.setValue(null);

        return valid;
    }

    public void onRegisterClicked() {
        registerClicked.setValue(true);
    }

    public void resetRegisterClicked() {
        registerClicked.setValue(false);
    }

    public MutableLiveData<Boolean> isRegisterClicked() {
        return registerClicked;
    }
}