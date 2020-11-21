package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Account;

@Dao
public interface AccountDao {
    @Insert
    void insert(Account acc);

    @Update
    void update(Account acc);

    @Query("DELETE FROM account")
    void deleteAll();

    @Query("SELECT * FROM account WHERE email = :email")
    LiveData<Account> getAccountByEmail(String email);

    @Query("SELECT * FROM account WHERE email = :email")
    Account getAccountByEmailAsync(String email);
}
