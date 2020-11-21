package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.R;


public class TimeoutReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase("SESSION_TIMEOUT")) {
            SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.preferences), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("timeout", true);
            editor.apply();
        }
    }
}
