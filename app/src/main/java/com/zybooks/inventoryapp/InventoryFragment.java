/**
 * @Author: Christopher Richards
 * @File: InventoryFragment.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: This is the java class that is the controller for the
 * add_item_fragment.
 */


package com.zybooks.inventoryapp;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InventoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InventoryFragment extends Fragment implements ItemAdapter.RemoveClickListener, ItemAdapter.EditClickListener, ItemAdapter.LowStockNotifier{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private ItemAdapter itemAdapter;
    private InventoryViewModel inventoryView;
    private InventoryDAO inventory;
    private boolean smsEnabled;
    private String smsMobileNumber;
    private ActivityResultLauncher<String> lowStockRequestPermissionLauncher;

    public InventoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InventoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    // Interface implementation for onClick listeners in the ItemAdapter
    @Override
    public void onDeleteItemClick(long id){
        deleteItem(id);
    }
    @Override
    public void onEditItemClick(long id){ editItem(id);}

    // Interface to send SMS message for low inventory alerts
    @Override
    public void notifyLowStock(InventoryDAO.InventoryItem item){
        if(!smsEnabled || smsMobileNumber.isEmpty()){
            return;
        }
        new ViewModelProvider(requireActivity()).get(InventoryViewModel.class).setLowStockItem(item);
        lowStockRequestPermissionLauncher.launch(Manifest.permission.SEND_SMS);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inventory, container, false);

        // Load the saved shared prefs for SMS notification.
        loadSMSPrefs();

        // Get the addButton floating action bar and set it's onclick event.
        FloatingActionButton addButton = view.findViewById(R.id.inventoryAddInventoryButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // The onClick even of the addButton sends the user to the AddItemFragment
                NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                NavController navController = navHostFragment.getNavController();
                NavOptions navOptions = new NavOptions.Builder()
                        .build();
                navController.navigate(R.id.navigation_add_item,null, navOptions);
            }
        });

        // Get reference to Recycler view and set up its layout manager and adapter
        RecyclerView itemContainer = view.findViewById(R.id.itemContainer);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        itemContainer.setLayoutManager(layoutManager);
        itemAdapter = new ItemAdapter(new ArrayList<>(), this, this, this);
        itemContainer.setAdapter(itemAdapter);

        // Create an inventory view for passing back and forth inventory data between fragments
        inventoryView = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);
        inventoryView.getInventoryData().observe(getViewLifecycleOwner(), data ->{
            itemAdapter.updateData(data);
        });

        // Get a initializing the PermissionLauncher for SMS notification. Check for persmission before sending SMS
        lowStockRequestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            // If access is granted, send the low stock SMS
            if(isGranted){
                InventoryDAO.InventoryItem item = inventoryView.getLowStockItem();
                if(item != null){
                    String smsMessage = String.format("Item %s currently has a low inventory of %d items.", item.getmName(), item.getmQuantity());
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(smsMobileNumber, null, smsMessage, null, null);
                }

            }
        });

        // Load the inventory when the inventory fragment is created
        loadInventory();

        return view;
    }

    // Function called by the onDeleteClick listener of the ItemAdapter
    // Deletes the item from the database with the _id specified.
    private void deleteItem(long id) {
        inventory.deleteInventoryItemFromDatabase(id);
        loadInventory();
    }

    // Function called by the onEditClick listener of the ItemAdapter
    // Sets item _id to be updated and navigates to the EditItemFragment
    private void editItem(long id){
        inventoryView.setInventoryIdToEdit(id);
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavOptions navOptions = new NavOptions.Builder()
                .build();
        navController.navigate(R.id.navigation_edit_item,null, navOptions);
    }

    // Loads the inventory to the inventory fragment.
    private void loadInventory(){
        ArrayList<InventoryDAO.InventoryItem> items = inventory.getAllItemsFromDatabase();
        inventoryView.setInventoryData(items);

    }

    // Loads the SMSPrefs for SMS notification.
    public void loadSMSPrefs(){
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("smsPrefs", Context.MODE_PRIVATE);
        smsEnabled = sharedPrefs.getBoolean("smsEnabled", false);
        smsMobileNumber = sharedPrefs.getString("smsNumber", "");


    }
}