package com.myprojects.bety2.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
 * 2- sharedPreference not restoring arabic                                                     DONE
 * 1- Move translate() method to LM class                                                       DONE
 * 3- Show setError() in checkInputs() one by one                                               DONE
 * 4- Add ConstraintLayout up of simple_background                                              DONE
 * 5- Check my API response after sign up a new user                                            DONE
 * =  and open next activity after api response                                                 ====
 * 6- Edit response type from api                                                               DONE
 **/

public class RegisterActivity extends AppCompatActivity {

    private EditText mFirstNameEdit, mLastNameEdit, mEmailEdit, mUsernameEdit, mPhoneNumberEdit, mPasswordEdit;
    private Button mSignUpButton;
    private TextView mSignInText, mWrongText;
    private ToggleButton mLanguageToggle;
    private ProgressBar mProgress;

    private boolean isValid = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // load locale from sharedPreference
        LM.loadLocale(this);
        String mSharedPreferenceLang = LM.mSharedPreferenceLang;
        setContentView(R.layout.activity_sign_up);

        // get views of all components
        findViews();

        // ClickListeners
        mSignUpButton.setOnClickListener( v -> {
            checkInputs();
            if(isValid)
                signUp();
        });

        mSignInText.setOnClickListener( v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        });

        mLanguageToggle.setChecked(!mSharedPreferenceLang.equals("ar"));

        mLanguageToggle.setOnClickListener( v -> {
            if(mLanguageToggle.isChecked()) {
                LM.setLocale("en", this);
            }
            else {
                LM.setLocale("ar", this);
            }
            recreate();
        });
    } // End of onCreate()

    private void checkInputs() {
        if(isEmpty(mFirstNameEdit)) {
            mFirstNameEdit.setError(LM.translate("required_input", this)); isValid = false; return;}
        if(isEmpty(mLastNameEdit)) {
            mLastNameEdit.setError(LM.translate("required_input", this)); isValid = false; return;}
        if(isEmpty(mEmailEdit)) {
            mEmailEdit.setError(LM.translate("required_input", this)); isValid = false; return;}
        if(isEmpty(mUsernameEdit)) {
            mUsernameEdit.setError(LM.translate("required_input", this)); isValid = false; return;}
        if(isEmpty(mPhoneNumberEdit)) {
            mPhoneNumberEdit.setError(LM.translate("required_input", this)); isValid = false; return;}
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

    private void signUp() {
        mProgress.setVisibility(View.VISIBLE);

        User user = User.builder()
                .firstName(mFirstNameEdit.getText().toString())
                .lastName(mLastNameEdit.getText().toString())
                .email(mEmailEdit.getText().toString())
                .username(mUsernameEdit.getText().toString())
                .phoneNumber(Long.parseLong(mPhoneNumberEdit.getText().toString()))
                .password(mPasswordEdit.getText().toString())
                .build();

        ApiManager.connectToApi();

        Call<JsonObject> call = ApiManager.apiUrl.register(user);

        call.enqueue(new Callback<JsonObject>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mProgress.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    mWrongText.setVisibility(View.VISIBLE);
                    String message = response.errorBody().string();
                    mWrongText.setText(message.substring(12, message.length()-2));
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), HomesActivity.class));
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgress.setVisibility(View.GONE);
                mWrongText.setVisibility(View.VISIBLE);
                mWrongText.setText(t.getMessage());
            }
        });
    }

    private void findViews() {
        mFirstNameEdit = findViewById(R.id.edit_first_name_sign_up);
        mLastNameEdit = findViewById(R.id.edit_last_name_sign_up);

        mEmailEdit = findViewById(R.id.edit_email_sign_up);
        mUsernameEdit = findViewById(R.id.edit_username);
        mPhoneNumberEdit = findViewById(R.id.edit_phone_sign_up);
        mPasswordEdit = findViewById(R.id.edit_password_sign_up);

        mSignUpButton = findViewById(R.id.button_sign_up);

        mSignInText = findViewById(R.id.text_sign_in);

        mLanguageToggle = findViewById(R.id.toggle_language_sign_up);

        mWrongText = findViewById(R.id.test_text);

        mProgress = findViewById(R.id.progress_sign_up);
    }
}