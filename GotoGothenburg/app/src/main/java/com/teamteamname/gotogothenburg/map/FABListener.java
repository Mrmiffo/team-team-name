package com.teamteamname.gotogothenburg.map;

import android.util.TypedValue;
import android.view.View;

import com.teamteamname.gotogothenburg.R;

/**
 * Created by patrick on 15/10/2015.
 */
public class FABListener implements View.OnClickListener {
    private IMap map;
    private IMapMarkerData markerData;
    private boolean isDisplaying = false;

    public FABListener(IMapMarkerData markerData, IMap map) {
        this.markerData = markerData;
        this.map = map;
    }

    @Override
    public void onClick(View v) {
        TypedValue value = new TypedValue();
        if (isDisplaying) {
            markerData.removeMarkers();
            v.getResources().getValue(R.dimen.BUTTON_UNPRESSED_ALPHA, value, true);
        } else {
            markerData.addMarkers(map);
            v.getResources().getValue(R.dimen.BUTTON_PRESSED_ALPHA, value, true);

        }
        v.setAlpha(value.getFloat());
        isDisplaying = !isDisplaying;
    }
}
