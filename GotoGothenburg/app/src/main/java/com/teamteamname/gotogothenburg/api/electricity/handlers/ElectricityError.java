package com.teamteamname.gotogothenburg.api.electricity.handlers;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Olof on 16/10/2015.
 */
public class ElectricityError {
    @Getter @Setter private String message;
    @Getter @Setter private String toString;
    @Getter @Setter private Map<String,String> responseHeaders;
    @Getter @Setter private int responseStatusCode;
    @Getter @Setter private long networkTimeMs;
}
