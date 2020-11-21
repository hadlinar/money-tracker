package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.viewmodel;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.Repository;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view.adapter.TransactionAdapter;

public class TransactionViewModel extends AndroidViewModel {

    private TransactionAdapter adapter = new TransactionAdapter();
    private Repository repo;

    public MutableLiveData<String> description = new MutableLiveData<>();
    public MutableLiveData<String> amount = new MutableLiveData<>();
    public MutableLiveData<String> date = new MutableLiveData<>();
    public MutableLiveData<Integer> type = new MutableLiveData<>();
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> category = new MutableLiveData<>();
    public MutableLiveData<Boolean> negativeBalance = new MutableLiveData<>();
    private MutableLiveData<Transaction> transactionLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> selectDateClicked = new MutableLiveData<>();
    private MutableLiveData<Boolean> newTransactionClicked = new MutableLiveData<>();

    public TransactionViewModel(Application application) {
        super(application);
        repo = new Repository(application);
        adapter.setTransactionList(new ArrayList<Transaction>());
    }

    // Methods below are for List Transaction Activity
    public void setAdapterClickListener(TransactionAdapter.OnItemClickListener clickListener) {
        this.adapter.setClickListener(clickListener);
    }

    public TransactionAdapter getAdapter() {
        return adapter;
    }

    public void setTransactionList(List<Transaction> transactions) {
        this.adapter.setTransactionList(transactions);
        this.adapter.notifyDataSetChanged();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return repo.getAllTransaction(email.getValue());
    }

    public void onNewTransactionClicked() {
        newTransactionClicked.setValue(true);
    }

    public void resetNewTransactionClicked() {
        newTransactionClicked.setValue(false);
    }

    public MutableLiveData<Boolean> isNewTransactionClicked() {
        return newTransactionClicked;
    }


    // Methods below are for Create Transaction Activity
    public void onCreateButtonClicked() {
        if (isInputValid()) {
            String typeString = "";
            int amt = 0;
            Account account = null;

            if (type.getValue() != null)
                if (type.getValue() == R.id.rb_expense) typeString = "Expenses";
            else typeString = "Earnings";

            if (amount.getValue() != null) amt = Integer.parseInt(amount.getValue());

            try {
                account = repo.getAccountByEmailAsync(email.getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (typeString.equalsIgnoreCase("expenses")) {
                if (account != null){
                    if (account.getAmount() - amt < 0) negativeBalance.setValue(true);
                    else negativeBalance.setValue(false);
                }
            }

            if (category.getValue() != null)
                switch (category.getValue().toLowerCase()) {
                    case "makanan":
                        category.setValue("Food");
                        break;
                    case "kendaraan":
                        category.setValue("Vehicle");
                        break;
                    case "hiburan":
                        category.setValue("Entertainment");
                        break;
                    case "pemasukan":
                        category.setValue("Income");
                        break;
                    default:
                        break;
                }

            if (!negativeBalance.getValue()) {
                Transaction transaction = new Transaction(typeString, description.getValue(), date.getValue(), amt, email.getValue(), category.getValue());
                repo.insertTransaction(transaction);
                transactionLiveData.setValue(transaction);

                updateAccountBalance(account, typeString, amt);
            }
        } else transactionLiveData.setValue(null);
    }

    private boolean isInputValid() {
        return email.getValue() != null && type.getValue() != null && description.getValue() != null &&
                date.getValue() != null && amount.getValue() != null && category.getValue() != null;
    }

    private void updateAccountBalance(Account account, String type, int nominal) {
        if (type.equalsIgnoreCase("earnings")) account.setAmount(account.getAmount() + nominal);
        else if (type.equalsIgnoreCase("expenses")) account.setAmount(account.getAmount() - nominal);


        repo.updateAccount(account);
    }

    public MutableLiveData<Transaction> newTransaction() {
        return transactionLiveData;
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


    // Methods below are for Detail Transaction Activity
    public void setTransactionLiveData(Transaction transactionLiveData) {
        this.transactionLiveData.setValue(transactionLiveData);
    }

}