package edu.wpi.mis270xteam1.whiskybarrl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anthony J. Ruffa on 2/5/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WhiskyBarrl.db";

    // Table name and columns for users table
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

    // Table name and columns for whiskeys table
    private static final String WHISKEY_TABLE_NAME = "Whiskeys";
    private static final String WHISKEY_COLUMN_ID = "_id";
    private static final String WHISKEY_COLUMN_NAME = "Name";
    private static final String WHISKEY_COLUMN_DESCRIPTION = "Description";
    private static final String WHISKEY_COLUMN_RATING = "Rating";
    private static final String WHISKEY_COLUMN_PROOF = "Proof";
    private static final String WHISKEY_COLUMN_AGE = "Age";
    private static final String WHISKEY_COLUMN_LOCATION = "Location";

    // Table name and columns for whiskey comments table
    private static final String WHISKEY_COMMENT_TABLE_NAME = "WhiskeyComments";
    private static final String WHISKEY_COMMENT_COLUMN_ID = "_id";
    private static final String WHISKEY_COMMENT_COLUMN_COMMENT_TEXT = "CommentText";
    private static final String WHISKEY_COMMENT_COLUMN_COMMENT_USER_ID = "UserID";
    private static final String WHISKEY_COMMENT_COLUMN_COMMENT_WHISKEY_ID = "WhiskeyID";

    // Table name and columns for favorites table
    private static final String FAVORITES_TABLE_NAME = "UserWhiskeys";
    private static final String FAVORITES_COLUMN_USER_ID = "UserID";
    private static final String FAVORITES_COLUMN_WHISKEY_ID = "WhiskeyID";

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
                WHISKEY_COLUMN_RATING + " REAL, " +
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

        db.execSQL("CREATE TABLE " + WHISKEY_COMMENT_TABLE_NAME + "(" +
                WHISKEY_COMMENT_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WHISKEY_COMMENT_COLUMN_COMMENT_TEXT + " TEXT, " +
                WHISKEY_COMMENT_COLUMN_COMMENT_USER_ID + " INTEGER, " +
                WHISKEY_COMMENT_COLUMN_COMMENT_WHISKEY_ID + " INTEGER, " +
                "FOREIGN KEY(" +
                WHISKEY_COMMENT_COLUMN_COMMENT_USER_ID +
                ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + "), " +
                "FOREIGN KEY(" +
                WHISKEY_COMMENT_COLUMN_COMMENT_WHISKEY_ID +
                ") REFERENCES " + WHISKEY_TABLE_NAME + "(" + WHISKEY_COLUMN_ID + ")" +
                ");");

        db.execSQL("CREATE TABLE " + FAVORITES_TABLE_NAME + "(" +
                FAVORITES_COLUMN_USER_ID + " INTEGER NOT NULL, " +
                FAVORITES_COLUMN_WHISKEY_ID + " INTEGER NOT NULL, " +
                "PRIMARY KEY ( "
                + FAVORITES_COLUMN_USER_ID + ", "
                + FAVORITES_COLUMN_WHISKEY_ID + ")," +
                " FOREIGN KEY (" + FAVORITES_COLUMN_USER_ID +
                ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COLUMN_ID + "), " +
                " FOREIGN KEY (" + FAVORITES_COLUMN_WHISKEY_ID +
                ") REFERENCES " + WHISKEY_TABLE_NAME + "(" + WHISKEY_COLUMN_ID + ")"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables and recreate them.
        db.execSQL("DROP TABLE IF EXISTS " + WHISKEY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + WHISKEY_COMMENT_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITES_TABLE_NAME);
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
     * Add a comment for a whiskey into the whiskey comments database table.
     *
     * @param whiskeyComment the whiskey comment to store
     * @return true if the entry was added in successfully, false otherwise
     */
    public boolean addWhiskeyComment(WhiskeyComment whiskeyComment) {
        ContentValues values = new ContentValues();
        values.put(WHISKEY_COMMENT_COLUMN_COMMENT_TEXT, whiskeyComment.getCommentText());
        values.put(WHISKEY_COMMENT_COLUMN_COMMENT_USER_ID, whiskeyComment.getUserId());
        values.put(WHISKEY_COMMENT_COLUMN_COMMENT_WHISKEY_ID, whiskeyComment.getWhiskeyId());

        SQLiteDatabase db = getWritableDatabase();
        db.insert(WHISKEY_COMMENT_TABLE_NAME, null, values);
        db.close();
        return true;
    }

    /**
     * Add a favorite whiskey for a user into the database.
     *
     * @param user the user who favorited the whiskey
     * @param whiskey the whiskey that was favorited
     * @return true if the entry was added in successfully, false otherwise
     */
    public boolean addFavorite(User user, Whiskey whiskey) {
        int userId = user.getId();
        int whiskeyId = whiskey.getId();

        ContentValues values = new ContentValues();
        values.put(FAVORITES_COLUMN_USER_ID, userId);
        values.put(FAVORITES_COLUMN_WHISKEY_ID, whiskeyId);

        SQLiteDatabase db = getWritableDatabase();
        db.insert(FAVORITES_TABLE_NAME, null, values);
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
                + " WHERE " + USER_COLUMN_USERNAME + " = \"" + username + "\";", null);

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
     * Retrieve a user from the database using the ID.
     *
     * @param userId the ID of the user to retrieve
     * @return the user, or null if one does not exist
     */
    public User getUserById(int userId) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME
                + " WHERE " + USER_COLUMN_ID + " = \"" + userId + "\";", null);

        if (!c.moveToFirst()) {
            return null;
        }

        User user = new User();
        user.setId(c.getInt(userId));
        user.setUsername(c.getString(c.getColumnIndex(USER_COLUMN_USERNAME)));
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
                + " WHERE " + WHISKEY_COLUMN_ID + " = \"" + id + "\";", null);

        if (!c.moveToFirst()) {
            return null;
        }

        Whiskey whiskey = new Whiskey();
        whiskey.setId(id);
        whiskey.setName(c.getString(c.getColumnIndex(WHISKEY_COLUMN_NAME)));
        whiskey.setDescription(c.getString(c.getColumnIndex(WHISKEY_COLUMN_DESCRIPTION)));
        whiskey.setRating(c.getFloat(c.getColumnIndex(WHISKEY_COLUMN_RATING)));
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
                whiskey.setRating(c.getFloat(c.getColumnIndex(WHISKEY_COLUMN_RATING)));
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
     * Get all the whiskeys from the database.
     *
     * @return an array adapter with the whiskeys retrieved
     */
    public List<WhiskeyComment> getWhiskeyComments(int whiskeyId) {
        SQLiteDatabase db = getWritableDatabase();
        List<WhiskeyComment> results = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM " + WHISKEY_COMMENT_TABLE_NAME
                + " WHERE " + WHISKEY_COMMENT_COLUMN_COMMENT_WHISKEY_ID + " = \"" + whiskeyId + "\";", null);

        if (c.moveToFirst()) {
            do {
                WhiskeyComment whiskeyComment = new WhiskeyComment();
                whiskeyComment.setId(c.getInt(c.getColumnIndex(WHISKEY_COMMENT_COLUMN_ID)));
                whiskeyComment.setCommentText(c.getString(c.getColumnIndex(WHISKEY_COMMENT_COLUMN_COMMENT_TEXT)));
                whiskeyComment.setUserId(c.getInt(c.getColumnIndex(WHISKEY_COMMENT_COLUMN_COMMENT_USER_ID)));
                whiskeyComment.setWhiskeyId(c.getInt(c.getColumnIndex(WHISKEY_COMMENT_COLUMN_COMMENT_WHISKEY_ID)));
                results.add(whiskeyComment);
            } while (c.moveToNext());
        }
        c.close();

        return results;
    }

    /**
     * Get all whiskey favorites for a given user.
     *
     * @param user the user to get the favorites for
     * @return the list of whiskeys the given user has favorited
     */
    public List<Whiskey> getUserFavorites(User user) {
        SQLiteDatabase db = getWritableDatabase();
        List<Whiskey> results = new ArrayList<>();

        /* SQL query taken from here:
         * http://stackoverflow.com/questions/7805039/multiple-to-multiple-junction-tables-newbie-on-database-structure
         */
        String query = "SELECT " + WHISKEY_TABLE_NAME + ".*, " +
                USER_TABLE_NAME + "." + USER_COLUMN_USERNAME +
                " FROM " + USER_TABLE_NAME +
                " INNER JOIN (" +
                WHISKEY_TABLE_NAME + " INNER JOIN " + FAVORITES_TABLE_NAME +
                " ON " + WHISKEY_TABLE_NAME + "." + WHISKEY_COLUMN_ID + " = " +
                FAVORITES_TABLE_NAME + "." + FAVORITES_COLUMN_WHISKEY_ID + ")" +
                " ON " + USER_TABLE_NAME + "." + USER_COLUMN_ID + " = " +
                FAVORITES_TABLE_NAME + "." + FAVORITES_COLUMN_USER_ID +
                " WHERE " + USER_TABLE_NAME + "." + USER_COLUMN_ID +
                " = \"" + user.getId() + "\"" +
                ";";

        Cursor c = db.rawQuery(query, null);

        if (c.moveToFirst()) {
            do {
                Whiskey whiskey = new Whiskey();
                whiskey.setId(c.getInt(c.getColumnIndex(WHISKEY_TABLE_NAME + "." + WHISKEY_COLUMN_ID)));
                whiskey.setName(c.getString(c.getColumnIndex(WHISKEY_COLUMN_NAME)));
                whiskey.setDescription(c.getString(c.getColumnIndex(WHISKEY_COLUMN_DESCRIPTION)));
                whiskey.setRating(c.getFloat(c.getColumnIndex(WHISKEY_COLUMN_RATING)));
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
     * Update the information of a user.
     *
     * @param user the new user to update it to
     */
    public void updateUser(User user) {
        SQLiteDatabase db = getWritableDatabase();
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

        db.update(USER_TABLE_NAME, values, USER_COLUMN_ID + "=" + user.getId(), null);
        db.close();
    }

    /**
     * Delete a favorite entry from the table given the user and whiskey
     *
     * @param user the user that created the favorite entry
     * @param whiskey the whiskey that was favorited
     */
    public void deleteFavorite(User user, Whiskey whiskey) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + FAVORITES_TABLE_NAME + " WHERE " +
                FAVORITES_COLUMN_USER_ID + "= \"" + user.getId() + "\" AND " +
                FAVORITES_COLUMN_WHISKEY_ID + "= \"" + whiskey.getId() + "\";");
        db.close();
    }
}
