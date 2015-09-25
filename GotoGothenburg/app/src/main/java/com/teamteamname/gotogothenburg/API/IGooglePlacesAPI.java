package com.teamteamname.gotogothenburg.API;

import java.util.List;

/**
 * Created by Mattias Ahlstedt on 2015-09-24.
 */
public interface IGooglePlacesAPI {
    public List<String> autoComplete(String s);
}
