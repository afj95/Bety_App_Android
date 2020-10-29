package com.myprojects.bety2.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;

import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.LM;
import com.myprojects.bety2.classes.User;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ToDo:
 * 1- Add ConstraintLayout up of simple_background                                              DONE
 * 2 Check if password is wrong                                                                 DONE
 * 3- Press back again to exit                                                                  DONE
 **/

public class LoginActivity extends AppCompatActivity {

    private EditText mUsername, mPasswordEdit;
    private Button mSignInButton;
    private TextView mSignUpText, mWrongText;
    private ToggleButton mLanguageToggle;
    private ProgressBar mProgress;

    private boolean isValid = true;

    public static User currentUser = null;
    public static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LM.loadLocale(this);
        String mSharedPreferenceLang = LM.mSharedPreferenceLang;
        setContentView(R.layout.activity_sign_in);

        findViews();

        mLanguageToggle.setChecked(!mSharedPreferenceLang.equals("ar"));

        mLanguageToggle.setOnClickListener( v -> {
            if(mLanguageToggle.isChecked())
                LM.setLocale("en", this);
            else
                LM.setLocale("ar", this);
            recreate();
        });

        mSignUpText.setOnClickListener( v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    @Override
    protected void onStart() {
        super.onStart();

        getCurrentUser();

        mSignInButton.setOnClickListener(loginClickListener);
    }

    private void getCurrentUser() {
        mProgress.setVisibility(View.VISIBLE);
        ApiManager.connectToApi();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String token = preferences.getString("Tokens", null);

        if(token != null) {

            Call<JsonObject> call = ApiManager.apiUrl.getCurrentUser("Bearer " + token);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    JsonObject jsonObject = response.body();

                    if (response.isSuccessful()) {
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        currentUser = gson.fromJson(jsonObject.get("user").getAsJsonObject(), User.class);
                    }
                    // In case response not successful
                    else currentUser = null;

                    if(currentUser != null) {
                        mProgress.setVisibility(View.GONE);
                        Intent intent = new Intent(getApplicationContext(), HomesActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    mProgress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "ERROR, " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } else mProgress.setVisibility(View.GONE);
    }

    View.OnClickListener loginClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkInputs();
            if(isValid) {
                String username = mUsername.getText().toString(),
                       password = mPasswordEdit.getText().toString();
                login(username, password);
            }
        }
    };

    private void checkInputs() {
        if(isEmpty(mUsername)) {
            mUsername.setError(LM.translate("required_input", this)); isValid = false; return;}
        if(isEmpty(mPasswordEdit)) {
            mPasswordEdit.setError(LM.translate("required_input", this)); isValid = false; return;}
        checkPassword();
    }

    private boolean isEmpty(EditText text) {
        return TextUtils.isEmpty(text.getText().toString());
    }

    private void checkPassword() {
        if(mPasswordEdit.getText().toString().length() < 8) {
            mPasswordEdit.setError(LM.translate("pass_length", this));
            isValid = false;
        }
    }

    private void login(String username, String password) {
        mProgress.setVisibility(View.VISIBLE);

        User user = User.builder().username(username).password(password).build();

        ApiManager.connectToApi();

        Call<JsonObject> call = ApiManager.apiUrl.logIn(user);

        call.enqueue(new Callback<JsonObject>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                if(response.isSuccessful()) {
                    Gson gson = new GsonBuilder().serializeNulls().create(); // To convert from json to java object
                    JsonObject jsonObject = response.body();

                    currentUser = gson.fromJson(jsonObject.get("user").getAsJsonObject(), User.class);
                    token = jsonObject.get("token").getAsString();
                    storeTokenInSharedPreference(token);
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomesActivity.class));
                }
                else if(response.code() == 404) {
                    mProgress.setVisibility(View.GONE);
                    mWrongText.setVisibility(View.VISIBLE);
                    mWrongText.setText(LM.translate("wrong_pass", getApplicationContext()));
                }
                else {
                    mProgress.setVisibility(View.GONE);
                    mWrongText.setVisibility(View.VISIBLE);
                    mWrongText.setText(LM.translate("error", getApplicationContext()));
                }
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                mWrongText.setVisibility(View.VISIBLE);
                mWrongText.setText(LM.translate("error", getApplicationContext()) + t.getMessage());
            }
        });
    }

    private void storeTokenInSharedPreference(String tokens) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("Tokens", tokens);
        editor.apply();
    }

    private void findViews() {
        mUsername = findViewById(R.id.edit_email_sign_in);
        mPasswordEdit = findViewById(R.id.edit_password_sign_in_);

        mSignInButton = findViewById(R.id.button_sign_in);

        mSignUpText = findViewById(R.id.text_sign_up);

        mLanguageToggle = findViewById(R.id.toggle_language_sign_up);

        mProgress = findViewById(R.id.progress_sign_in);

        mWrongText = findViewById(R.id.text_wrong);
    }
}
