package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class Account {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String email;
    private int amount;

    public Account(String email, int amount) {
        this.email = email;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
