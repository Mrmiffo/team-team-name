package com.teamteamname.gotogothenburg.api.vasttrafik.parsers;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.GeoCallback;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.TripHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-10-16.
 */
public class GeoManager implements GeoCallback {

    private TripHandler tripCallback;
    private ErrorHandler errorCallback; //Will be used in future version

    private int nrOfPolylines;

    private List<List<PolylineOptions>> polylines = new ArrayList<>();
    private List<Change> markers = new ArrayList<>();

    public GeoManager(TripHandler tripCallback, ErrorHandler errorCallback,
                      RequestQueue queue, int nrOfPolylines,
                      List<String> urls, List<Change> trips, List<Boolean> walks){

        this.tripCallback = tripCallback;
        this.errorCallback = errorCallback;

        this.nrOfPolylines = nrOfPolylines;

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
    public void markerRequestDone(String line, String stopName, String track, LatLng position) {
        markers.add(new Change(line, stopName, track, position));
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

            String[] lines = new String[markers.size()];
            String[] stopNames = new String[markers.size()];
            String[] tracks = new String[markers.size()];
            LatLng[] positions = new LatLng[markers.size()];
            for(int i = 0; i < markers.size(); i++){
                lines[i] = markers.get(i).getLine();
                stopNames[i] = markers.get(i).getTrack();
                tracks[i] = markers.get(i).getTrack();
                positions[i] = markers.get(i).getPosition();
            }
            tripCallback.RequestDone(lines, stopNames, tracks, positions, temp.toArray(new PolylineOptions[temp.size()]));
        }
    }
}
