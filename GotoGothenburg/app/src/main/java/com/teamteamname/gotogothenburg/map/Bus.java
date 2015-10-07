package com.teamteamname.gotogothenburg.map;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;


/**
 * Created by Mattias Ahlstedt on 2015-09-22.
 */
public class Bus {

    @Getter final private String dgw;
    @Getter final private String vin;
    @Getter final private String regNr;
    @Getter final private String sysId;
    @Getter private boolean active;
    private static List<Bus> buses = new ArrayList<>();
    private static boolean initiated;

    private Bus(String dgw, String vin, String regNr, String sysId, boolean active){
        this.dgw = dgw;
        this.vin = vin;
        this.regNr = regNr;
        this.sysId = sysId;
        this.active = active;
    }

    /**
     * Creates a new bus with the given parameters as properties and then adds it to the list of buses
     *
     * @param dgw       The DomainGateWay of the bus
     * @param vin       The VehicleIdentificationNumber of the bus
     * @param regNr     The RegistrationNumber of the bus
     * @param sysId     The systemID of the router on the bus
     * @param active    True if the bus is sending data
     */
    private static void addBus(String dgw, String vin, String regNr, String sysId, boolean active){
        final Bus bus = new Bus(dgw, vin, regNr, sysId, active);
        buses.add(bus);
    }

    public static void init(){
        if(initiated) {
            addBus("Ericsson$100020", "YV3U0V222FA100020", "EPO 131", "2501069301", false);
            addBus("Ericsson$100021", "YV3U0V222FA100021", "EPO 136", "2501069758", true);
            addBus("Ericsson$100022", "YV3U0V222FA100022", "EPO 143", "2501131248", false);
            addBus("Ericsson$171164", "YV3T1U22XF1171164", "EOG 604", "2501142922", false);
            addBus("Ericsson$171234", "YV3T1U225F1171234", "EOG 606", "2501069303", false);
            addBus("Ericsson$171235", "YV3T1U227F1171235", "EOG 616", "2500825764", false);
            addBus("Ericsson$171327", "YV3T1U221F1171327", "EOG 622", "2501075606", false);
            addBus("Ericsson$171328", "YV3T1U223F1171328", "EOG 627", "2501069756", false);
            addBus("Ericsson$171329", "YV3T1U225F1171329", "EOG 631", "2501131250", false);
            addBus("Ericsson$171330", "YV3T1U223F1171330", "EOG 634", "2501074720", false);
            initiated = true;
        }
    }

    /**
     * @param dgw   The DomainGateWay of the required bus
     * @return      A copy of the bus with the given DomainGateWay
     */
    public static Bus getBusByDgw(String dgw){
        for(final Bus b : buses){
            if(b.getDgw().equals(dgw)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getSysId(), b.isActive());
            }
        }
        return null;
    }

    /**
     * @param vin   The VehicleIdentificationNumber of the required bus
     * @return      A copy of the bus with the given VehicleIdentificationNumber
     */
    public static Bus getBusByVin(String vin){
        for(final Bus b : buses){
            if(b.getVin().equals(vin)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getSysId(), b.isActive());
            }
        }
        return null;
    }

    /**
     * @param regNr The RegistrationNumber of the required bus
     * @return      A copy of the bus with the given RegistrationNumber
     */
    public static Bus getBusByRegNr(String regNr){
        for(final Bus b : buses){
            if(b.getRegNr().equals(regNr)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getSysId(), b.isActive());
            }
        }
        return null;
    }

    /**
     * @param sysId   The MAC-address of the router of the required bus
     * @return      A copy of the bus with the router with the given MAC-address
     */
    public static Bus getBusBySysId(String sysId){
        for(final Bus b : buses){
            if(b.getSysId().equals(sysId)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getSysId(), b.isActive());
            }
        }
        return null;
    }


}
