package com.teamteamname.gotogothenburg.API;


import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;

/**
 * Created by Olof on 25/09/2015.
 */
public class ElectricityAPI implements IElectricityAPI{

    private RequestQueue queue;

    public ElectricityAPI(RequestQueue queue){
        this.queue = queue;
    }

    public ElectricityAPI(Context context){
        Cache cache = new DiskBasedCache(context.getCacheDir(),1024*1024);

        Network network = new BasicNetwork(new HurlStack());

        queue = new RequestQueue(cache,network);

        queue.start();
    }

}
