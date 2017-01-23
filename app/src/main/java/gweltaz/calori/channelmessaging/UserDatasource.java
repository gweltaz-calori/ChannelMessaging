package gweltaz.calori.channelmessaging;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;
import java.util.UUID;

/**
 * Created by calorig on 23/01/2017.
 */
public class UserDatasource
{
    private SQLiteDatabase database;
    private FriendsDB dbHelper;
    private String[] allColumns = { FriendsDB.USER_ID,FriendsDB.USERNAME,
            FriendsDB.USER_URL };
    public UserDatasource(Context context) {
        dbHelper = new FriendsDB(context);
    }
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }
    public Friends createFriend(String url,int id,String username) {
        ContentValues values = new ContentValues();
        values.put(FriendsDB.USER_ID, id);
        values.put(FriendsDB.USER_URL, url);
        values.put(FriendsDB.USER_URL, username);
        database.insert(FriendsDB.FRIENDS_TABLE_NAME, null,
                values);
        Cursor cursor = database.query(FriendsDB.FRIENDS_TABLE_NAME,
                allColumns, FriendsDB.USER_ID + " = \"" + id+"\"", null,
                null, null, null);
        cursor.moveToFirst();
        Friends friend = cursorToFriend(cursor);
        cursor.close();
        return friend;
    }
    public List<Friends> all()
    {

    }
    private Friends cursorToFriend(Cursor cursor) {
        Friends friend = new Friends(cursor.getString(2),cursor.getInt(0),cursor.getString(1));
        return friend;
    }
}