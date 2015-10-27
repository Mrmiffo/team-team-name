package com.teamteamname.gotogothenburg.guide;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.teamteamname.gotogothenburg.R;

/**
 * A simple class with a text used to tell the user that he/she is not connected to ElectriCity wifi,
 * and why it's required.
 * Created by Anton on 2015-10-13.
 */
public class ConnectToWiFiErrorDialog extends DialogFragment {
    private Context context;

    public static ConnectToWiFiErrorDialog createInstance(final Context context){
        ConnectToWiFiErrorDialog toReturn = new ConnectToWiFiErrorDialog();
        toReturn.context = context;
        return toReturn;
    }

    @Override
    public Dialog onCreateDialog(Bundle stuff){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.notConnectedToWifiErrorTitle));
        builder.setMessage(context.getString(R.string.notConnectedToWifiErrorText));
        builder.setPositiveButton(R.string.openWifiSettings, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
        builder.setNegativeButton(R.string.close, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return builder.create();

    }
}
