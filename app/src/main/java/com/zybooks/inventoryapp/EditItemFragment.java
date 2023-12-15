/**
 * @Author: Christopher Richards
 * @File: EditItemFragment.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: This is the java class that is the controller for the
 * edit_item_fragment.
 */


package com.zybooks.inventoryapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;

    public EditItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditItemFragment newInstance(String param1, String param2) {
        EditItemFragment fragment = new EditItemFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_item, container, false);

        // Get Inventory view model so we can retrieve the item to be edited.
        InventoryViewModel inventoryView = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);

        // Get the saveButton and disable it if no item to edit.
        Button saveButton = view.findViewById(R.id.editItemSaveButton);
        saveButton.setEnabled(inventoryView.getInventoryIdToEdit() > -1);

        // If there is an item to edit (> -1) enable save button and create DAO.
        if(inventoryView.getInventoryIdToEdit() != -1){

            // Create DAO
            InventoryDAO inventory = new InventoryDAO(getContext());

            // Create Inventory Item to be edited from database.
            InventoryDAO.InventoryItem item = inventory.getItemById(inventoryView.getInventoryIdToEdit());

            // Get EditTexts and set text to properties from the item to edit.
            EditText nameText = view.findViewById(R.id.editNameEditText);
            EditText descriptionText = view.findViewById(R.id.editDescriptionEditText);
            EditText quantityText = view.findViewById(R.id.editQuantityEditText);
            EditText alertText = view.findViewById(R.id.editAlertQuantityEditText);
            nameText.setText(item.getmName());
            descriptionText.setText(item.getmDescription());
            quantityText.setText(String.valueOf(item.getmQuantity()));
            alertText.setText(String.valueOf(item.getmAlertQuantity()));

        }
        // Call saveItem() on save button click.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });
        return view;
    }

    // Method to save changes to database
    private void saveItem(){
        // Get the EditTexts
        EditText nameText = view.findViewById(R.id.editNameEditText);
        EditText descriptionText = view.findViewById(R.id.editDescriptionEditText);
        EditText quantityText = view.findViewById(R.id.editQuantityEditText);
        EditText alertText = view.findViewById(R.id.editAlertQuantityEditText);

        // Create InventoryItem and get _id of item to be edited from InventoryViewModel
        InventoryDAO.InventoryItem item = new InventoryDAO.InventoryItem();
        InventoryViewModel inventoryViewModel = new ViewModelProvider(requireActivity()).get(InventoryViewModel.class);

        // Set item properties to values of EditTexts
        item.setmId(inventoryViewModel.getInventoryIdToEdit());;
        item.setmName(nameText.getText().toString());
        item.setmDescription(descriptionText.getText().toString());
        item.setmQuantity(Integer.parseInt(quantityText.getText().toString()));
        item.setmAlertQuantity(Integer.parseInt(alertText.getText().toString()));

        // Create DAO and update item in database.
        InventoryDAO inventory = new InventoryDAO(getContext());
        inventory.updateInventoryItemToDatabase(item);

        // Clear _id of item to be edited.
        inventoryViewModel.setInventoryIdToEdit(-1);

        // Navigate back to inventory fragment.
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavOptions navOptions = new NavOptions.Builder()
                .build();
        navController.navigate(R.id.navigation_inventory,null, navOptions);
    }
}