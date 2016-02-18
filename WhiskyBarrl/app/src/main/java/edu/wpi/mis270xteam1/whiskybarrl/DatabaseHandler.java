package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony J. Ruffa on 2/5/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WhiskyBarrl.db";

    private static final String USER_TABLE_NAME = "Users";
    private static final String USER_COLUMN_ID = "_id";
    private static final String USER_COLUMN_USERNAME = "Username";
    private static final String USER_COLUMN_PASSWORD = "Password";
    private static final String USER_COLUMN_FIRST_NAME = "Firstname";
    private static final String USER_COLUMN_LAST_NAME = "Lastname";
    private static final String USER_COLUMN_EMAIL = "Email";
    private static final String USER_COLUMN_PHONE = "Phone";
    private static final String USER_COLUMN_AGE = "Age";
    private static final String USER_COLUMN_GENDER = "Gender";
    private static final String USER_COLUMN_COUNTRY = "Country";

    private static final String WHISKEY_TABLE_NAME = "Whiskeys";
    private static final String WHISKEY_COLUMN_ID = "_id";
    private static final String WHISKEY_COLUMN_NAME = "Name";
    private static final String WHISKEY_COLUMN_DESCRIPTION = "Description";
    private static final String WHISKEY_COLUMN_RATING = "Rating";
    private static final String WHISKEY_COLUMN_PROOF = "Proof";
    private static final String WHISKEY_COLUMN_AGE = "Age";
    private static final String WHISKEY_COLUMN_LOCATION = "Location";

    private Context context;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
                USER_COLUMN_USERNAME + " TEXT UNIQUE, " +
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
     * Retrieve a user from the database using the username.
     *
     * @param username the username of the user to retrieve
     * @return the user, or null if one does not exist
     */
    public User getUser(String username) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME
                + " WHERE " + USER_COLUMN_USERNAME + " = \"" + username + "\"" , null);

        if (!c.moveToFirst()) {
            return null;
        }

        User user = new User();
        user.setId(c.getInt(c.getColumnIndex(USER_COLUMN_ID)));
        user.setUsername(username);
        user.setPassword(c.getString(c.getColumnIndex(USER_COLUMN_PASSWORD)));
        user.setFirstName(c.getString(c.getColumnIndex(USER_COLUMN_FIRST_NAME)));
        user.setLastName(c.getString(c.getColumnIndex(USER_COLUMN_LAST_NAME)));
        user.setEmail(c.getString(c.getColumnIndex(USER_COLUMN_EMAIL)));
        user.setPhoneNumber(c.getString(c.getColumnIndex(USER_COLUMN_PHONE)));
        user.setAge(c.getInt(c.getColumnIndex(USER_COLUMN_AGE)));
        user.setGender(c.getString(c.getColumnIndex(USER_COLUMN_GENDER)));
        user.setCountry(c.getString(c.getColumnIndex(USER_COLUMN_COUNTRY)));

        c.close();

        return user;
    }

    /**
     * Retrieve a whiskey from the database using its ID.
     *
     * @param id the ID of the whiskey to retrieve
     * @return the whiskey, or null if one does not exist
     */
    public Whiskey getWhiskey(int id) {
        SQLiteDatabase db = getReadableDatabase();


        Cursor c = db.rawQuery("SELECT * FROM " + WHISKEY_TABLE_NAME
                + " WHERE " + WHISKEY_COLUMN_ID + " = \"" + id + "\"", null);

        if (!c.moveToFirst()) {
            return null;
        }

        Whiskey whiskey = new Whiskey();
        whiskey.setId(id);
        whiskey.setName(c.getString(c.getColumnIndex(WHISKEY_COLUMN_NAME)));
        whiskey.setDescription(c.getString(c.getColumnIndex(WHISKEY_COLUMN_DESCRIPTION)));
        whiskey.setRating(c.getString(c.getColumnIndex(WHISKEY_COLUMN_RATING)));
        whiskey.setProofLevel(c.getInt(c.getColumnIndex(WHISKEY_COLUMN_PROOF)));
        whiskey.setLocation(c.getString(c.getColumnIndex(WHISKEY_COLUMN_LOCATION)));
        whiskey.setAge(c.getInt(c.getColumnIndex(WHISKEY_COLUMN_AGE)));

        c.close();

        return whiskey;
    }

    /**
     * Get all the whiskeys from the database.
     *
     * @return an array adapter with the whiskeys retrieved
     */
    public List<Whiskey> getAllWhiskeys() {
        SQLiteDatabase db = getWritableDatabase();
        List<Whiskey> results = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + WHISKEY_TABLE_NAME + ";", null);

        if (c.moveToFirst()) {
            do {
                Whiskey whiskey = new Whiskey();
                whiskey.setId(c.getInt(c.getColumnIndex(WHISKEY_COLUMN_ID)));
                whiskey.setName(c.getString(c.getColumnIndex(WHISKEY_COLUMN_NAME)));
                whiskey.setDescription(c.getString(c.getColumnIndex(WHISKEY_COLUMN_DESCRIPTION)));
                whiskey.setRating(c.getString(c.getColumnIndex(WHISKEY_COLUMN_RATING)));
                whiskey.setProofLevel(c.getInt(c.getColumnIndex(WHISKEY_COLUMN_PROOF)));
                whiskey.setLocation(c.getString(c.getColumnIndex(WHISKEY_COLUMN_LOCATION)));
                whiskey.setAge(c.getInt(c.getColumnIndex(WHISKEY_COLUMN_AGE)));
                results.add(whiskey);
            } while (c.moveToNext());
        }
        c.close();

        return results;
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
