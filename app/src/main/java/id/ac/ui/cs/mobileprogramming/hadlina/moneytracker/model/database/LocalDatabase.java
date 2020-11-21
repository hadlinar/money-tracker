package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao.AccountDao;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao.DebtDao;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao.TransactionDao;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;

@Database(entities = {Account.class, Transaction.class, Debt.class}, version = 4, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    private static final String DB_NAME = "wallet_db";
    private static LocalDatabase INSTANCE;

    public abstract AccountDao accountDao();
    public abstract TransactionDao transactionDao();
    public abstract DebtDao debtDao();


    public static LocalDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (LocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), LocalDatabase.class, DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
