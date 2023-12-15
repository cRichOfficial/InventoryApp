/**
 * @Author: Christopher Richards
 * @File: InventoryViewModel.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: Defines Inventory view model for passing inventory data
 * between fragments.
 */

package com.zybooks.inventoryapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class InventoryViewModel extends ViewModel {
    private MutableLiveData<ArrayList<InventoryDAO.InventoryItem>> mInventoryData = new MutableLiveData<>();
    private long meditId = -1;
    private InventoryDAO.InventoryItem mLowStockItem;

    public LiveData<ArrayList<InventoryDAO.InventoryItem>> getInventoryData() {
        return mInventoryData;
    }

    public void setLowStockItem(InventoryDAO.InventoryItem item){mLowStockItem = item;}
    public InventoryDAO.InventoryItem getLowStockItem(){return mLowStockItem;}

    public void setInventoryData(ArrayList<InventoryDAO.InventoryItem> items) {
        mInventoryData.setValue(items);
    }
    public void setInventoryIdToEdit(long id){
        meditId = id;
    }

    public long getInventoryIdToEdit(){return meditId;}
}