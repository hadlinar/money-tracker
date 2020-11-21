package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.Repository;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> transactionClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> debtClicked = new MutableLiveData<>();
    private MutableLiveData<String> name = new MutableLiveData<>();
    private String email;
    private Repository repo;

    public MainViewModel(Application application) {
        super(application);
        repo = new Repository(application);
    }

    public MutableLiveData<Boolean> isTransactionClicked() {
        return transactionClicked;
    }

    public MutableLiveData<Boolean> isDebtClicked() {
        return debtClicked;
    }

    public void onTransactionClicked() {
        transactionClicked.setValue(true);
    }

    public void resetTransactionClicked() {
        transactionClicked.setValue(false);
    }

    public void onDebtClicked() {
        debtClicked.setValue(true);
    }

    public void resetDebtClicked() {
        debtClicked.setValue(false);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public MutableLiveData<String> getName() {
        return name;
    }

    public void setName(String email) {
        String[] arr = email.split("@");
        name.setValue(arr[0]);
    }

    public LiveData<Account> getAccount() {
        return repo.getAccountByEmail(email);
    }

}
