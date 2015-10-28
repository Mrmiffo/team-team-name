package com.teamteamname.gotogothenburg.api.electricity;

import android.test.AndroidTestCase;
import android.util.Log;

import com.android.volley.Response;
import com.teamteamname.gotogothenburg.api.GPSCoord;
import com.teamteamname.gotogothenburg.R;
import com.teamteamname.gotogothenburg.api.electricity.mock.MockElectricityHandler;
import com.teamteamname.gotogothenburg.api.electricity.mock.MockRequestQueue;
import com.teamteamname.gotogothenburg.api.Bus;
import com.teamteamname.gotogothenburg.api.Stops;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Olof on 28/09/2015.
 */
public class ElectricityAPITest extends AndroidTestCase {
    private ElectricityAPI mElevtricityAPI;
    private MockRequestQueue mQueue;

    private static final String dgw = "Ericsson$100021";
    private Bus bus;
    private MockElectricityHandler mHandler;


    // Mock JSONArrays that is used to test the API's parsing of responses.
    //TODO: Make tests for empty responses.
    private final JSONArray testEmptyResponse = new JSONArray();

    @Override
    protected void setUp() throws Exception{
        super.setUp();

        Bus.init();
        bus = Bus.getBusByDgw(dgw);

        mHandler = new MockElectricityHandler();

        mQueue = new MockRequestQueue();

        mElevtricityAPI = new ElectricityAPI(this.getContext(), mQueue);
    }

    @Override
    protected void tearDown() throws Exception{
        bus = null;
        mHandler = null;
        mElevtricityAPI = null;
    }

    //Test so that ElectricityAPI creates a correct Request(correct URL, correct method, correct Auth header)
    public void testGPSRequest(){
        mElevtricityAPI.getBusLocation(bus, mHandler);

        final String mockURI = "https://ece01.ericsson.net:4443/ecity?dgw=" + bus.getDgw() + "&" + "sensorSpec=Ericsson$GPS2";
        //Removes the times, which is inpossible to predict.
        final String createdURI = mQueue.getUri().substring(0, mockURI.length());

        assertTrue(mQueue.getMethod() == 0);
        assertTrue(createdURI.equals(mockURI));
        assertTrue(mQueue.getHeader().get("Authorization").equals("Basic " + this.getContext().getResources().getString(R.string.electricity_api_key)));

    }

    public void testGPSResponse(){
        mElevtricityAPI.getBusLocation(bus, mHandler);

        final Response.ErrorListener errorListener = mQueue.getErrorListener();
        final Response.Listener parser = mQueue.getParser();

        //Tests parse on empty response
        parser.onResponse(testEmptyResponse);

        //Tests if it returns, in a correct format, the latest one of 2 GPSCoords in a response.
        JSONArray testGPSCoord = new JSONArray();
        try{
            testGPSCoord = new JSONArray("[{\"resourceSpec\":\"Latitude2_Value\",\"timestamp\":\"1\",\"value\":\"11.0\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Latitude2_Value\",\"timestamp\":\"2\",\"value\":\"5.0\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Longitude2_Value\",\"timestamp\":\"1\",\"value\":\"11.0\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Longitude2_Value\",\"timestamp\":\"2\",\"value\":\"5.0\",\"gatewayId\":\"Vin_Num_001\"}]");
        }catch (JSONException e){
            Log.e("TestError","Could not create a test JSONArray.");
        }
        parser.onResponse(testGPSCoord);
        final GPSCoord correctGPSCoord = new GPSCoord((float)5.0,(float)5.0);
        assertTrue(correctGPSCoord.equals(mHandler.getGpsResponse()));

    }

    public void testStopPressedResponse(){
        mElevtricityAPI.getStopPressed(bus, mHandler);

        final Response.ErrorListener errorListener = mQueue.getErrorListener();
        final Response.Listener parser = mQueue.getParser();

        //Tests parse on empty respponse
        parser.onResponse(testEmptyResponse);

        //Test if it returns in a correct format the latest of 2 StopPressed events.
        JSONArray testStopPressedCorrect = new JSONArray();
        try {
            testStopPressedCorrect = new JSONArray("[{\"resourceSpec\":\"Stop_Pressed_Value\",\"timestamp\":\"1\",\"value\":\"true\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Stop_Pressed_Value\",\"timestamp\":\"2\",\"value\":\"false\",\"gatewayId\":\"Vin_Num_001\"}]");
        }catch(JSONException e){
            Log.e("TestError","Could not create a test JSONArray.");

        }
        parser.onResponse(testStopPressedCorrect);
        final boolean correctStopPressed = false;
        assertTrue(mHandler.isPressedResponse()==correctStopPressed);
    }

