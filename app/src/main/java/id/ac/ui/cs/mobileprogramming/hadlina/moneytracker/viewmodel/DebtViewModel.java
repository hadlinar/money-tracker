package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.Repository;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view.adapter.DebtAdapter;

public class DebtViewModel extends AndroidViewModel {
    private Repository repo;
    private DebtAdapter adapter = new DebtAdapter();

    public MutableLiveData<String> description = new MutableLiveData<>(); //ini noteeeeeeeee
    public MutableLiveData<String> amount = new MutableLiveData<>();
    public MutableLiveData<String> creditor = new MutableLiveData<>(); //debtname!
    public MutableLiveData<String> dueDate = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    private MutableLiveData<Debt> debtLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> selectDateClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> newDebtClicked = new MutableLiveData<>();

    public DebtViewModel(@NonNull Application application) {
        super(application);
        repo = new Repository(application);
        adapter.setDebtList(new ArrayList<Debt>());
    }

    // Methods below are for Debt List Activity
    public void setAdapterClickListener(DebtAdapter.OnItemClickListener clickListener) {
        adapter.setClickListener(clickListener);
    }

    public DebtAdapter getAdapter() {
        return adapter;
    }

    public void setDebtList(List<Debt> debts) {
        adapter.setDebtList(debts);
        adapter.notifyDataSetChanged();
    }

    public LiveData<List<Debt>> getDebts() {
        return repo.getAllDebt(email.getValue());
    }

    public void onNewDebtClicked() {
        newDebtClicked.setValue(true);
    }

    public void resetNewDebtClicked() {
        newDebtClicked.setValue(false);
    }

    public MutableLiveData<Boolean> isNewDebtClicked() {
        return newDebtClicked;
    }

    // Method below are for Debt Create Activity
    public void onCreateButtonClicked() {
        if (isInputValid()) {
            int amt = 0;
            amt = Integer.parseInt(amount.getValue());
            Debt debt = new Debt(description.getValue(), amt, creditor.getValue(), dueDate.getValue(), email.getValue());
            repo.insertDebt(debt);
            debtLiveData.setValue(debt);
        } else debtLiveData.setValue(null);
    }

    private boolean isInputValid() {
        return description.getValue() != null && amount.getValue() != null && creditor.getValue() != null
                && dueDate.getValue() != null && email.getValue() != null;
    }

    public MutableLiveData<Debt> newDebt() {
        return debtLiveData;
    }

    public void onSelectDateClicked() {
        selectDateClicked.setValue(true);
    }

    public void resetSelectDateClicked() {
        selectDateClicked.setValue(false);
    }

    public MutableLiveData<Boolean> isSelectDateClicked() {
        return selectDateClicked;
    }

    public String monthToString(int month) {
        switch (month) {
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            default: return "";
        }
    }

    public int monthToInt(String month) {
        switch (month) {
            case "January": return 1;
            case "February": return 2;
            case "March": return 3;
            case "April": return 4;
            case "May": return 5;
            case "June": return 6;
            case "July": return 7;
            case "August": return 8;
            case "September": return 9;
            case "October": return 10;
            case "November": return 11;
            case "December": return 12;
            default: return 0;
        }
    }
}
