package com.teamteamname.gotogothenburg.information;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Olof on 12/10/2015.
 */
public class RouteNotifier extends Handler {

    private Context context;

    public RouteNotifier(Context context){
        super(Looper.getMainLooper());
        this.context = context;
    }

    public RouteNotifier(Context context, Looper looper){
        super(looper);
        this.context = context;
    }

    /**
     * Adds a catchBusEvent which will create a notification at the given timespamp (uptimeMillis).
     * @param timestamp
     * @param busLine
     * @param stopName
     * @param stopPosition
     */
    public void addCatchBusEvent(long timestamp, int busLine, String stopName, String stopPosition){
        this.postAtTime(new CatchingBusEvent(context,busLine,stopName,stopPosition),timestamp);
    }

    /**
     * Adds a getOffBusEvent which will create a notification at the given timestamp (uptimeMillis).
     * @param timestamp
     * @param busLine
     * @param stopName
     * @param stopPosition
     */
    public void addGetOffBusEvent(long timestamp, int busLine, String stopName, String stopPosition){
        this.postAtTime(new GetOffBusEvent(context,busLine,stopName,stopPosition),timestamp);
    }
}
