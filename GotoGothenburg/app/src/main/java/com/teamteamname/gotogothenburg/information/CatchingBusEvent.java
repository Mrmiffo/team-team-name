package com.teamteamname.gotogothenburg.information;

import android.content.Context;
import android.support.v7.app.NotificationCompat;

/**
 * Created by Olof on 12/10/2015.
 */
public class CatchingBusEvent implements Runnable {
    private Context context;

    public CatchingBusEvent(Context context){
        this.context = context;
    }

    @Override
    public void run() {
        // Check if the Bus really is close.

        // Then say it to the user.
        // Notification!
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Your bus is approaching");
        builder.setContentText("Bus 55 is almost at the stop");

        // Remember to say where he/she should stand to catch it.
    }
}
