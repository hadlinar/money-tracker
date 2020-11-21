package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao.AccountDao;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao.DebtDao;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao.TransactionDao;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.database.LocalDatabase;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;

public class Repository {
    private AccountDao accountDao;
    private TransactionDao transactionDao;
    private DebtDao debtDao;

    public Repository(Application application) {
        LocalDatabase db = LocalDatabase.getInstance(application);
        accountDao = db.accountDao();
        transactionDao = db.transactionDao();
        debtDao = db.debtDao();
    }

    // Below are all wrapper method for Account DAO
    public LiveData<Account> getAccountByEmail(String email) {
        return accountDao.getAccountByEmail(email);
    }

    public Account getAccountByEmailAsync(String email) throws ExecutionException, InterruptedException {
        return new GetAccountByEmailAsyncTask(accountDao).execute(email).get();
    }

    public void insertAccount(Account account) {
        new InsertAccountAsyncTask(accountDao).execute(account);
    }

    public void updateAccount(Account account) {
        new UpdateAccountAsyncTask(accountDao).execute(account);
    }

    private static class InsertAccountAsyncTask extends AsyncTask<Account, Void, Void> {
        private AccountDao accountDao;

        InsertAccountAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.insert(accounts[0]);
            return null;
        }
    }

    private static class UpdateAccountAsyncTask extends  AsyncTask<Account, Void, Void> {
        private AccountDao accountDao;

        UpdateAccountAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.update(accounts[0]);
            return null;
        }
    }

    private static class GetAccountByEmailAsyncTask extends  AsyncTask<String, Void, Account> {
        private AccountDao accountDao;

        GetAccountByEmailAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Account doInBackground(String... strings) {
            return accountDao.getAccountByEmailAsync(strings[0]);
        }
    }


    // Below are all wrapper method for Transaction DAO
    public LiveData<List<Transaction>> getAllTransaction(String user) {
        return transactionDao.getAllTransactionByUser(user);
    }

    public void insertTransaction(Transaction transaction) {
        new InsertTransactionAsyncTask(transactionDao).execute(transaction);
    }

    private static class InsertTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {
        private TransactionDao transactionDao;

        InsertTransactionAsyncTask(TransactionDao transactionDao) {
            this.transactionDao = transactionDao;
        }

        @Override
        protected Void doInBackground(Transaction... transactions) {
            transactionDao.insert(transactions[0]);
            return null;
        }
    }

    // Below are all wrapper method for Debt DAO
    public LiveData<List<Debt>> getAllDebt(String user) {
        return debtDao.getAllDebtByUser(user);
    }

    public void insertDebt(Debt debt){
        new InsertDebtAsyncTask(debtDao).execute(debt);
    }

    private static class InsertDebtAsyncTask extends AsyncTask<Debt, Void, Void> {
        private DebtDao debtDao;

        InsertDebtAsyncTask(DebtDao debtDao) {
            this.debtDao = debtDao;
        }


        @Override
        protected Void doInBackground(Debt... debts) {
            debtDao.insert(debts[0]);
            return null;
        }
    }
}
