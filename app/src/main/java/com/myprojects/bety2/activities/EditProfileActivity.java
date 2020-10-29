package com.myprojects.bety2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.myprojects.bety2.R;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    String userJson;
    User user;

    private TextView mFirstNameEdit, mLastNameEdit, mUsernameEdit, mEmailEdit, /*mPasswordEdit,*/ mPhoneEdit, mJoinedEdit;
    private Button mUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        findViews();

        // getting data from previous activity (profileFragment) as json
        Bundle extras = getIntent().getExtras();
        userJson = extras == null ? null : extras.getString("user");
        // converting json object to User object
        user = (new Gson()).fromJson(userJson, User.class);

        // show user data
        showUserData();

        mUpdate.setOnClickListener(v -> {
            updateUserData();
        });

//        Toast.makeText(getApplicationContext(), user.getEmail() + "", Toast.LENGTH_LONG).show();
    }

    private void updateUserData() {
        // get data from activity
        User updatedUser = User.builder()
                .firstName(mFirstNameEdit.getText().toString())
                .lastName(mLastNameEdit.getText().toString())
                .username(mUsernameEdit.getText().toString())
                .email(mEmailEdit.getText().toString())
                .phoneNumber(Long.parseLong(mPhoneEdit.getText().toString()))
                .build();

        sendUpdatedDataToApi(updatedUser);

    }

    private void sendUpdatedDataToApi(User updatedUser) {
        ApiManager.connectToApi();

        String token = getToken();
        Call<Void> call = ApiManager.apiUrl.updateUser(updatedUser,"Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_LONG).show();
            }
        });
    }

    private String getToken() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }

    private void showUserData() {
        mFirstNameEdit.setText(user.getFirstName());
        mLastNameEdit.setText(user.getLastName());
        mUsernameEdit.setText(user.getUsername());
        mEmailEdit.setText(user.getEmail());
        mPhoneEdit.setText(String.valueOf(user.getPhoneNumber()));
    }

    private void findViews() {
//        mImageProfile = findViewById(R.id.imageB_edit_profile);

        mFirstNameEdit = findViewById(R.id.text_first_name_edit_profile);
        mLastNameEdit = findViewById(R.id.text_last_name_edit_profile);
        mUsernameEdit = findViewById(R.id.text_username_edit_profile);
        mEmailEdit = findViewById(R.id.text_email_edit_profile);
//        mPasswordEdit = findViewById(R.id.text_password_profile);
        mPhoneEdit = findViewById(R.id.text_phone_edit_profile);
        mJoinedEdit = findViewById(R.id.text_joined_edit_profile);

        mUpdate = findViewById(R.id.button_update_edit_profile);
    }
}