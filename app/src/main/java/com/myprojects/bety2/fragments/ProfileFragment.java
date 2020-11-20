/**
 * ToDo:
 * 1- Get the password from Back-End not encrypted
* */

package com.myprojects.bety2.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;
import com.myprojects.bety2.activities.EditProfileActivity;
import com.myprojects.bety2.activities.LoginActivity;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.LM;
import com.myprojects.bety2.classes.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private ImageButton mImageEdit;
    private TextView mNameProfile, mUsernameProfile, mEmailProfile, /*mPasswordProfile,*/ mPhoneProfile, mJoinedProfile;
    private Button mLogOut;
//    private ImageButton mShowPassword;
    private Context context;

    public static User currentUser = null;

    String mSharedPreference, token;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        assert container != null;
        context = container.getContext();

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mSharedPreference = LM.mSharedPreferenceLang;
        findViews(view);
        mImageEdit.setEnabled(false);

        token = getToken();

        getUserData();

        mLogOut.setOnClickListener(v -> {
            AlertDialog alertDialog = new AlertDialog.Builder(context)
                    .setTitle(LM.translate("sign_out", context))
                    .setMessage(LM.translate("sure_exit", context))
                    .setPositiveButton(LM.translate("yes", context), ((dialog, which) -> {dialog.dismiss(); logOut(getActivity()); }))
                    .setNegativeButton(LM.translate("no", context), null)
                    .show();

        });

        mImageEdit.setOnClickListener(v -> {
            // Start edit profile activity
            Intent intent = new Intent(context, EditProfileActivity.class);

            intent.putExtra("user", (new Gson()).toJson(currentUser));
            startActivity(intent);
        });

//        mShowPassword.setOnClickListener(v -> {
//            Toast.makeText(getContext(), "test", Toast.LENGTH_LONG).show();
//            mPasswordProfile.setTransformationMethod(new PasswordTransformationMethod());
//        });

        return view;
    }

    private void getUserData() {
        ApiManager.connectToApi();

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

                if (currentUser != null) {
                    mImageEdit.setEnabled(true);
                    setUserData(currentUser);
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "ERROR, " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void setUserData(User userData) {
        mNameProfile.setText(userData.getFirstName() + " " + userData.getLastName());
        mUsernameProfile.setText(userData.getUsername());
        mEmailProfile.setText(userData.getEmail());
//        mPasswordProfile.setText(userData.getPassword());
        mPhoneProfile.setText(String.valueOf(userData.getPhoneNumber()));
        mJoinedProfile.setText(userData.getJoined());
    }

    private void logOut(FragmentActivity activity) {
        ApiManager.connectToApi();

        Call<Void> call = ApiManager.apiUrl.logOut("Bearer " + token);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()) {
                    deleteFromSharedPreferences("Tokens");
                    startActivity(new Intent(context, LoginActivity.class));
                    activity.finish();
                }
                // In case of unsuccessful response
                else Toast.makeText(context, LM.translate("error", context), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(context, "Error, Try again " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // Deleting tokens when user logged out
    private void deleteFromSharedPreferences(String tokens) {
        SharedPreferences.Editor editor = context.getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.remove(tokens);
        editor.apply();
    }

    private String getToken() {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }

    private void findViews(View view) {

        mImageEdit = view.findViewById(R.id.image_profile_edit);

        mNameProfile = view.findViewById(R.id.text_name_profile);
        mUsernameProfile = view.findViewById(R.id.text_username_profile);
        mEmailProfile = view.findViewById(R.id.text_email_profile);
//        mPasswordProfile = view.findViewById(R.id.text_password_profile);
        mPhoneProfile = view.findViewById(R.id.text_phone_profile);
         mJoinedProfile = view.findViewById(R.id.text_joined_profile);

//        mShowPassword = view.findViewById(R.id.image_show_password);

        mLogOut = view.findViewById(R.id.button_logout_profile);
    }
}
