package com.myprojects.bety2.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;
import com.myprojects.bety2.activities.HomesActivity;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.LM;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInfoDialog extends Dialog {

    Activity activity;
    TextView mErrorInfoText, mHomeNameInfo, mHomeMembersInfo, mHomeStuffsInfo, mHomeCreatedInfo;
    Button mDeleteHomeInfo, mCloseHomeInfo;
    ProgressBar mProgressBar;
    String homeId;

    public HomeInfoDialog(@NonNull Activity activity, String id) {
        super(activity);

        this.activity = activity;
        this.homeId = id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.dialog_home_info);

        findViews();
        getHomeInfo();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // clicks listeners

        // delete
        mDeleteHomeInfo.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage(LM.translate("sure_delete", activity))
                    .setPositiveButton(LM.translate("yes", activity), (dialog, which) -> deleteThisHome())
                    .setNegativeButton(LM.translate("no", activity), (dialog, which) -> dialog.dismiss())
                    .show();
        });

//      close
        mCloseHomeInfo.setOnClickListener(v -> {
            this.dismiss();
        });
    }

    private void findViews() {
        mErrorInfoText   = findViewById(R.id.text_error_info);
        mHomeNameInfo    = findViewById(R.id.text_home_name_info);
        mHomeMembersInfo = findViewById(R.id.text_home_members_info);
        mHomeStuffsInfo  = findViewById(R.id.text_home_stuffs_info);
        mHomeCreatedInfo = findViewById(R.id.text_home_created_info);

        mDeleteHomeInfo  = findViewById(R.id.button_delete_home_info);
        mCloseHomeInfo   = findViewById(R.id.button_close_info);

        mProgressBar = findViewById(R.id.progress_home_info);
    }

    private void getHomeInfo() {
        mProgressBar.setVisibility(View.VISIBLE);

        String token = getToken();

        ApiManager.connectToApi();

        Call<JsonObject> call = ApiManager.apiUrl.getHomeInfo(homeId, "Bearer " + token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    mProgressBar.setVisibility(View.GONE);

                    JsonObject jsonObject = response.body();
                    mHomeNameInfo.setText(jsonObject.get("name").getAsString());
                    mHomeMembersInfo.setText(jsonObject.get("members").getAsString());
                    mHomeStuffsInfo.setText(jsonObject.get("stuffs").getAsString());
                    mHomeCreatedInfo.setText(jsonObject.get("created").getAsString());

                } else {
                    mProgressBar.setVisibility(View.GONE);

                    mErrorInfoText.setVisibility(View.VISIBLE);
                    mErrorInfoText.setText(LM.translate("error", activity));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mErrorInfoText.setVisibility(View.VISIBLE);
                mErrorInfoText.setText(LM.translate("error", activity));

                Toast.makeText(getContext(), "ERROR " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private String getToken() {
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }

    private void deleteThisHome() {
        mProgressBar.setVisibility(View.VISIBLE);
        ApiManager.connectToApi();

        String token = getToken();

        Call<JsonObject> call = ApiManager.apiUrl.deleteHome(homeId, "Bearer " + token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                mProgressBar.setVisibility(View.GONE);

                if(!response.isSuccessful()) {
                    mErrorInfoText.setVisibility(View.VISIBLE);
                    mErrorInfoText.setText(LM.translate("error", activity));
                } else {
                    Toast.makeText(getContext(), "Deleted", Toast.LENGTH_LONG).show();
                    dismiss();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);

                mErrorInfoText.setVisibility(View.VISIBLE);
                mErrorInfoText.setText(LM.translate("error", activity));
                Toast.makeText(getContext(), "Failed, " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        activity.finish();
        activity.startActivity(new Intent(getContext(), HomesActivity.class));
    }
}