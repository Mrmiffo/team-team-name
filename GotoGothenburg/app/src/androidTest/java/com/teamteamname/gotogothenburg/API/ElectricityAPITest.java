package com.teamteamname.gotogothenburg.api;

import android.app.Activity;
import android.test.ActivityUnitTestCase;

import com.android.volley.Response;
import com.teamteamname.gotogothenburg.API.mock.MockElectricityHandler;
import com.teamteamname.gotogothenburg.API.mock.MockRequestQueue;
import com.teamteamname.gotogothenburg.GPSCoord;
import com.teamteamname.gotogothenburg.map.Bus;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Olof on 28/09/2015.
 */
public class ElectricityAPITest extends ActivityUnitTestCase {
    private ElectricityAPI mElevtricityAPI;
    private Activity mActivity;
    private MockRequestQueue mQueue;

    // Mock JSONArrays that is used to test the API's parsing of responses.
    private JSONArray testEmptyResponse = new JSONArray();
    private JSONArray testTemp = new JSONArray();
    private JSONArray testWifiUsers = new JSONArray();

    public ElectricityAPITest(Class activityClass) { super(activityClass); }

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        mActivity = getActivity();

        mQueue = new MockRequestQueue();

        ElectricityAPI.init(this.getActivity().getApplicationContext(), mQueue);

        mElevtricityAPI = ElectricityAPI.getInstance();

    }

    @Override
    protected void tearDown() throws Exception{

    }

    public void testGPSRequest(){
        Bus bus = Bus.getBusByDgw("Ericsson$100020");
        MockElectricityHandler mHandler = new MockElectricityHandler();

        mElevtricityAPI.getBusLocation(bus, mHandler);

        String mockURI = "https://ece01.ericsson.net:4443/ecity?dgw=" + bus.getDgw() + "&" + "sensorSpec=Ericsson$GPS2";
        mQueue.getUri().substring(0,mockURI.length());

        assertTrue(mQueue.getMethod() == 0);
        assertTrue(mQueue.getUri().equals(mockURI));
        assertTrue(mQueue.getHeader().get("Authorization").equals("Basic Z3JwMjc6QkNjUmwtOFVsSQ=="));

    }

    public void testGPSResponse(){
        Bus bus = Bus.getBusByDgw("Ericsson$100020");
        MockElectricityHandler mHandler = new MockElectricityHandler();

        mElevtricityAPI.getBusLocation(bus, mHandler);

        Response.ErrorListener errorListener = mQueue.getErrorListener();
        Response.Listener parser = mQueue.getParser();

        //Tests response on empty response
        parser.onResponse(testEmptyResponse);

        //Tests
        try{
            JSONArray testGPSCoord = new JSONArray("[{\"resourceSpec\":\"Authenticated_Users_Value\",\"timestamp\":\"1442391279000\",\"value\":\"5\",\"gatewayId\":\"Vin_Num_001\"}]");

        }catch (JSONException e){

        }

    }

    public void testStopPressedResponse(){
        Bus bus = Bus.getBusByDgw("Ericsson$100020");
        MockElectricityHandler mHandler = new MockElectricityHandler();

        mElevtricityAPI.getStopPressed(bus, mHandler);

        Response.ErrorListener errorListener = mQueue.getErrorListener();
        Response.Listener parser = mQueue.getParser();



        JSONArray testStopPressedCorrect = new JSONArray();
        JSONArray testStopPressedNoAnswer = new JSONArray();

        //[{"resourceSpec":"Authenticated_Users_Value","timestamp":"1442391279000","value":"5","gatewayId":"Vin_Num_001"}]
    }

    public void testCabinTempResponse(){

    }

    public void testAmbientTempResponse(){

    }

    public void testWifiUsersResponse(){

    }

}
