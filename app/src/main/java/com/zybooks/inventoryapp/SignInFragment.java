package com.zybooks.inventoryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
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
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean userIdEmpty = true;
    private boolean userPassEmpty = true;

    private View view;
    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
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
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        view.findViewById(R.id.signInButton).setEnabled(false);
        EditText userIdText = view.findViewById(R.id.userNameEditText);
        EditText userPassText = view.findViewById(R.id.passwordEditText);
        Button signInButton = view.findViewById(R.id.signInButton);
        Button registerButton = view.findViewById(R.id.registerButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        userIdText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userIdEmpty = s.length() < 1;
                view.findViewById(R.id.signInButton).setEnabled(!userPassEmpty && !userIdEmpty);
            }
        });
        userPassText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                userPassEmpty = s.length() < 1;
                view.findViewById(R.id.signInButton).setEnabled(!userPassEmpty && !userIdEmpty);
            }
        });

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null && activity.getSupportActionBar() != null) {
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }

        return view;
    }
    public void signIn(){
        EditText userIdText = view.findViewById(R.id.userNameEditText);
        String userId = userIdText.getText().toString();
        EditText userPassText = view.findViewById(R.id.passwordEditText);
        String userPass = userPassText.getText().toString();
        InventoryAppDatabase userDB = new InventoryAppDatabase(view.getContext());
        UserViewModel userView = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        if(userDB.authenticateUser(userId, userPass)){
            userView.setUserLoginStatus(true);
            UserDAO users = new UserDAO(getContext());
            UserDAO.User user = users.getUser(userId);
            if(user != null){
                userView.setUser(user);
            }

        }
        else {
            userIdText.setText("");
            userPassText.setText("");
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Authentication Error");
            alert.setMessage("The username or password entered is incorrect.");
            alert.setPositiveButton("OK", null);
            AlertDialog dialog = alert.create();
            dialog.show();
        }

    }

    public void registerUser(){
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavOptions navOptions = new NavOptions.Builder()
                .build();
        navController.navigate(R.id.navigation_register_user, null, navOptions);
    }

}