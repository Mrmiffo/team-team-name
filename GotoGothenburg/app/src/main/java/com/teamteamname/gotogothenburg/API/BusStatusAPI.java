package com.teamteamname.gotogothenburg.API;

import android.util.Log;

import com.android.volley.RequestQueue;

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
public class BusStatusAPI implements IBusStatusAPI{
    private static BusStatusAPI instance;
    private static final String TARGET_URL= "ombord.info/api/XML/system/";

    private RequestQueue queue;

    private BusStatusAPI(){

    }

    public synchronized static BusStatusAPI getInstance(){
        return instance;
    }

    public static void initialize(){
        if (instance == null){
            instance = new BusStatusAPI();
        }

    }


    @Override
    public void getConnectedBusSystemID(IBusStatusHandler handler) {
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
            Log.e("MalformedURLException", e.toString());
            handler.getConnectedBusSystemIDCallback(null);
        } catch (ParserConfigurationException e) {
            Log.e("ParserConfigurationException", e.toString());
            handler.getConnectedBusSystemIDCallback(null);
        } catch (SAXException e) {
            Log.e("SAXException", e.toString());
            handler.getConnectedBusSystemIDCallback(null);
        } catch (IOException e) {
            Log.e("IOException", e.toString());
            handler.getConnectedBusSystemIDCallback(null);
        }
    }

    private class SystemIDParser extends DefaultHandler{
        private IBusStatusHandler handler;
        SystemIDParser(IBusStatusHandler handler){
            this.handler = handler;
        }

        /** Gets be called on the following structure:
         * <tag>characters</tag>
         */
        @Override
        public void characters(char ch[], int start, int length) {
            String xmlText = new String(ch, start, length);
            Log.e("XML:", xmlText);
            //handler.getConnectedBusSystemIDCallback(xmlText);
        }
    }
}
