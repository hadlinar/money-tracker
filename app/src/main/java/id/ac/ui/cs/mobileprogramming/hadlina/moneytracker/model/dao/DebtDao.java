package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity.Debt;

@Dao
public interface DebtDao {
    @Insert
    void insert(Debt debt);

    @Update
    void update(Debt debt);

    @Delete
    void delete(Debt debt);

    @Query("SELECT * FROM debt WHERE user = :user")
    LiveData<List<Debt>> getAllDebtByUser(String user);
}
