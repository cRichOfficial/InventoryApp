/**
 * @Author: Christopher Richards
 * @File: InventoryDOA.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: This class defines the data access object for the inventory table.
 */


package com.zybooks.inventoryapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Array;
import java.util.ArrayList;

public class InventoryDAO {

    private InventoryAppDatabase mDb;

    public InventoryDAO(){}
    public InventoryDAO(Context context)
    {
        mDb = new InventoryAppDatabase(context);

    }

    public long saveInventoryToDatabase(InventoryItem item){
        return mDb.createInventoryItem(item);

    }

    public boolean updateInventoryItemToDatabase(InventoryItem item){
        return mDb.updateInventoryItem(item);
    }

    public boolean deleteInventoryItemFromDatabase(long itemId){
        return mDb.deleteInventoryItem(itemId);
    }

    public ArrayList<InventoryItem> getAllItemsFromDatabase(){
        return mDb.getInventoryItems();
    }
    public InventoryItem getItemById(long id){return mDb.getInventoryItemById(id);}

    public InventoryAppDatabase getmDb() {
        return mDb;
    }

    public void setmDb(InventoryAppDatabase mDb) {
        mDb = mDb;
    }


    // Subclass that defines an Inventory Item
    public static class InventoryItem{

        // class fields
        private String mName, mDescription;
        private int mQuantity, mAlertQuantity;
        private long mId;
        public InventoryItem(){}

        // Standard class constructor
        public InventoryItem(long id, String itemName, String itemDescription, int itemQuantity, int itemAlertQuantity){
            mId = id;
            mName = itemName;
            mDescription = itemDescription;
            mQuantity = itemQuantity;
            mAlertQuantity = itemAlertQuantity;
        }

        // Getter and Setter functions
        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }

        public String getmDescription() {
            return mDescription;
        }

        public void setmDescription(String mDescription) {
            this.mDescription = mDescription;
        }

        public int getmQuantity() {
            return mQuantity;
        }

        public void setmQuantity(int mQuantity) {
            this.mQuantity = mQuantity;
        }
        public int getmAlertQuantity(){return mAlertQuantity;}
        public void setmAlertQuantity(int itemAlertQuantity){this.mAlertQuantity = itemAlertQuantity;}

        public long getmId() {
            return mId;
        }

        public void setmId(long mId) {
            this.mId = mId;
        }
    }
}
