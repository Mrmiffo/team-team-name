package com.teamteamname.gotogothenburg.destination;
/*
 * Copyright (C) 2015 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import android.app.Fragment;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.teamteamname.gotogothenburg.R;

/**
 * A fragment for creating new Destinations.
 *
 * Most of code is from the Google Reference example but, of course, tailored to fit the current
 * situation.
 */
public class CreateNewDestinationFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener{

    /*
    Callback for creating the new Destination
     */
    private GoogleApiClient locationsAPI;
    private AutoCompleteAdapter adapter;
    private TextView destinationDetails;
    private TextView destinationAttribution;
    private String selectedPlaceName;
    private LatLng selectedPlacePos;
    private SavedDestinations savedDestinations;

    /**
     * Creates a new instance of CreateNewDestinationFragment unless an existing Fragment already exists
     *
     * @return The created CreateNewDestinationFragment
     */
    public static CreateNewDestinationFragment newInstance() {
        CreateNewDestinationFragment instance = new CreateNewDestinationFragment();
        instance.savedDestinations = SavedDestinations.getInstance();
        return instance;
    }

    public CreateNewDestinationFragment() {}

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        locationsAPI = new GoogleApiClient
                .Builder(getActivity())
                .enableAutoManage((FragmentActivity) getActivity(), 0, this)
                .addApi(Places.GEO_DATA_API).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_new_destination, container, false);

        adapter = new AutoCompleteAdapter(getActivity(), locationsAPI, null);

        destinationDetails = (TextView) view.findViewById(R.id.destinationDetails);
        destinationAttribution = (TextView) view.findViewById(R.id.destinationAttribution);

        final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) view.findViewById(R.id.destinationName);
        autoCompleteTextView.setOnItemClickListener(autocompleteClickListener);
        autoCompleteTextView.setAdapter(adapter);

        view.findViewById(R.id.cancelDestination).setOnClickListener(cancelDestinationListener);

        view.findViewById(R.id.createDestinationButton).setOnClickListener(createDestinationClickListener);

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        locationsAPI.stopAutoManage((FragmentActivity)getActivity());
    }

    /**
     * Listener that hides the CreateNewDestinationFragment and lets the user return to the program.
     */
    private View.OnClickListener cancelDestinationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down).remove(CreateNewDestinationFragment.this).commit();
        }
    };

    /**
     * Sends the selected information to DestinationFragment
     */
    private View.OnClickListener createDestinationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            savedDestinations.addDestination(new Destination(selectedPlaceName, selectedPlacePos.latitude, selectedPlacePos.longitude));
            getFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_down).remove(CreateNewDestinationFragment.this).commit();
        }
    };

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener autocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = adapter.getItem(position);
            final String placeId = item.getPlaceId();

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(locationsAPI, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("NewDestinationFragment", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }

            final Place place = places.get(0);
            selectedPlaceName = place.getName().toString();
            selectedPlacePos = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);

            // Format details of the place for display and show it in a TextView.
            destinationDetails.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                destinationAttribution.setVisibility(View.GONE);
            } else {
                destinationAttribution.setVisibility(View.VISIBLE);
                destinationAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }

            places.release();
        }
    };

    /*
    Helper method for formatting information about selected location
     */
    private static Spanned formatPlaceDetails(Resources res, CharSequence name, CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name, address, phoneNumber,
                websiteUri == null ? "" : websiteUri));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Places API", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        Toast.makeText(getActivity(),
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }
}
