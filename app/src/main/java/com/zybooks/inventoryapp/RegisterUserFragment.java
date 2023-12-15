package com.zybooks.inventoryapp;

import android.app.AlertDialog;
import android.os.Bundle;

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
 * Use the {@link RegisterUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private boolean enableRegister = false;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    View view;

    public RegisterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterUserFragment newInstance(String param1, String param2) {
        RegisterUserFragment fragment = new RegisterUserFragment();
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
        view = inflater.inflate(R.layout.fragment_register_user, container, false);
        Button registerButton = view.findViewById(R.id.registerSaveButton);
        registerButton.setEnabled(enableRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        EditText passwordText = view.findViewById(R.id.registerPasswordEditText);
        EditText confirmPasswordText = view.findViewById(R.id.registerConfirmPasswordEditText);
        EditText userNameText = view.findViewById(R.id.registerUserNameEditText);
        EditText firstNameText = view.findViewById(R.id.registerFirstNameEditText);
        EditText lastNameText = view.findViewById(R.id.registerLastNameEditText);
        EditText emailText = view.findViewById(R.id.registerEmailEditText);
        passwordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerButton.setEnabled(
                        userNameText.length() > 0 &&
                        passwordText.length() > 0 &&
                        confirmPasswordText.length() > 0 &&
                        firstNameText.length() > 0 &&
                        lastNameText.length() > 0 &&
                        emailText.length() > 0
                );
            }
        });

        confirmPasswordText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerButton.setEnabled(
                        userNameText.length() > 0 &&
                        passwordText.length() > 0 &&
                        confirmPasswordText.length() > 0 &&
                        firstNameText.length() > 0 &&
                        lastNameText.length() > 0 &&
                        emailText.length() > 0
                );
            }
        });
        userNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerButton.setEnabled(
                        userNameText.length() > 0 &&
                        passwordText.length() > 0 &&
                        confirmPasswordText.length() > 0 &&
                        firstNameText.length() > 0 &&
                        lastNameText.length() > 0 &&
                        emailText.length() > 0
                );
            }
        });
        firstNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerButton.setEnabled(
                        userNameText.length() > 0 &&
                        passwordText.length() > 0 &&
                        confirmPasswordText.length() > 0 &&
                        firstNameText.length() > 0 &&
                        lastNameText.length() > 0 &&
                        emailText.length() > 0
                );
            }
        });

        lastNameText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerButton.setEnabled(
                        userNameText.length() > 0 &&
                        passwordText.length() > 0 &&
                        confirmPasswordText.length() > 0 &&
                        firstNameText.length() > 0 &&
                        lastNameText.length() > 0 &&
                        emailText.length() > 0
                );
            }
        });

        emailText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                registerButton.setEnabled(
                        userNameText.length() > 0 &&
                        passwordText.length() > 0 &&
                        confirmPasswordText.length() > 0 &&
                        firstNameText.length() > 0 &&
                        lastNameText.length() > 0 &&
                        emailText.length() > 0
                );
            }
        });

        return view;
    }

    public void register(){
       EditText passwordText = view.findViewById(R.id.registerPasswordEditText);
       EditText confirmPasswordText = view.findViewById(R.id.registerConfirmPasswordEditText);
       if(!passwordText.getText().toString().equals(confirmPasswordText.getText().toString())){
           AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
           alert.setTitle("Password Mismatch");
           alert.setMessage("The entered passwords do not match.");
           alert.setPositiveButton("OK", null);
           AlertDialog dialog = alert.create();
           dialog.show();
           return;
       }
       EditText userIdText = view.findViewById(R.id.registerUserNameEditText);
       EditText userEmailText = view.findViewById(R.id.registerEmailEditText);
       EditText userFirstNameText = view.findViewById(R.id.registerFirstNameEditText);
       EditText userLastNameText = view.findViewById(R.id.registerLastNameEditText);
       UserDAO.User user = new UserDAO.User();
       user.setmUserId(userIdText.getText().toString());
       user.setmUserPass(passwordText.getText().toString());
       user.setmUserEmail(userEmailText.getText().toString());
       user.setmFirstName(userFirstNameText.getText().toString());
       user.setmLastName(userLastNameText.getText().toString());


       InventoryAppDatabase userDB = new InventoryAppDatabase(view.getContext());
       if(userDB.createUser(user)){
           passwordText.setText("");
           confirmPasswordText.setText("");
           userFirstNameText.setText("");
           userLastNameText.setText("");
           userEmailText.setText("");
           AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
           alert.setTitle("Registration Complete");
           alert.setMessage("Thank you for registering. You may now log in.");
           alert.setPositiveButton("OK", null);
           AlertDialog dialog = alert.create();
           dialog.show();
           NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
           NavController navController = navHostFragment.getNavController();
           NavOptions navOptions = new NavOptions.Builder()
                   .build();
           navController.navigate(R.id.navigation_sign_in, null, navOptions);
       }
    }
}