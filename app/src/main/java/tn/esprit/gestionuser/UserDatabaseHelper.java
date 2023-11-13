package tn.esprit.gestionuser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class UserDatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    private Context context;
    private static final String DATABASE_NAME = "bokify.db";
    private static final int DATABASE_VERSION = 1;

    // Table Name
    public static final String TABLE_USERS = "users";
    public static final String TABLE_OFFERS = "offres";

    // Table columns
    public static final String _ID = "_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_EMAIL = "email";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_USER_PHONE = "phone";

    //hotel
    public static final String _ID_OFFER = "_id_hotel";
    public static final String COLUMN_OFFER_NAME = "offername";
    public static final String COLUMN_OFFER_LOCATION = "location";
    public static final String COLUMN_OFFER_DETAILS = "details";
    public static final String COLUMN_OFFER_PRICE = "price";

    // Creating table query
    public int deleteOffer(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_OFFERS, _ID_OFFER + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
    public int updateOffer(long id, String offername, String location, String details,
                                 Float price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_OFFER_NAME, offername);
        values.put(String.valueOf(COLUMN_OFFER_LOCATION), location);
        values.put(String.valueOf(COLUMN_OFFER_DETAILS), details);
        values.put(String.valueOf(COLUMN_OFFER_PRICE), price != null ? price.toString() : null);
        int result = db.update(TABLE_OFFERS, values, _ID_OFFER + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }
    public Cursor getAllOffers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_OFFERS, null);
    }


    public ArrayList<Offer> getAllOffers2() {
        ArrayList<Offer> offers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                _ID_OFFER,
                COLUMN_OFFER_NAME,
                COLUMN_OFFER_LOCATION,
                COLUMN_OFFER_DETAILS,
                COLUMN_OFFER_PRICE
        };

        Cursor cursor = db.query(
                TABLE_OFFERS,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                long offerIdColumnIndex = cursor.getColumnIndex(_ID_OFFER);
                long offerId = offerIdColumnIndex != -1 ? cursor.getInt((int) offerIdColumnIndex) : -1L;

                int nameColumnIndex = cursor.getColumnIndex(COLUMN_OFFER_NAME);
                String name = nameColumnIndex != -1 ? cursor.getString(nameColumnIndex) : "";

                int locationColumnIndex = cursor.getColumnIndex(COLUMN_OFFER_LOCATION);
                String location = locationColumnIndex != -1 ? cursor.getString(locationColumnIndex) : "";

                int detailsColumnIndex = cursor.getColumnIndex(COLUMN_OFFER_DETAILS);
                String details = detailsColumnIndex != -1 ? cursor.getString(detailsColumnIndex) : "";

                int priceColumnIndex = cursor.getColumnIndex(COLUMN_OFFER_PRICE);
                float price = priceColumnIndex != -1 ? cursor.getFloat(priceColumnIndex) : -1;

                Offer offer = new Offer(offerId, name, location, details, price);
                offers.add(offer);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return offers;
    }



    public Cursor getOfferById(long offerId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_OFFERS, null, _ID_OFFER + "=?", new String[]{String.valueOf(offerId)}, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }
    void addOffer(String name, String details, String location, Float price){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_OFFER_NAME, name);
        cv.put(COLUMN_OFFER_DETAILS, details);
        cv.put(COLUMN_OFFER_LOCATION, location);
        cv.put(COLUMN_OFFER_PRICE, price);
        long result = db.insert(TABLE_OFFERS,null, cv);
        if(result == -1){
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
    }

    private static final String CREATE_TABLE_OFFERS = "CREATE TABLE " + TABLE_OFFERS + "(" +
            _ID_OFFER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_OFFER_NAME + " TEXT NOT NULL, " +
            COLUMN_OFFER_DETAILS + " TEXT NOT NULL, " +
            COLUMN_OFFER_LOCATION + " TEXT NOT NULL, " +
            COLUMN_OFFER_PRICE + " REAL NOT NULL);";

    private static final String CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "(" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_NAME + " TEXT NOT NULL, " +
            COLUMN_USER_EMAIL + " TEXT NOT NULL, " +
            COLUMN_USER_PASSWORD + " TEXT NOT NULL, " +
            COLUMN_USER_PHONE + " TEXT NOT NULL);";

    public UserDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Set the context member variable
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_OFFERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OFFERS);
        onCreate(db);
    }




    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS, null);
    }

    public void deleteAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_USERS, null, null);
        if(rowsDeleted > 0) {
            Log.d("DB", "Deleted " + rowsDeleted + " users from the database.");
        } else {
            Log.d("DB", "No users found to delete.");
        }
        db.close();
    }

    public boolean checkUserExists(String username, String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_NAME};
        String selection = COLUMN_USER_NAME + "=?" + " AND " + COLUMN_USER_EMAIL + "=?";
        String[] selectionArgs = {username, email};

        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        return cursorCount > 0;
    }
    public boolean updateUserPassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();


        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(newPassword.getBytes(StandardCharsets.UTF_8));
            String safePasswordHash = Base64.encodeToString(hash, Base64.NO_WRAP);
            contentValues.put(COLUMN_USER_PASSWORD, safePasswordHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }


        int updateStatus = db.update(TABLE_USERS, contentValues, COLUMN_USER_NAME + "=?", new String[] {username});
        db.close();


        return updateStatus > 0;

    }






}
