package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Transaction implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String type;

    @NonNull
    private String title;

    @NonNull
    private String date;

    private int amount;

    @NonNull
    private String user;

    @NonNull
    private String category;

    public Transaction(String type, String title, String date, int amount, @NonNull String user, @NonNull String category) {
        this.type = type;
        this.title = title;
        this.date = date;
        this.amount = amount;
        this.user = user;
        this.category = category;
    }

    protected Transaction(Parcel in) {
        type = Objects.requireNonNull(in.readString());
        title = Objects.requireNonNull(in.readString());
        date = Objects.requireNonNull(in.readString());
        amount = in.readInt();
        id = in.readInt();
        user = Objects.requireNonNull(in.readString());
        category = Objects.requireNonNull(in.readString());
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @NonNull
    public String getType() {
        return type;
    }

    public void setType(@NonNull String type) {
        this.type = type;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getUser() {
        return user;
    }

    public void setUser(@NonNull String user) {
        this.user = user;
    }

    @NonNull
    public String getCategory() {
        return category;
    }

    public void setCategory(@NonNull String category) {
        this.category = category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(type);
        parcel.writeString(title);
        parcel.writeString(date);
        parcel.writeInt(amount);
        parcel.writeInt(id);
        parcel.writeString(user);
        parcel.writeString(category);
    }
}
