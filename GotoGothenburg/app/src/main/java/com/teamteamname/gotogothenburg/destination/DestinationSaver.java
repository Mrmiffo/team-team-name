package com.teamteamname.gotogothenburg.destination;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * The destination saver is a class used by the SavedDestination to register changes into a database
 * stored on the local device.
 * NOTE: ANY CHANGE TO THIS CLASS MUST UPDATE THE onUpgrade METHOD AND INCREASE THE DATABASE_VERSION
 * +1 IN ORDER TO UPDATE THE DATABASE ON THE DEVICE
 * Created by Anton on 2015-10-06.
 */
public class DestinationSaver implements IDestinationSaver{
    //An instance of the DB reader helper class for destinations. Local class.
    private DestinationDBReaderHelper destDBReader;

    //Static strings for standardized SQL injections.
    private static final String TEXT_TYPE = " TEXT";
    private static final String DOUBLE_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + DestinationEntry.TABLE_NAME + " (" +
                    DestinationEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    DestinationEntry.COLUMN_NAME_LATITUDE + DOUBLE_TYPE + COMMA_SEP +
                    DestinationEntry.COLUMN_NAME_LONGITUDE  + DOUBLE_TYPE + COMMA_SEP +
                    DestinationEntry.COLUMN_NAME_VISITED  + TEXT_TYPE +
                    " )";

    /**
     * Default constructor require a context in order to access the DB stored in the local device allocated to the app.
     * @param context of the application.
     */
    public DestinationSaver(Context context){
        destDBReader = new DestinationDBReaderHelper(context);
    }


    //Local method used by the save method to put data in the db.
    private void putDestination(Destination dest) {
        SQLiteDatabase db = destDBReader.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DestinationEntry.COLUMN_NAME_NAME, dest.getName());
        values.put(DestinationEntry.COLUMN_NAME_LATITUDE, dest.getLatitude());
        values.put(DestinationEntry.COLUMN_NAME_LONGITUDE, dest.getLongitude());
        values.put(DestinationEntry.COLUMN_NAME_VISITED, dest.isVisited());
        db.insert(
                DestinationEntry.TABLE_NAME,
                null,
                values);
        }


    @Override
    public void saveAll(final List<Destination> destinationsToSave) {
        AsyncTask thread = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                //Drop the table to clean it from all previous destinations.
                dropTable();
                for (Destination dest: destinationsToSave) {
                    putDestination(dest);
                }
                return null;
            }
        };
        //noinspection unchecked
        thread.execute();

    }

    @Override
    public void save(final Destination destinationToSave) {
        AsyncTask thread = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                putDestination(destinationToSave);
                return null;
            }
        };
        //noinspection unchecked
        thread.execute();
    }

    @Override
    public List<Destination> loadAll() {
        List<Destination> toReturn = new ArrayList<>();
        SQLiteDatabase db = destDBReader.getReadableDatabase();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                DestinationEntry.COLUMN_NAME_NAME,
                DestinationEntry.COLUMN_NAME_LATITUDE,
                DestinationEntry.COLUMN_NAME_LONGITUDE,
                DestinationEntry.COLUMN_NAME_VISITED
        };

        Cursor c = db.query(
                DestinationEntry.TABLE_NAME,                // From
                projection,                                 // Select
                null,                                       // Where
                null,                                       // = X or Y
                null,                                       // Group by
                null,                                       // Having
                null                                        // Order by
        );
        if (c.moveToFirst()) {
            toReturn.add(createDestination(c));
            while (c.moveToNext()) {
                toReturn.add(createDestination(c));
            }
        }
        return toReturn;
    }

    @Override
    public void removeDestination(final Destination destinationToRemove) {
        AsyncTask thread = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                destDBReader.getWritableDatabase().delete(
                        DestinationEntry.TABLE_NAME,
                        DestinationEntry.COLUMN_NAME_NAME + "= ?",
                        new String[]{destinationToRemove.getName()});
                return null;
            }
        };
        //noinspection unchecked
        thread.execute();

    }

    @Override
    public void removeAllDestinations() {
        AsyncTask thread = new AsyncTask<Object, Void, Void>() {
            @Override
            protected Void doInBackground(Object... params) {
                // Drop table
                dropTable();
                return null;
            }
        };
        //noinspection unchecked
        thread.execute();

    }

    //Local method used to remove all entries in the database.
    private void dropTable() {
        final String SQL_DELETE_ENTRIES =
                "delete from " + DestinationEntry.TABLE_NAME;
        SQLiteDatabase db = destDBReader.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
    }

    //Local method used to extract a destination from a CursorQuery.
    private Destination createDestination(Cursor data){
        String name = data.getString(data.getColumnIndexOrThrow(DestinationEntry.COLUMN_NAME_NAME));
        double lat = data.getDouble(data.getColumnIndexOrThrow(DestinationEntry.COLUMN_NAME_LATITUDE));
        double lon = data.getDouble(data.getColumnIndexOrThrow(DestinationEntry.COLUMN_NAME_LONGITUDE));
        boolean visited = Boolean.parseBoolean(data.getString(data.getColumnIndexOrThrow(DestinationEntry.COLUMN_NAME_VISITED)));
        return new Destination(name, lat, lon, visited);
    }


    //Local class used as a reference to all the tables in the database.
    private static abstract class DestinationEntry implements BaseColumns {
        public static final String TABLE_NAME = "savedDestinations";
        public static final String COLUMN_NAME_NAME = "destination_name";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_VISITED = "visited";

    }

    //Local class used to provide and create a read and writable database on the local device.
    private static class DestinationDBReaderHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "SavedDestinations.db";

        public DestinationDBReaderHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO Implement onUpgrade if necessary
        }
    }
}
