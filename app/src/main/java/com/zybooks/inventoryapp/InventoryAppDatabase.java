/**
 * @Author: Christopher Richards
 * @File: InventoryAppDatabase.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: This is the java class that provides access to the app database
 * for the DAOs.
 */

package com.zybooks.inventoryapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;

import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

public class InventoryAppDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventoryApp.db";
    private static final int VERSION = 4;
    public InventoryAppDatabase(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }


    // Define user table columns
    private static final class UserTable{
        private static final String TABLE = "users";
        private static final String COL_ID = "_id";
        private static final String USER_ID = "user_id";
        private static final String USER_PASS = "user_pass";
        private static final String USER_FIRST_NAME = "user_first_name";
        private static final String USER_LAST_NAME = "user_last_name";
        private static final String USER_EMAIL = "user_email";
        private static final String SALT = "salt";
    }

    // Define inventory columns
    private static final class InventoryTable{
        private static final String TABLE = "inventory";
        private static final String COL_ID = "_id";
        private static final String INVENTORY_NAME = "inventory_name";
        private static final String INVENTORY_DESCRIPTION = "inventory_description";
        private static final String INVENTORY_QUANTITY = "inventory_quantity";
        private static final String INVENTORY_ALERT_QUANTITY = "inventory_alert_quantity";
    }

    // Create when database is created, create the tables
    @Override
    public void onCreate(SQLiteDatabase db){
        String createUserTableQuery = String.format("create table %s (%s integer primary key autoincrement, %s text, %s text, %s text, %s text, %s text, %s text)",
                UserTable.TABLE,
                UserTable.COL_ID,
                UserTable.USER_ID,
                UserTable.USER_PASS,
                UserTable.SALT,
                UserTable.USER_EMAIL,
                UserTable.USER_FIRST_NAME,
                UserTable.USER_LAST_NAME);

        String  createInventoryTableQuery = String.format("create table %s (%s integer primary key autoincrement, %s text, %s text, %s integer, %s integer)",
                InventoryTable.TABLE,
                InventoryTable.COL_ID,
                InventoryTable.INVENTORY_NAME,
                InventoryTable.INVENTORY_DESCRIPTION,
                InventoryTable.INVENTORY_QUANTITY,
                InventoryTable.INVENTORY_ALERT_QUANTITY);

        db.execSQL(createUserTableQuery);
        db.execSQL(createInventoryTableQuery);
    }

    // If table is upgraded, drop tables and create new ones.
    @Override
    public void onUpgrade(SQLiteDatabase db, int OldVersion, int newVersion){
        db.execSQL(String.format("drop table if exists %s", UserTable.TABLE));
        db.execSQL(String.format("drop table if exists %s", InventoryTable.TABLE));
        onCreate(db);
    }

    // Method for adding a user object to the app database
    public boolean createUser(UserDAO.User user){
        // Get database
        SQLiteDatabase db = getWritableDatabase();
        // Create content values for query
        ContentValues values = new ContentValues();
        // Add user data to the contentValues for the database query.
        values.put(UserTable.USER_ID, user.getmUserId());
        values.put(UserTable.USER_FIRST_NAME, user.getmFirstName());
        values.put(UserTable.USER_LAST_NAME, user.getmLastName());
        values.put(UserTable.USER_EMAIL, user.getmUserEmail());

        // We are going to salt and hash the password, in the even a third
        // party is able to read the database on the device, they will not
        // have the password for the users for the app on the device.
        String salt = getSalt();
        user.setmUserPass(hashPass(user.getmUserPass() + salt));
        values.put(UserTable.USER_PASS, user.getmUserPass());
        values.put(UserTable.SALT, salt);

        // Insert database and return a success (true) or failure (false) outcome
        return db.insert(UserTable.TABLE, null, values) > 0;
    }

    // Method to retrieve a user from the database
    public UserDAO.User getUser(String userId){
        // Get database
        SQLiteDatabase db = getReadableDatabase();

        // Specify columns. Since this is populating the profile,
        // there is no need to retrieve password or salt information.
        String[] columns = {
                UserTable.USER_FIRST_NAME,
                UserTable.USER_LAST_NAME,
                UserTable.USER_EMAIL
        };

        // Get the user from the DB.
        Cursor cursor = db.query(UserTable.TABLE, columns, "user_id = ?",new String[]{userId}, null, null, null);
        if(cursor.moveToFirst()){
            UserDAO.User user = new UserDAO.User();
            user.setmFirstName(cursor.getString(0));
            user.setmLastName(cursor.getString(1));
            user.setmUserEmail(cursor.getString(2));
            return user;
        }
        return null;
    }


    // Method to authenticate a user.
    public boolean authenticateUser(String userId, String userPass){
        // Create query template.
        String query = String.format("select %s from %s where %s = ? and %s = ?", UserTable.USER_ID, UserTable.TABLE, UserTable.USER_ID, UserTable.USER_PASS);

        // Get the user's salt information to generate hash of password
        String userSalt = getUserSalt(userId);
        if(userSalt != null){
            // Get database
            SQLiteDatabase db = getReadableDatabase();

            // Search for user and hashed password. If it returns conditional value depending on records
            // returned. Anything greater than 0 is a success.
            Cursor cursor = db.rawQuery(query, new String[]{userId, hashPass(userPass + userSalt)});
            int cursCount = cursor.getCount();
            cursor.close();
            return cursCount > 0;
        }
        return false;
    }

    // Get the stored salt value for the user.
    private String getUserSalt(String userId){
        SQLiteDatabase db = getWritableDatabase();
        String query = String.format("select %s from %s where %s = ?", UserTable.SALT, UserTable.TABLE, UserTable.USER_ID);
        Cursor cursor = db.rawQuery(query, new String[]{userId});
        String salt;
        if(cursor.moveToFirst()){
            salt = cursor.getString(0);
        }
        else{
            salt = null;
        }
        return salt;
    }

    /* Password hashing function found via https://stackoverflow.com/questions/68701475/android-password-hash */
    private String hashPass(String userPass){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(("SHA-512"));
            md.reset();
            md.update(userPass.getBytes());
            byte[] mdArray = md.digest();
            StringBuilder sb = new StringBuilder(mdArray.length * 2);
            for(byte b : mdArray){
                int v = b & 0xff;
                if(v < 16){
                    sb.append('0');
                }
                sb.append(Integer.toHexString(v));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    // Method to generate salt value.
    private String getSalt(){
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[32];
            sr.nextBytes(salt);
            return Base64.encodeToString(salt, Base64.DEFAULT);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }


    // Method to add inventory items to database.
    public long createInventoryItem(@NonNull InventoryDAO.InventoryItem item){
        // Get DB
        SQLiteDatabase db = getWritableDatabase();

        // Define content values and use them in insert statement.
        ContentValues values = new ContentValues();
        values.put(InventoryTable.INVENTORY_NAME, item.getmName());
        values.put(InventoryTable.INVENTORY_DESCRIPTION, item.getmDescription());
        values.put(InventoryTable.INVENTORY_QUANTITY, item.getmQuantity());
        values.put(InventoryTable.INVENTORY_ALERT_QUANTITY, item.getmAlertQuantity());
        return db.insert(InventoryTable.TABLE, null, values);

    }

    // Method to update an item in the database
    public boolean updateInventoryItem(InventoryDAO.InventoryItem item){
        // Get DB
        SQLiteDatabase db = getWritableDatabase();

        // Create and populate content values and use in update query.
        ContentValues values = new ContentValues();
        values.put(InventoryTable.INVENTORY_NAME, item.getmName());
        values.put(InventoryTable.INVENTORY_DESCRIPTION, item.getmDescription());
        values.put(InventoryTable.INVENTORY_QUANTITY, item.getmQuantity());
        values.put(InventoryTable.INVENTORY_ALERT_QUANTITY, item.getmAlertQuantity());
        return db.update(InventoryTable.TABLE, values, "_id = ?", new String[]{item.getmId() + ""}) > 0;

    }

    // Method to delete inventory item.
    public boolean deleteInventoryItem(long itemId){
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(InventoryTable.TABLE, "_id = ?", new String[]{itemId + ""}) > 1;
    }

    // Method that retrieves all inventory items
    public ArrayList<InventoryDAO.InventoryItem> getInventoryItems(){
        // Get DB
        SQLiteDatabase db = getReadableDatabase();

        // Query the item_inventory table.
        Cursor cursor = db.query(InventoryTable.TABLE, null, null, null, null, null, null);

        // Create ArrayList of InventoryItems and populate with records from cursor.
        ArrayList< InventoryDAO.InventoryItem> items = new ArrayList<>();
        while(cursor.moveToNext()){
            InventoryDAO.InventoryItem item = new InventoryDAO.InventoryItem();
            item.setmId(cursor.getLong(0));
            item.setmName(cursor.getString(1));
            item.setmDescription(cursor.getString(2));
            item.setmQuantity(cursor.getInt(3));
            item.setmAlertQuantity(cursor.getInt(4));
            items.add(item);
        }
        return items;
    }

    // Method to obtain an inventory item by ID
    public InventoryDAO.InventoryItem getInventoryItemById(long id) {
        // Get DB
        SQLiteDatabase db = getReadableDatabase();

        // Get record that matches given id.
        Cursor cursor = db.query(InventoryTable.TABLE,null,"_id = ?", new String[]{String.valueOf(id)}, null, null, null);

        // Get the first occurrence of the record and populate the inventory item and return it.
        if(cursor.moveToFirst()){
            InventoryDAO.InventoryItem item = new InventoryDAO.InventoryItem();
            item.setmId(cursor.getLong(0));
            item.setmName(cursor.getString(1));
            item.setmDescription(cursor.getString(2));
            item.setmQuantity(cursor.getInt(3));
            item.setmAlertQuantity(cursor.getInt(4));
            return item;

        }
        return null;
    }
}
