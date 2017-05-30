package com.zackstrikesback.customsearch;

import java.util.HashMap;
import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class StateDB {
    private static final String DBNAME = "state";
    private static final int VERSION = 1;
    private StateDBOpenHelper mStateDBOpenHelper;
    private static final String FIELD_ID = "_id";
    private static final String FIELD_NAME = "name";
    private static final String TABLE_NAME = "states";
    private HashMap<String, String> mAliasMap;

    public StateDB(Context context){
        mStateDBOpenHelper = new StateDBOpenHelper(context, DBNAME, null, VERSION);

        // This HashMap is used to map table fields to Custom Suggestion fields
        mAliasMap = new HashMap<>();

        // Unique id for the each Suggestions ( Mandatory )
        mAliasMap.put("_ID", FIELD_ID + " as " + "_id" );

        // Text for Suggestions ( Mandatory )
        mAliasMap.put(SearchManager.SUGGEST_COLUMN_TEXT_1, FIELD_NAME + " as " + SearchManager.SUGGEST_COLUMN_TEXT_1);

        // This value will be appended to the Intent data on selecting an item from Search result or Suggestions ( Optional )
        mAliasMap.put( SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, FIELD_ID + " as " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID );
    }

    /** Returns States */
    public Cursor getStates(String[] selectionArgs){

        String selection = FIELD_NAME + " like ? ";

        if(selectionArgs!=null){
            selectionArgs[0] = "%"+selectionArgs[0] + "%";
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setProjectionMap(mAliasMap);

        queryBuilder.setTables(TABLE_NAME);

        Cursor c = queryBuilder.query(mStateDBOpenHelper.getReadableDatabase(),
                new String[] { "_ID",
                        SearchManager.SUGGEST_COLUMN_TEXT_1 ,
                        SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID } ,
                selection,
                selectionArgs,
                null,
                null,
                FIELD_NAME + " asc ","10"
        );
        return c;
    }

    /** Return State corresponding to the id */
    public Cursor getState(String id){

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        queryBuilder.setTables(TABLE_NAME);

        Cursor c = queryBuilder.query(mStateDBOpenHelper.getReadableDatabase(),
                new String[] { "_id", "name" } ,
                "_id = ?", new String[] { id } , null, null, null ,"1"
        );

        return c;
    }
    class StateDBOpenHelper extends SQLiteOpenHelper{

        public StateDBOpenHelper( Context context,
                                    String name,
                                    CursorFactory factory,
                                    int version ) {
            super(context, DBNAME, factory, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql = "";

            // Defining table structure
            sql =   " create table " + TABLE_NAME + "" +
                    " ( " +
                    FIELD_ID + " integer primary key autoincrement, " +
                    FIELD_NAME + " varchar(100) " +
                    " ) " ;

            // Creating table
            db.execSQL(sql);

            for(int i=0;i<State.states.length;i++){

                // Defining insert statement
                sql = "insert into " + TABLE_NAME + " ( " +
                        FIELD_NAME +  " ) " +
                        " values ( " +
                        " '" + State.states[i] + "' ) ";

                // Inserting values into table
                db.execSQL(sql);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
        }
    }
}
