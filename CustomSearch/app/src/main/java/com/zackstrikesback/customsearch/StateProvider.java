package com.zackstrikesback.customsearch;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class StateProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    public static final String AUTHORITY = "com.zackstrikesback.customsearch.StateProvider";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/states" );

    StateDB mStateDB = null;

    private static final int SUGGESTIONS_STATE = 1;
    private static final int SEARCH_STATE = 2;
    private static final int GET_STATE = 3;

    UriMatcher mUriMatcher = buildUriMatcher();

    private UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, SearchManager.SUGGEST_URI_PATH_QUERY,SUGGESTIONS_STATE);

        uriMatcher.addURI(AUTHORITY, "states", SEARCH_STATE);

        uriMatcher.addURI(AUTHORITY, "states/#", GET_STATE);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        mStateDB = new StateDB(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor c = null;
        switch(mUriMatcher.match(uri)){
            case SUGGESTIONS_STATE :
                c = mStateDB.getStates(selectionArgs);
                break;
            case SEARCH_STATE :
                c = mStateDB.getStates(selectionArgs);
                break;
            case GET_STATE :
                String id = uri.getLastPathSegment();
                c = mStateDB.getState(id);
        }

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }
}
