package com.myprojects.bety2.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.myprojects.bety2.R;
import com.myprojects.bety2.api.ApiManager;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMemberDialog extends Dialog {

    private TextView mUsername;
    private Button mAddNew;
    private String homeID;

    public AddMemberDialog(@NonNull @NotNull Context context, String homeId) {
        super(context);
        this.homeID = homeId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.dialog_add_member);

        mUsername = findViewById(R.id.edit_username_fragment);
        mAddNew = findViewById(R.id.button_add_member);

        mAddNew.setOnClickListener(v -> {
            ApiManager.connectToApi();

            String token = getToken(),
            username = mUsername.getText().toString();

            Call<Void> call = ApiManager.apiUrl.addMember("Berer " + token, username, homeID);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_LONG).show();
                }
            });

        });
    }

    private String getToken() {
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }
}
