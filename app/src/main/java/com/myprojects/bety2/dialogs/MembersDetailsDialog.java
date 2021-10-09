package com.myprojects.bety2.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;
import com.myprojects.bety2.adapters.MemberAdapter;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MembersDetailsDialog extends Dialog {

    public Activity activity;
    private String homeId;
    private ImageButton mAddNewMember;
    private final List<User> mUsers = new ArrayList<>();
    private RecyclerView mMembersRecycler;
    private RecyclerView.Adapter mMembersAdapter;

    private static String TAG = "MembersDetailsDialog";

    public MembersDetailsDialog(@NonNull Activity activity, String homeID) {
        super(activity);
        this.activity = activity;
        this.homeId = homeID;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.dialog_members_details);

        // Setting the background to TRANSPARENT to show .xml background
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mAddNewMember = findViewById(R.id.button_add_new_member);
        mMembersRecycler = findViewById(R.id.recycler_members_template);

        mAddNewMember.setOnClickListener(v -> {

            Toast.makeText(getContext(), "Testing adding new member", Toast.LENGTH_LONG).show();

            AddMemberDialog dialog = new AddMemberDialog(activity, homeId);
            dialog.show();
            // Add .xml file for the dialog
        });

        LinearLayoutManager layout = new LinearLayoutManager(activity);
        mMembersRecycler.setLayoutManager(layout);
        mMembersAdapter = new MemberAdapter(mUsers, activity.getApplicationContext());
        mMembersRecycler.setAdapter(mMembersAdapter);

        getMembersFromApi();

    }

    private void getMembersFromApi() {
        ApiManager.connectToApi();

        String token = getToken();

        Call<JsonArray> call = ApiManager.apiUrl.getMembers(homeId, "Bearer " + token);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if(response.isSuccessful()) {
                    JsonArray array = response.body();
                    Gson gson = new GsonBuilder().serializeNulls().create();

                    for(JsonElement e : array) {
                        User user = gson.fromJson(e, User.class);
                        mUsers.add(user);
                    }
                    mMembersRecycler.setAdapter(mMembersAdapter);
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    private String getToken() {
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }
}