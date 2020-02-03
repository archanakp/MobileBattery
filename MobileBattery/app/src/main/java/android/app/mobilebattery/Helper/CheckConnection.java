package android.app.mobilebattery.Helper;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

public class CheckConnection {

    Context context;
    AlertDialog alertDialog;

    public CheckConnection(Context context) {
        this.context = context;
    }

    public boolean isConnected(){

        ConnectivityManager conManager = (ConnectivityManager) context
                .getSystemService(Service.CONNECTIVITY_SERVICE);
        if(conManager != null){
            NetworkInfo info = conManager.getActiveNetworkInfo();
            if (info != null){
                if (info.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }

        return false;
    }

    public void buildAlertDialog(Context c) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet connection");
        builder.setMessage("You need to have Mobile BatteryBrandItem or Wifi to access this. Press ok to exit");
        builder.setCancelable(false);
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                context.getApplicationContext().startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                builder.setCancelable(true);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                context.getApplicationContext().finish();
            }
        });

        alertDialog = builder.create();

    }
}
