package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.view;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;

public class TimeoutActivity extends AppCompatActivity {

    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences(getString(R.string.preferences), Context.MODE_PRIVATE);

        ContextThemeWrapper ctw = new ContextThemeWrapper(TimeoutActivity.this, R.style.Theme_MaterialComponents_Dialog);
        final AlertDialog alertDialog = new AlertDialog.Builder(ctw).create();
        alertDialog.setCancelable(false);
        alertDialog.setTitle(getString(R.string.session_timeout));
        alertDialog.setTitle(this.getString(R.string.session_has_expired));
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, this.getString(R.string.logout), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email", null);
                editor.apply();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }
}
