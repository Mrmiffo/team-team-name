package com.teamteamname.gotogothenburg.information;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.teamteamname.gotogothenburg.R;


/**
 * This class represents the Info screen and simply provides the 4 text fields with data.
 * Created by Anton on 2015-09-21.
 */
public class InfoScreen extends Fragment {

    public static InfoScreen newInstance(){
        return new InfoScreen();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View toReturn = inflater.inflate(R.layout.fragment_info, container, false);
        //Fetch all text fields.
        final TextView ticketTitle = (TextView) toReturn.findViewById(R.id.ticket_title);
        final TextView ticketText = (TextView) toReturn.findViewById(R.id.ticket_text);
        final TextView howToTravelTitle = (TextView) toReturn.findViewById(R.id.how_to_travel_title);
        final TextView howToTravelText = (TextView) toReturn.findViewById(R.id.how_to_travel_text);

        //Add text from the @string
        ticketTitle.setText(R.string.info_frag_ticket_title);
        ticketText.setText(R.string.info_frag_ticket_text);
        howToTravelTitle.setText(R.string.info_frag_how_to_travel_title);
        howToTravelText.setText(R.string.info_frag_how_to_travel_text);

        return toReturn;
    }



}
