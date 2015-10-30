package com.teamteamname.gotogothenburg.api;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Olof on 16/10/2015.
 */
public class ApiRequestError {
    @Getter private String message;
    @Getter private Map<String,String> responseHeaders;
    @Getter private int responseStatusCode;
    @Getter private long networkTimeMs;

    public ApiRequestError(String message, Map<String, String> responseHeaders,
                           int responseStatusCode, long networkTimeMs){
        this.message = message;
        this.responseHeaders = responseHeaders;
        this.responseStatusCode = responseStatusCode;
        this.networkTimeMs = networkTimeMs;
    }
}