    public void testCabinTempResponse(){
        mElevtricityAPI.getCabinTemperature(bus, mHandler);

        final Response.ErrorListener errorListener = mQueue.getErrorListener();
        final Response.Listener parser = mQueue.getParser();

        //Tests parse on empty respponse
        parser.onResponse(testEmptyResponse);

        //Test if it returns in a correct format the latest of 2 logged temperatures.
        JSONArray testCabinTempCorrect = new JSONArray();
        try {
            testCabinTempCorrect = new JSONArray("[{\"resourceSpec\":\"Driver_Cabin_Temperature_Value\",\"timestamp\":\"1\",\"value\":\"0.0\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Driver_Cabin_Temperature_Value\",\"timestamp\":\"2\",\"value\":\"100.0\",\"gatewayId\":\"Vin_Num_001\"}]");
        }catch(JSONException e){
            Log.e("TestError","Could not create a test JSONArray.");

        }
        parser.onResponse(testCabinTempCorrect);
        final double correctTemp = 100.0;
        assertTrue(mHandler.getCabinTempResponse()==correctTemp);
    }

    public void testAmbientTempResponse(){
        mElevtricityAPI.getAmbientTemperature(bus, mHandler);

        final Response.ErrorListener errorListener = mQueue.getErrorListener();
        final Response.Listener parser = mQueue.getParser();

        //Tests parse on empty respponse
        parser.onResponse(testEmptyResponse);

        //Test if it returns in a correct format the latest of 2 logged temperatures.
        JSONArray testAmbientTempCorrect = new JSONArray();
        try {
            testAmbientTempCorrect = new JSONArray("[{\"resourceSpec\":\"Ambient_Temperature_Value\",\"timestamp\":\"1\",\"value\":\"0.0\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Ambient_Temperature_Value\",\"timestamp\":\"2\",\"value\":\"100.0\",\"gatewayId\":\"Vin_Num_001\"}]");
        }catch(JSONException e){
            Log.e("TestError","Could not create a test JSONArray.");

        }
        parser.onResponse(testAmbientTempCorrect);
        final double correctTemp = 100.0;
        assertTrue(mHandler.getAmbientTempResponse()==correctTemp);
    }

    public void testWifiUsersResponse(){
        mElevtricityAPI.getNbrOfWifiUsers(bus, mHandler);

        final Response.ErrorListener errorListener = mQueue.getErrorListener();
        final Response.Listener parser = mQueue.getParser();

        //Tests parse on empty respponse
        //parser.onResponse(testEmptyResponse);

        //Test if it returns in a correct format the latest of 2 logged temperatures.
        JSONArray testWifiUsersCorrect = new JSONArray();
        try {
            testWifiUsersCorrect = new JSONArray("[{\"resourceSpec\":\"Total_Online_Users_Value\",\"timestamp\":\"1\",\"value\":\"1\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Total_Online_Users_Value\",\"timestamp\":\"2\",\"value\":\"100\",\"gatewayId\":\"Vin_Num_001\"}]");
        }catch(JSONException e){
            Log.e("TestError","Could not create a test JSONArray in WifiUsers test.");

        }
        parser.onResponse(testWifiUsersCorrect);
        final int correctNbrOfUsers = 100;
        final int response = mHandler.getNbrOfUsersResponse();
        assertTrue(response==correctNbrOfUsers);
    }

    public void testNextStopResponse(){
        mElevtricityAPI.getNextStop(bus, mHandler);

        final Response.ErrorListener errorListener = mQueue.getErrorListener();
        final Response.Listener parser = mQueue.getParser();

        JSONArray testNextStopCorrect = new JSONArray();
        try{
            testNextStopCorrect = new JSONArray("[{\"resourceSpec\":\"Bus_Stop_Name_Value\",\"timestamp\":\"1\",\"value\":\"Brunnsparken\",\"gatewayId\":\"Vin_Num_001\"},{\"resourceSpec\":\"Bus_Stop_Name_Value\",\"timestamp\":\"2\",\"value\":\"Regnb%C3%A5gsgatan\",\"gatewayId\":\"Vin_Num_001\"}]");
        }catch(JSONException e){
            Log.e("TestError","Could not create test JSONArray in NextStop test.");
        }
        parser.onResponse(testNextStopCorrect);
        final Stops correctStop = Stops.REGNBÅGSGATAN;
        final Stops response = mHandler.getNextStopResponse();
        assertTrue(response.equals(correctStop));
    }

}
