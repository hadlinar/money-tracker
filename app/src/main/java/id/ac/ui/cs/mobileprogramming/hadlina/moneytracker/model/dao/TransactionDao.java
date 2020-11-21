package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Transaction;

@Dao
public interface TransactionDao {
    @Insert
    void insert(Transaction trx);

    @Delete
    void delete(Transaction trx);

    @Query("DELETE FROM `transaction`")
    void deleteAll();

    @Query("SELECT * FROM `transaction` WHERE user = :user")
    LiveData<List<Transaction>> getAllTransactionByUser(String user);
}
