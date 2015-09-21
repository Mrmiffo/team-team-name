package API;

import java.net.HttpURLConnection;

/**
 * Created by Olof on 21/09/2015.
 */
public class APIHandler implements IAPIHandler {

    private String url = "https://ece01.ericsson.net:4443/ecity";

    private void preperation(){

        URL url = new URL(url);
        long t2 = System.currentTimeMillis();
        long t1 = t2 - (1000*5);
        t2 = t2 - (1000);
    }


    @Override
    public String getGPSPosition() {

    }
}
