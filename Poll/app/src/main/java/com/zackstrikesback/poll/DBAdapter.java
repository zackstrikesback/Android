package com.zackstrikesback.poll;

import java.util.ArrayList;
import java.util.Date;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter extends SQLiteOpenHelper {
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_QUESTION = "Question";
    public static final String KEY_IMAGE = "Image";
    public static final String KEY_YES = "Yes";
    public static final String KEY_NO = "No";
    private static final String DB_NAME = "Polls.db";
    private static final int DB_VERSION = 1;
    private static DBAdapter myDb = null;
    private SQLiteDatabase db;
    public static final String DB_POLL_TABLE = "Poll";
    private static final String CreatePollTable = "create table "
            + DB_POLL_TABLE
            + "(ID integer primary key autoincrement, Name text, Question text, Image text, Yes int, No int); ";

    private DBAdapter(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static DBAdapter getInstance(Context context) {
        if (myDb == null)
        {
            myDb = new DBAdapter(context);
        }

        return myDb;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreatePollTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DB_POLL_TABLE);
        onCreate(db);
    }

    public boolean addPoll(String name, String question, String image) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_QUESTION, question);
        values.put(KEY_IMAGE, image);
        values.put(KEY_YES, 0);
        values.put(KEY_NO, 0);

        db.insert(DB_POLL_TABLE, null, values);
        return true;
    }

    public DBPoll[] getPolls() {
        ArrayList<DBPoll> polls = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DB_POLL_TABLE, null);
        if (cursor.moveToFirst()) {
            while (true)
            {
                DBPoll poll = new DBPoll();

                poll.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));

                poll.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));

                poll.setQuestion(cursor.getString(cursor.getColumnIndex(KEY_QUESTION)));

                poll.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE)));

                poll.setYes(cursor.getInt(cursor.getColumnIndex(KEY_YES)));

                poll.setNo(cursor.getInt(cursor.getColumnIndex(KEY_NO)));

                polls.add(poll);
                if (!cursor.moveToNext())
                {
                    break;
                }
            }
        }
        cursor.close();
        DBPoll [] dbPoll = polls.toArray(new DBPoll [polls.size()]);
        return dbPoll;
    }

    public boolean updatePoll(Integer id, String name, String question, String image, Integer y, Integer n) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_QUESTION, question);
        values.put(KEY_IMAGE, image);
        values.put(KEY_YES, 0);
        values.put(KEY_NO, 0);

        db.update(DB_POLL_TABLE, values, "ID = ?", new String[]{Integer.toString(id)});
        db.close();
        return true;
    }

    public void votePoll(Integer id, boolean vote) {
        db = this.getWritableDatabase();
        if(vote) {
            db.execSQL("update "+DB_POLL_TABLE+" set "+KEY_YES+" = "+KEY_YES+" + 1 where ID = "+id);
        }
        else {
            db.execSQL("update "+DB_POLL_TABLE+" set "+KEY_NO+" = "+KEY_NO+" + 1 where ID = "+id);
        }
        db.close();
    }

    public void deletePoll(Integer id) {
        db = this.getWritableDatabase();
        db.delete(DB_POLL_TABLE, "ID =" + id, null);
        db.close();
    }

    public void deletePolls() {
        db = this.getWritableDatabase();
        db.delete(DB_POLL_TABLE, null, null);
        db.close();
    }
}