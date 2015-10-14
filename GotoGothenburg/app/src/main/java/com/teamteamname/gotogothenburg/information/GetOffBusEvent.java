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
public class GetOffBusEvent implements Runnable {
    private Context context;
    private int busLine;
    private String stop;
    private String stopPosition;

    public GetOffBusEvent(Context context, int busLine, String stop, String stopPosition){
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
        int id = 2;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("Your stop is approaching");
        builder.setContentText("Bus " + busLine + " is almost at the stop");
        builder.setSmallIcon(R.drawable.testicon);

        Log.i("Notification", "Created GetOffBus Notification");

        Intent resultIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack
        stackBuilder.addParentStack(MainActivity.class);
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
    }
}
