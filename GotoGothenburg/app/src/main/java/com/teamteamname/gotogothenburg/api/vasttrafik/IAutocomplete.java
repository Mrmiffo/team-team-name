package com.teamteamname.gotogothenburg.api.vasttrafik;

import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.AutocompleteHandler;
import com.teamteamname.gotogothenburg.api.vasttrafik.callbacks.ErrorHandler;

/**
 * Created by Mattias Ahlstedt on 2015-10-27.
 */
public interface IAutocomplete {
    /**
     * Sends a list of autocomplete suggestions to the callback
     * The suggestions are based on the string "input" which is supplied.
     * @param autoCallback  The interface which is to be supplied with the result
     * @param errorCallback The interface which is to be supplied with an eventual error
     * @param input     The input which is the base for the autocomplete suggestions
     */
    void getAutocomplete(AutocompleteHandler autoCallback, ErrorHandler errorCallback, String input);
}
