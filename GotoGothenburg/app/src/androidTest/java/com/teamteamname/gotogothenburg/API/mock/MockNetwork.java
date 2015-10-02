package com.teamteamname.gotogothenburg.api.mock;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

/**
 * Created by Olof on 29/09/2015.
 */

//This class is only used to be able to create a MockRequestQueue
public class MockNetwork implements Network {
    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        return null;
    }
}
