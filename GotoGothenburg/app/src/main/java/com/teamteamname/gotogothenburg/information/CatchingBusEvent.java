package com.teamteamname.gotogothenburg.information;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.activity.MainActivity;

/**
 * Created by Olof on 12/10/2015.
 */
public class CatchingBusEvent implements Runnable {
    private Context context;
    private int busLine;
    private String stop;
    private String stopPosition;

    public CatchingBusEvent(Context context, int busLine, String stop, String stopPosition){
        this.context = context;
        this.busLine = busLine;
        this.stop = stop;
        this.stopPosition = stopPosition;
    }

    @Override
    public void run() {
        // Check if the Bus really is close.

        // Then say it to the user.
        // Notification!
        int id = 1;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Your bus is approaching");
        builder.setContentText("Bus " + busLine + " is almost at the stop");
        builder.setSmallIcon(R.drawable.testicon);

        Log.i("Notification","Created CatchBus Notification");

        try {
            Intent resultIntent = new Intent(context, Class.forName("com.teamteamname.gotogothenburg.MainActivity").getClass());
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack
            stackBuilder.addParentStack(Class.forName("com.teamteamname.gotogothenburg.MainActivity").getClass());
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);
            // Gets a PendingIntent containing the entire back stack
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(id, builder.build());

            // Remember to say where he/she should stand to catch it.
        } catch (ClassNotFoundException e) {
            Log.e("Class not found", e.getMessage());
        }

    }
}
