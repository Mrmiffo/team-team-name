package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.VasttrafikChange;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.GeoCallback;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.VasttrafikTripHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-16.
 */
public class GeoManager implements GeoCallback {

    private VasttrafikTripHandler tripCallback;
    private VasttrafikErrorHandler errorCallback;

    private RequestQueue queue;
    private int nrOfPolylines;

    private List<String> urls;
    private List<VasttrafikChange> trips;
    private List<Boolean> walks;

    private List<List<PolylineOptions>> polylines = new ArrayList<>();
    private List<VasttrafikChange> markers = new ArrayList<>();

    public GeoManager(VasttrafikTripHandler tripCallback, VasttrafikErrorHandler errorCallback,
                      RequestQueue queue, int nrOfPolylines,
                      List<String> urls, List<VasttrafikChange> trips, List<Boolean> walks){

        this.tripCallback = tripCallback;
        this.errorCallback = errorCallback;

        this.queue = queue;
        this.nrOfPolylines = nrOfPolylines;

        this.urls = urls;
        this.trips = trips;
        this.walks = walks;

        for(int i = 0; i < nrOfPolylines; i++){
            GeoParser parser = new GeoParser(this, queue, urls.get(i), trips.get(i), walks.get(i));
            JsonObjectRequest request = new JsonObjectRequest(urls.get(i), null, parser, parser);
            queue.add(request);
        }
    }

    @Override
    public void polylineRequestDone(List<PolylineOptions> polyline) {
        polylines.add(polyline);
        done();
    }

    @Override
    public void markerRequestDone(VasttrafikChange trip) {
        markers.add(trip);
        done();
    }

    private void done(){
        if(polylines.size() == nrOfPolylines && markers.size() == nrOfPolylines){
            List<PolylineOptions> temp = new ArrayList<>();
            for(List<PolylineOptions> lpo : polylines){
                for(PolylineOptions po : lpo){
                    temp.add(po);
                }
            }
            tripCallback.vasttrafikRequestDone( markers.toArray(new VasttrafikChange[markers.size()]),
                                                temp.toArray(new PolylineOptions[temp.size()]));
        }
    }
}
