package gweltaz.calori.channelmessaging.dbhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.util.UUID;

/**
 * Created by calorig on 23/01/2017.
 */
public class FriendsDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "MyDB.db";
    public static final String FRIENDS_TABLE_NAME = "Friends";
    public static final String USER_ID = "id";
    public static final String USERNAME = "username";
    public static final String USER_URL = "imageUrl";
    private static final String FRIENDS_TABLE_CREATE = "CREATE TABLE " + FRIENDS_TABLE_NAME + " (" + USER_ID + " INTEGER, " +
            USERNAME + " TEXT, " + USER_URL + " TEXT);";
    public FriendsDB(Context context) {
        super(context, Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FRIENDS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FRIENDS_TABLE_NAME);
        onCreate(db);
    }

}
