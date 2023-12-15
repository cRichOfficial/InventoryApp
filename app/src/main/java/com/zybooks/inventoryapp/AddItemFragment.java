/**
 * @Author: Christopher Richards
 * @File: AddItemFragment.java
 * @Date: 2023-12-09
 * @Email: christopher.richards4@snhu.edu
 * @Description: This is the java class that is the controller for the
 * add_item_fragment.
 */

package com.zybooks.inventoryapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private boolean itemNameEmpty, itemDescriptionEmpty, itemQUantityEmpty;

    public AddItemFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemFragment newInstance(String param1, String param2) {
        AddItemFragment fragment = new AddItemFragment();
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
        view = inflater.inflate(R.layout.fragment_add_item, container, false);

        // Get references to the Views in the fragment.
        Button buttonAdd = view.findViewById(R.id.addItemButton);
        EditText nameEditText = view.findViewById(R.id.addNameEditText);
        EditText descriptionEditText = view.findViewById(R.id.addDescriptionEditText);
        EditText quantityEditText = view.findViewById(R.id.addQuantityEditText);

        // Add TextChanged listeners to the EditTexts to determine if
        // they are empty, and if so, enable and disable buttons as necesaary
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemNameEmpty = !(s.length() > 0);
                buttonAdd.setEnabled((!itemNameEmpty && !itemDescriptionEmpty && !itemQUantityEmpty));
            }
        });

        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemDescriptionEmpty = !(s.length() > 0);
                buttonAdd.setEnabled((!itemNameEmpty && !itemDescriptionEmpty && !itemQUantityEmpty));
            }
        });

        quantityEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                itemQUantityEmpty = !(s.length() > 0);
                buttonAdd.setEnabled((!itemNameEmpty && !itemDescriptionEmpty && !itemQUantityEmpty));
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        // Disable the "up" button in the action bar.
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        return view;
    }

    // Method to save the edited item. This creates an InventoryDAO and gets data from
    // the EditTexts and saves to database.
    public void saveItem(){
        // Create Inventory Item
        InventoryDAO.InventoryItem item = new InventoryDAO.InventoryItem();

        // Get EditTexts
        EditText nameEditText = view.findViewById(R.id.addNameEditText);
        EditText descriptionEditText = view.findViewById(R.id.addDescriptionEditText);
        EditText quantityEditText = view.findViewById(R.id.addQuantityEditText);
        EditText alertEditText = view.findViewById(R.id.addAlertEditText);

        // Set Inventory item properties to values in the EditTexts
        item.setmName(nameEditText.getText().toString());
        item.setmDescription(descriptionEditText.getText().toString());
        item.setmQuantity(Integer.parseInt(quantityEditText.getText().toString()));
        item.setmAlertQuantity(Integer.parseInt(alertEditText.getText().toString()));

        // Create our DAO and save to database
        InventoryDAO inventoryDAO = new InventoryDAO(this.getContext());
        inventoryDAO.saveInventoryToDatabase(item);

        // Navigate back to inventory screen
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavOptions navOptions = new NavOptions.Builder()
                .build();
        navController.navigate(R.id.navigation_inventory,null, navOptions);
    }
}