package com.zybooks.inventoryapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View view;
    private static final int REQUEST_SMS_PERMISSION = 1;

    private ActivityResultLauncher<String> requestPermissionLauncher;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            SharedPreferences sharedPrefs = getActivity().getSharedPreferences("smsPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPrefs.edit();
            editor.putBoolean("smsEnabled", true);
            editor.apply();
            showPhoneDialog();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        loadSMSPrefs();
        Switch smsToggle = view.findViewById(R.id.sms_toggle_switch);
        smsToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    showPermissionDialog();
                }
                else{
                    SharedPreferences sharedPrefs = getActivity().getSharedPreferences("smsPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("smsEnabled", false);
                    editor.apply();
                }
            }
        });
        return view;
    }

    public void showPhoneDialog(){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.phone_popup_layout, null);
        EditText smsEditText = dialogView.findViewById(R.id.sms_number_edit_text);

        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("SMS Mobile Number")
                .setView(dialogView)
                .setPositiveButton("OK", null) // Initially setting to null
                .setNegativeButton("Cancel", (d, which) -> {
                    Switch smsToggle = view.findViewById(R.id.sms_toggle_switch);
                    smsToggle.setChecked(false);
                    SharedPreferences sharedPrefs = getActivity().getSharedPreferences("smsPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPrefs.edit();
                    editor.putBoolean("smsEnabled", false);
                    editor.apply();
                    d.cancel();
                })
                .create();

        dialog.setOnShowListener(d -> {
            Button positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false); // Disable the button initially

            smsEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Enable button only if EditText is not empty
                    positiveButton.setEnabled(s.length() == 10);
                }
            });

            positiveButton.setOnClickListener(v -> {
                // Handle the OK button click
                String smsNumber = smsEditText.getText().toString();
                TextView smsText = view.findViewById(R.id.sms_mobile_number_text_view);
                SharedPreferences sharedPrefs = getActivity().getSharedPreferences("smsPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPrefs.edit();
                editor.putString("smsNumber", smsNumber);
                smsText.setText(smsNumber);
                editor.apply();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
    public void showPermissionDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Permission Warning");
        alert.setMessage("This application uses SMS messaging to send alerts.");
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissionLauncher.launch(Manifest.permission.SEND_SMS);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    public void loadSMSPrefs(){
        SharedPreferences sharedPrefs = getActivity().getSharedPreferences("smsPrefs", Context.MODE_PRIVATE);
        Switch smsToggle = view.findViewById(R.id.sms_toggle_switch);
        TextView mobileNumberText = view.findViewById(R.id.sms_mobile_number_text_view);
        smsToggle.setChecked(sharedPrefs.getBoolean("smsEnabled", false));
        mobileNumberText.setText(sharedPrefs.getString("smsNumber", ""));


    }

}