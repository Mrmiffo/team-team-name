package com.teamteamname.gotogothenburg.map;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/**
 * Created by Mattias Ahlstedt on 2015-09-22.
 */
public class Bus {

    @Getter private String dgw;
    @Getter private String vin;
    @Getter private String regNr;
    @Getter private String mac;
    @Getter private Boolean active;
    private static List<Bus> busses = new ArrayList<Bus>();
    private static Boolean initiated;

    private Bus(String dgw, String vin, String regNr, String mac, Boolean active){
        this.dgw = dgw;
        this.vin = vin;
        this.regNr = regNr;
        this.mac = mac;
        this.active = active;
    }

    /**
     * Creates a new bus with the given parameters as properties and then adds it to the list of busses
     *
     * @param dgw       The DomainGateWay of the bus
     * @param vin       The VehicleIdentificationNumber of the bus
     * @param regNr     The RegistrationNumber of the bus
     * @param mac       The MAC-address of the router on the bus
     * @param active    True if the bus is sending data
     */
    private static void addBus(String dgw, String vin, String regNr, String mac, Boolean active){
        Bus bus = new Bus(dgw, vin, regNr, mac, active);
        busses.add(bus);
    }

    public static void init(){
        if(initiated == false) {
            addBus("Ericsson$100020", "YV3U0V222FA100020", "EPO 131", "0013951349f5", false);
            addBus("Ericsson$100021", "YV3U0V222FA100021", "EPO 136", "001395134bbe", true);
            addBus("Ericsson$100022", "YV3U0V222FA100022", "EPO 143", "001395143bf0", false);
            addBus("Ericsson$171164", "YV3T1U22XF1171164", "EOG 604", "00139514698a", false);
            addBus("Ericsson$171234", "YV3T1U225F1171234", "EOG 606", "0013951349f7", false);
            addBus("Ericsson$171235", "YV3T1U227F1171235", "EOG 616", "0013950f92a4", false);
            addBus("Ericsson$171327", "YV3T1U221F1171327", "EOG 622", "001395136296", false);
            addBus("Ericsson$171328", "YV3T1U223F1171328", "EOG 627", "001395134bbc", false);
            addBus("Ericsson$171329", "YV3T1U225F1171329", "EOG 631", "001395143bf2", false);
            addBus("Ericsson$171330", "YV3T1U223F1171330", "EOG 634", "001395135f20", false);
            initiated = true;
        }
    }

    /**
     * @param dgw   The DomainGateWay of the required bus
     * @return      A copy of the bus with the given DomainGateWay
     */
    public static Bus getBusByDgw(String dgw){
        for(Bus b : busses){
            if(b.getDgw().equals(dgw)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getMac(), b.getActive());
            }
        }
        return null;
    }

    /**
     * @param vin   The VehicleIdentificationNumber of the required bus
     * @return      A copy of the bus with the given VehicleIdentificationNumber
     */
    public static Bus getBusByVin(String vin){
        for(Bus b : busses){
            if(b.getVin().equals(vin)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getMac(), b.getActive());
            }
        }
        return null;
    }

    /**
     * @param regNr The RegistrationNumber of the required bus
     * @return      A copy of the bus with the given RegistrationNumber
     */
    public static Bus getBusByRegNr(String regNr){
        for(Bus b : busses){
            if(b.getRegNr().equals(regNr)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getMac(), b.getActive());
            }
        }
        return null;
    }

    /**
     * @param mac   The MAC-address of the router of the required bus
     * @return      A copy of the bus with the router with the given MAC-address
     */
    public static Bus getBusByMac(String mac){
        for(Bus b : busses){
            if(b.getMac().equals(mac)){
                return new Bus(b.getDgw(), b.getVin(), b.getRegNr(), b.getMac(), b.getActive());
            }
        }
        return null;
    }
}
