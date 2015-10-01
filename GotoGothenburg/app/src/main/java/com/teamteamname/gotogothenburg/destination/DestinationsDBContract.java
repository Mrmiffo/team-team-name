package com.teamteamname.gotogothenburg.destination;

import android.provider.BaseColumns;

/**
 * Created by Anton on 2015-09-30.
 */
public class DestinationsDBContract {
    public DestinationsDBContract(){}

    public static abstract class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "savedDestinations";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_NAME_TITLE = "destination";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    }
}
