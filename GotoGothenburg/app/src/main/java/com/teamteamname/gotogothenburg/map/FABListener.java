package com.teamteamname.gotogothenburg.map;

import android.view.View;

/**
 * Created by patrick on 15/10/2015.
 */
public class FABListener implements View.OnClickListener {
    private MapFragment mapFragment;
    private IMapMarkerData markerData;
    private boolean isDisplaying = false;

    public FABListener(IMapMarkerData markerData, MapFragment mapFragment) {
        this.markerData = markerData;
        this.mapFragment = mapFragment;
    }

    @Override
    public void onClick(View v) {
        if (isDisplaying) {
            markerData.removeMarkers();
            v.setAlpha(0.5f);
        } else {
            markerData.addMarkers(mapFragment);
            v.setAlpha(1f);
        }
        isDisplaying = !isDisplaying;
    }
}
