package com.zackstrikesback.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class FinancialProvider extends ContentProvider {

    public static final String AUTHORITY = "com.zackstrikesback.contentprovider.provider.Financial";
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int FINANCIAL = 1;
    private static final int FINANCIAL_ID = 2;
    static final Uri CONTENT_URI = Uri.parse(AUTHORITY);
    private static HashMap<String, String> FINANCIAL_PROJECTION_MAP;
    static final String _ID = "transaction_id";
    static final String DATE = "date";
    static final String DESC = "description";
    static final String AMOUNT = "amount";

    static {
        sUriMatcher.addURI(AUTHORITY, "financial", FINANCIAL);
        sUriMatcher.addURI(AUTHORITY, "financial/#", FINANCIAL_ID);
    }

    //public FinancialProvider() {
    //}

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Financial";
    static final String FINANCIAL_TABLE_NAME = "transactions";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + FINANCIAL_TABLE_NAME +
                    " (transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " date TEXT NOT NULL, " +
                    " description TEXT NOT NULL, " +
                    " amount TEXT NOT NULL);";

    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " +  FINANCIAL_TABLE_NAME);
            onCreate(db);
        }
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)){
            case FINANCIAL:
                count = db.delete(FINANCIAL_TABLE_NAME, selection, selectionArgs);
                break;

            case FINANCIAL_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete( FINANCIAL_TABLE_NAME, _ID + " = " + id +
                                (!TextUtils.isEmpty(selection) ?
                                        " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case FINANCIAL:
                return "vnd.android.cursor.dir/financial";
            case FINANCIAL_ID:
                return "vnd.android.cursor.item/financial";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(	FINANCIAL_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */

        db = dbHelper.getWritableDatabase();
        return (db == null)? false:true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FINANCIAL_TABLE_NAME);

        switch (sUriMatcher.match(uri)) {
            case FINANCIAL:
                qb.setProjectionMap(FINANCIAL_PROJECTION_MAP);
                break;

            case FINANCIAL_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }

        if (sortOrder == null || sortOrder == ""){
            /**
             * By default sort on student names
             */
            sortOrder = _ID;
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs, null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
            int count = 0;
            switch (sUriMatcher.match(uri)) {
                case FINANCIAL:
                    count = db.update(FINANCIAL_TABLE_NAME, values, selection, selectionArgs);
                    break;

                case FINANCIAL_ID:
                    count = db.update(FINANCIAL_TABLE_NAME, values,
                            _ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ?
                                            " AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
    }
}
