package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel;

import android.app.Application;
import android.util.Patterns;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.Repository;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.User;

public class RegisterViewModel extends AndroidViewModel {
    public MutableLiveData<FirebaseAuth> firebase = new MutableLiveData<>();
    public MutableLiveData<LinearLayout> progressBar = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    public MutableLiveData<Integer> errorEmail = new MutableLiveData<>();
    public MutableLiveData<Integer> errorPassword = new MutableLiveData<>();
    public MutableLiveData<String> amount = new MutableLiveData<>();
    private MutableLiveData<User> userLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> loginClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> createClicked = new MutableLiveData<>();

    private Repository repo;

    public RegisterViewModel(Application application) {
        super(application);
        repo = new Repository(application);
    }

    public MutableLiveData<User> getUser() {
        return userLiveData;
    }

    public void onRegisterClicked() {
        if (isInputValid()) {
            progressBar.getValue().setVisibility(View.VISIBLE);
            firebase.getValue().createUserWithEmailAndPassword(email.getValue(), password.getValue())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        userLiveData.setValue(new User(email.getValue(), password.getValue()));
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

    public void onLoginClicked() {
        loginClicked.setValue(true);
    }

    public void resetLoginClicked() {
        loginClicked.setValue(false);
    }

    public MutableLiveData<Boolean> isLoginClicked() {
        return loginClicked;
    }

    public void onCreateClicked() {
        createClicked.setValue(true);
        repo.insertAccount(new Account(email.getValue(), Integer.parseInt(amount.getValue())));
    }

    public void resetCreateClicked() {
        createClicked.setValue(false);
    }

    public MutableLiveData<Boolean> isCreateClicked() {
        return createClicked;
    }
}
