package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Anthony J. Ruffa on 2/5/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WhiskyBarrl.db";

    private static final String USER_TABLE_NAME = "Users";
    private static final String USER_COLUMN_ID = "_id";
    private static final String USER_COLUMN_USERNAME = "username";
    private static final String USER_COLUMN_PASSWORD = "password";
    private static final String USER_COLUMN_FIRST_NAME = "firstname";
    private static final String USER_COLUMN_LAST_NAME = "lastname";
    private static final String USER_COLUMN_EMAIL = "email";
    private static final String USER_COLUMN_PHONE = "phone";
    private static final String USER_COLUMN_AGE = "age";
    private static final String USER_COLUMN_GENDER = "gender";
    private static final String USER_COLUMN_COUNTRY = "country";

    private static final String WHISKEY_TABLE_NAME = "Whiskeys";
    private static final String WHISKEY_COLUMN_ID = "_id";
    private static final String WHISKEY_COLUMN_NAME = "whiskey";
    private static final String WHISKEY_COLUMN_DESCRIPTION = "description";
    private static final String WHISKEY_COLUMN_RATING = "rating";
    private static final String WHISKEY_COLUMN_PROOF = "proof";
    private static final String WHISKEY_COLUMN_AGE = "age";
    private static final String WHISKEY_COLUMN_LOCATION = "location";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the database tables.
        db.execSQL("CREATE TABLE " + WHISKEY_TABLE_NAME + "(" +
                WHISKEY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WHISKEY_COLUMN_NAME + " TEXT, " +
                WHISKEY_COLUMN_DESCRIPTION + " TEXT, " +
                WHISKEY_COLUMN_RATING + " TEXT, " +
                WHISKEY_COLUMN_PROOF + " INTEGER, " +
                WHISKEY_COLUMN_AGE + " INTEGER, " +
                WHISKEY_COLUMN_LOCATION + " TEXT " +
                ");");

        db.execSQL("CREATE TABLE " + USER_TABLE_NAME + "(" +
                USER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_COLUMN_USERNAME + " TEXT, " +
                USER_COLUMN_PASSWORD + " TEXT, " +
                USER_COLUMN_FIRST_NAME + " TEXT, " +
                USER_COLUMN_LAST_NAME + " TEXT, " +
                USER_COLUMN_EMAIL + " TEXT, " +
                USER_COLUMN_PHONE + " TEXT, " +
                USER_COLUMN_AGE + " INTEGER, " +
                USER_COLUMN_GENDER + " TEXT, " +
                USER_COLUMN_COUNTRY + " TEXT " +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate them.
        db.execSQL("DROP TABLE IF EXISTS " + WHISKEY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    /**
     * Add a user into the user database table.
     *
     * @param user the user to add
     * @return true if the entry was added in successfully, false otherwise
     */
    public boolean addUser(User user) {
        if (user.getAge() >= 21) {
            // Add the entry only if the user is at least 21.
            ContentValues values = new ContentValues();
            values.put(USER_COLUMN_USERNAME, user.getUsername());
            values.put(USER_COLUMN_PASSWORD, user.getPassword());
            values.put(USER_COLUMN_FIRST_NAME, user.getFirstName());
            values.put(USER_COLUMN_LAST_NAME, user.getLastName());
            values.put(USER_COLUMN_EMAIL, user.getEmail());
            values.put(USER_COLUMN_PHONE, user.getPhoneNumber());
            values.put(USER_COLUMN_AGE, user.getAge());
            values.put(USER_COLUMN_GENDER, user.getGender());
            values.put(USER_COLUMN_COUNTRY, user.getCountry());

            SQLiteDatabase db = getWritableDatabase();
            db.insert(USER_TABLE_NAME, null, values);
            db.close();
            return true;
        }
        return false;
    }

    /**
     * Add a whiskey into the whiskey database table.
     *
     * @param whiskey the whiskey to add
     * @return true if the entry was added in successfully, false otherwise
     */
    public boolean addWhiskey(Whiskey whiskey) {
        ContentValues values = new ContentValues();
        values.put(WHISKEY_COLUMN_NAME, whiskey.getName());
        values.put(WHISKEY_COLUMN_DESCRIPTION, whiskey.getDescription());
        values.put(WHISKEY_COLUMN_RATING, whiskey.getRating());
        values.put(WHISKEY_COLUMN_PROOF, whiskey.getProofLevel());
        values.put(WHISKEY_COLUMN_AGE, whiskey.getAge());
        values.put(WHISKEY_COLUMN_LOCATION, whiskey.getLocation());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(WHISKEY_TABLE_NAME, null, values);
        db.close();
        return true;
    }

    /**
     * Delete a user from the table given its ID in the database table.
     *
     * @param userId the ID of the user to delete
     */
    public void deleteUser(String userId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + USER_TABLE_NAME + " WHERE " +
                USER_COLUMN_ID + "= \"" + userId + "\";");
    }

    /**
     * Delete a whiskey from the table given its ID in the database table.
     *
     * @param whiskeyId the ID of the whiskey to delete
     */
    public void deleteWhiskey(String whiskeyId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + WHISKEY_TABLE_NAME + " WHERE " +
                WHISKEY_COLUMN_ID + "= \"" + whiskeyId + "\";");
    }
}
