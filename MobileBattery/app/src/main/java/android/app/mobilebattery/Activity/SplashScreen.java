package android.app.mobilebattery.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.mobilebattery.Helper.CheckConnection;
import android.app.mobilebattery.R;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {


    SharedPreferences appPreferences;

    private final int SPLASH_TIME_OUT = 4000;

    Context mContext = SplashScreen.this;
    private AlertDialog alertDialog;
    CheckConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);

        buildAlertDialog(mContext);
        connection = new CheckConnection(this);


        if (connection.isConnected()) {
            splashDelay();
        } else if (!connection.isConnected()) {
            alertDialog.show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(networkStateReceiver, intentFilter);
        connection = new CheckConnection(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (connection.isConnected()) {
            alertDialog.cancel();
            splashDelay();
        } else {
            alertDialog.show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(networkStateReceiver);
    }


    private void splashDelay() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                appPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
                String loginId = appPreferences.getString("loginId", "nothing");

                if (loginId.equals("admin@mb.com")){
                    Intent intent = new Intent(SplashScreen.this, Adminhome.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(SplashScreen.this, LogInScreen.class);
                    startActivity(intent);
                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public void buildAlertDialog(Context c) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet connection");
        builder.setMessage("You need to have Mobile BatteryBrandItem or Wifi to access this. Press ok to exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                builder.setCancelable(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog = builder.create();
    }

    private BroadcastReceiver networkStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiStateExtra = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (wifiStateExtra) {
                case WifiManager.WIFI_STATE_ENABLED:
                    setContentView(R.layout.activity_splash_screen);
                    splashDelay();
                    alertDialog.cancel();
                    break;

                case WifiManager.WIFI_STATE_DISABLED:
                    alertDialog.show();
                    break;
            }
        }
    };
}
