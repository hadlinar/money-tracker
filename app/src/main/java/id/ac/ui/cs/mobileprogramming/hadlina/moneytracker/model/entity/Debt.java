package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Debt implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String title;

    private int amount;

    @NonNull
    private String creditor;

    @NonNull
    private String dueDate;

    @NonNull
    private String user;

    public Debt(@NonNull String title, int amount, @NonNull String creditor, @NonNull String dueDate, @NonNull String user) {
        this.title = title;
        this.amount = amount;
        this.creditor = creditor;
        this.dueDate = dueDate;
        this.user = user;
    }

    protected Debt(Parcel in) {
        id = in.readInt();
        title = Objects.requireNonNull(in.readString());
        amount = in.readInt();
        creditor = Objects.requireNonNull(in.readString());
        dueDate = Objects.requireNonNull(in.readString());
        user = Objects.requireNonNull(in.readString());
    }

    public static final Creator<Debt> CREATOR = new Creator<Debt>() {
        @Override
        public Debt createFromParcel(Parcel in) {
            return new Debt(in);
        }

        @Override
        public Debt[] newArray(int size) {
            return new Debt[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    @NonNull
    public String getCreditor() {
        return creditor;
    }

    public void setCreditor(@NonNull String creditor) {
        this.creditor = creditor;
    }

    @NonNull
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(@NonNull String dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeInt(amount);
        parcel.writeString(creditor);
        parcel.writeString(dueDate);
        parcel.writeString(user);
    }
}
