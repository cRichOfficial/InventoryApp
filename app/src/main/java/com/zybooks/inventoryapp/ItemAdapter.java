/**
 * @Author: Christopher Richards
 * @File: ItemAdapter.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: Java class that defines the adapter for the
 * RecycleView used in the InventoryFragment.
 */


package com.zybooks.inventoryapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    // Define the listeners and item array list for the Adapter
    private ArrayList<InventoryDAO.InventoryItem> mItems;
    private RemoveClickListener mRemoveListener;
    private EditClickListener mEditListener;
    private LowStockNotifier mLowStockNotifier;


    // Interfaces for the listeners that implemented
    // within the InventoryFragment
    public interface RemoveClickListener{
        void onDeleteItemClick(long id);
    }

    public interface EditClickListener{
        void onEditItemClick(long id);
    }

    public interface LowStockNotifier{
        void notifyLowStock(InventoryDAO.InventoryItem item);
    }

    // Standar class constructor
    public ItemAdapter(ArrayList<InventoryDAO.InventoryItem> items, RemoveClickListener removeClickListener, EditClickListener editClickListener, LowStockNotifier lowStockNotifier){
        this.mItems = items;
        this.mRemoveListener = removeClickListener;
        this.mEditListener = editClickListener;
        this.mLowStockNotifier = lowStockNotifier;
    }

    // Create the view holder for the views that are to be added to the RecycleView.
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
            return new ViewHolder(itemView);
    }

    // Bind data to child views from the items to be viewed.
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        // get the specified item from the item array list
        InventoryDAO.InventoryItem item = mItems.get(position);

        // Add data from the item to the view objects in the recycler
        holder.nameText.setText(item.getmName());
        holder.descriptionText.setText(item.getmDescription());
        holder.quantityText.setText(String.valueOf(item.getmQuantity()));
        holder.removeButton.setTag(item.getmId());
        holder.editButton.setTag(item.getmId());
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRemoveListener != null){
                    // Trigger the onDelete listener for the remove Button
                    mRemoveListener.onDeleteItemClick((long)v.getTag());
                }
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mEditListener != null){
                    // Trigger the onEdit listener for the edit button
                    mEditListener.onEditItemClick((long)v.getTag());
                }
            }
        });

        // Finally, check if the item quantity is below the alert threshold,
        // Trigger the notification interface.
        if(item.getmQuantity() <= item.getmAlertQuantity()){
            mLowStockNotifier.notifyLowStock(item);
        }


    }

    @Override
    public int getItemCount(){
        Log.d("ItemCount", String.valueOf(this.mItems.size()));
        return this.mItems.size();
    }

    // Update the items in the RecyclerView
    public void updateData(ArrayList<InventoryDAO.InventoryItem> items){
        this.mItems.clear();
        this.mItems.addAll(items);
        notifyDataSetChanged();
    }

    // This is the ViewHolder implementation
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageButton removeButton, editButton;
        TextView nameText, descriptionText, quantityText;

        public ViewHolder(View itemView){
            super(itemView);

            // Assign the view objects in the layout to the objects in the ViewHolder
            // THis is accessed by the onBindViewHolder of the Adapter.
            removeButton = itemView.findViewById(R.id.item_layout_remove_button);
            editButton = itemView.findViewById(R.id.item_layout_edit_button);
            nameText = itemView.findViewById(R.id.item_layout_name_text);
            descriptionText = itemView.findViewById(R.id.item_layout_description_text);
            quantityText = itemView.findViewById(R.id.item_layout_quantity_text);


        }
    }
}
