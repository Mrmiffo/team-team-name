package com.teamteamname.gotogothenburg.api.electricity.mock;

import com.android.volley.Network;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.VolleyError;

/**
 * A mockclass of the volley-class Network. Only used as a false parameter to create a MockRequestQueue.
 */

//This class is only used to be able to create a MockRequestQueue
public class MockNetwork implements Network {
    @Override
    public NetworkResponse performRequest(Request<?> request) throws VolleyError {
        return null;
    }
}
