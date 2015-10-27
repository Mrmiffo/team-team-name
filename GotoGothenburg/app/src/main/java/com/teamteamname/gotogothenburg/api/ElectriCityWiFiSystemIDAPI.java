package com.teamteamname.gotogothenburg.api;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 * An API for reading bus data.
 * Created by Anton on 2015-10-01.
 */
public class ElectriCityWiFiSystemIDAPI implements IElectriCityWiFiSystemIDAPI {
    private static ElectriCityWiFiSystemIDAPI instance;
    private static final String TARGET_URL= "http://www.ombord.info/api/xml/system/";

    private ElectriCityWiFiSystemIDAPI(){

    }

    public synchronized static ElectriCityWiFiSystemIDAPI getInstance(){
        if (instance == null){
            instance = new ElectriCityWiFiSystemIDAPI();
        }
        return instance;
    }

    @Override
    public void getConnectedBusSystemID(IElectriCityWiFiSystemIDAPIHandler handler) {
        try {
            URL url = new URL(TARGET_URL);
            /* Get a SAXParser from the SAXPArserFactory. */
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();

            /* Get the XMLReader of the SAXParser we created. */
            XMLReader xmlReader = sp.getXMLReader();
            /* Create a new ContentHandler and apply it to the XML-Reader*/
            SystemIDParser myHandler = new SystemIDParser(handler);
            xmlReader.setContentHandler(myHandler);

            /* Parse the xml-data from our URL. */
            xmlReader.parse(new InputSource(url.openStream()));
            /* Parsing has finished. */
        } catch (MalformedURLException e) {
            //This error is swallowed as it should only occur if the URL is incorrect, and the URL
            //is hard coded into this class and should not change.
            Log.e("MalformedURLException", e.toString());
        } catch (ParserConfigurationException e) {
            //This error is swallowed as it should only occur if the parser is incorrectly setup,
            // something the user can't do anything about.
            Log.e("ParserConfigurationExc", e.toString());
        } catch (SAXException e) {
            //This error is swallowed as it's nothing the user can do anything about.
            Log.e("SAXException", e.toString());
        } catch (IOException e) {
            Log.e("IOException", e.toString());
            handler.connectedBusErrorCallback(e);
        }
    }

    private static class SystemIDParser extends DefaultHandler{
        private IElectriCityWiFiSystemIDAPIHandler handler;
        SystemIDParser(IElectriCityWiFiSystemIDAPIHandler handler){

            this.handler = handler;
        }
        private boolean readSystemID;
        private final static String SYSTEM_ID_XML_QNAME = "system_id";

        /** Gets be called on the following structure:
         * <tag>characters</tag>
         */
        @Override
        public void characters(char ch[], int start, int length) {
            if (readSystemID){
                String xmlText = new String(ch, start, length);
                readSystemID = false;
                handler.connectedBusSystemIDCallback(xmlText);
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes){
            if (SYSTEM_ID_XML_QNAME.equals(qName)){
                readSystemID = true;
            }
        }
    }
}
