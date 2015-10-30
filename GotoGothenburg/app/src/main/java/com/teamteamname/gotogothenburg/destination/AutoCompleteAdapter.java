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

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataBufferUtils;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by patrick on 02/10/2015.
 */
public class AutoCompleteAdapter extends ArrayAdapter<AutocompletePrediction> implements Filterable {

    private List<AutocompletePrediction> results;
    final private GoogleApiClient placesAPI;
    final private LatLngBounds bounds;
    final private AutocompleteFilter placeFilter;

    public AutoCompleteAdapter(Context context, GoogleApiClient apiClient, AutocompleteFilter filter) {
        super(context, android.R.layout.simple_expandable_list_item_1, android.R.id.text1);
        placesAPI = apiClient;
        placeFilter = filter;
        bounds = new LatLngBounds(new LatLng(57.595402, 11.762982), new LatLng(57.753259, 12.068711));
    }

    @Override
    public int getCount(){
        return results.size();
    }

    @Override
    public AutocompletePrediction getItem(int position) {
        return results.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final View row = super.getView(position, convertView, parent);

        // Sets the primary and secondary text for a row.
        // Note that getPrimaryText() and getSecondaryText() return a CharSequence that may contain
        // styling based on the given CharacterStyle.

        final AutocompletePrediction item = getItem(position);

        final TextView textView1 = (TextView) row.findViewById(android.R.id.text1);
        if(item.getDescription().indexOf(',') == -1) {
            textView1.setText(item.getDescription());

        } else {
            textView1.setText(item.getDescription().substring(0, item.getDescription().indexOf(',')));
        }
        return row;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults filterResults = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    results = getAutocomplete(constraint);
                    if (results != null) {
                        // The API successfully returned filterResults.
                        filterResults.values = results;
                        filterResults.count = results.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                if (filterResults != null && filterResults.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    notifyDataSetInvalidated();
                }
            }

            @Override
            public CharSequence convertResultToString(Object resultValue) {
                // Override this method to display a readable result in the AutocompleteTextView
                // when clicked.
                if (resultValue instanceof AutocompletePrediction) {
                    return ((AutocompletePrediction) resultValue).getDescription();
                } else {
                    return super.convertResultToString(resultValue);
                }
            }
        };
    }

    private List<AutocompletePrediction> getAutocomplete(CharSequence constraint) {
        if (placesAPI.isConnected()) {

            // Submit the query to the autocomplete API and retrieve a PendingResult that will
            // contain the results when the query completes.
            final PendingResult<AutocompletePredictionBuffer> results =
                    Places.GeoDataApi
                            .getAutocompletePredictions(placesAPI, constraint.toString(),
                                    bounds, placeFilter);

            // This method should have been called off the main UI thread. Block and wait for at most 60s
            // for a result from the API.
            final AutocompletePredictionBuffer autocompletePredictions = results
                    .await(60, TimeUnit.SECONDS);

            // Confirm that the query completed successfully, otherwise return null
            final Status status = autocompletePredictions.getStatus();
            if (!status.isSuccess()) {
                Toast.makeText(getContext(), "Error contacting API: " + status.toString(),
                        Toast.LENGTH_SHORT).show();
                Log.e("AutoCompleteAdapter", "Error getting autocomplete prediction API call: " + status.toString());
                autocompletePredictions.release();
                return null;
            }

            Log.i("AutoCompleteAdapter", "Query completed. Received " + autocompletePredictions.getCount()
                    + " predictions.");

            // Freeze the results immutable representation that can be stored safely.
            return DataBufferUtils.freezeAndClose(autocompletePredictions);
        }
        Log.e("AutoCompleteAdapter", "Google API client is not connected for autocomplete query.");
        return null;
    }
}
