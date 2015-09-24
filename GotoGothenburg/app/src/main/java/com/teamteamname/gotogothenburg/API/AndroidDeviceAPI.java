package com.teamteamname.gotogothenburg.API;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.teamteamname.gotogothenburg.R;

import java.io.File;

/**
 * The AndriodDeviceAPI is intended for any android device with a WiFi signal.
 * Created by Anton on 2015-09-23.
 */
public class AndroidDeviceAPI implements IDeviceAPI {
    private WifiInfo wifiInfo;
    private Context context;

    /**
     * Constructor require a context in order to connect the API to the device.
     * @param context the main activity context
     */
    public AndroidDeviceAPI(Context context){
        this.context = context;
    }

    /**
     * Method used to get the MAC adress of the router. This is used to identify which bus the user is on.
     * @return null if the com.teamteamname.gotogothenburg.API has been created incorrectly or if the device is not connected to a Wifi.
     */
    @Override
    public String getWiFiRouterMAC(){
        if (wifiInfo == null) {
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiManager.getConnectionInfo();
        }
        return wifiInfo.getBSSID();
    }

    @Override
    public void playSound(File sound) {
        int resourceID = context.getResources().getIdentifier(sound.getName(), null, null);
        MediaPlayer mediaPlayer = MediaPlayer.create(context, resourceID);
        mediaPlayer.start();
    }

    @Override
    public boolean iSHandsfreePluggedIn() {
        return AudioManager.ACTION_HEADSET_PLUG.equals("1");
    }

    @Override
    public void handsfreeNotPluggedInPopUp() {
        new DialogFragment() {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                // Use the Builder class for convenient dialog construction
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.unplugged_handsfree)
                        .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                // Create the AlertDialog object and return it
                return builder.create();
            }
        };
    }
}
