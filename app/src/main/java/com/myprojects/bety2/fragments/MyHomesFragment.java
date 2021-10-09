package com.myprojects.bety2.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;
import com.myprojects.bety2.activities.StuffsActivity;
import com.myprojects.bety2.adapters.HomesAdapter;
import com.myprojects.bety2.classes.Home;
import com.myprojects.bety2.classes.User;
import com.myprojects.bety2.dialogs.AddHomeDialog;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.LM;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ToDo:
 * 1- Send the user token when fetching data from api.                                          DONE
 **/

public class MyHomesFragment extends Fragment implements HomesAdapter.OnHomeClickListener{

    private Context context;

    private List<Home> mHomes = new ArrayList<>();
    private RecyclerView mHomesRecycler;
    private static RecyclerView.Adapter mHomesAdapter;
    private TextView mBackText, mCreateHomeText;
    private ProgressBar mHomesProgressBar;
    private final FragmentActivity activity = getActivity();

    String mSharedPreference;

    @SneakyThrows
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = container.getContext();
        LM.loadLocale(context);
        View view = inflater.inflate(R.layout.fragment_homes, container, false);

        mSharedPreference = LM.mSharedPreferenceLang;
        findViews(view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(activity);
        mHomesRecycler.setLayoutManager(layoutManager);
        mHomesAdapter = new HomesAdapter(mHomes, activity, this);
        mHomesRecycler.setAdapter(mHomesAdapter);
        mHomesProgressBar.setVisibility(View.VISIBLE);

        getHomesFromApi();

        mCreateHomeText.setOnClickListener(createHomeListener);

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    public void getHomesFromApi() {
        ApiManager.connectToApi();

        String token = getToken();
        Call<JsonArray> call = ApiManager.apiUrl.getHomes("Bearer " + token);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                mHomesProgressBar.setVisibility(View.GONE);

                if(response.isSuccessful()) {
                    JsonArray jsonArray = response.body();
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    for (JsonElement e : jsonArray) {
                        Home home = gson.fromJson(e, Home.class);
                        mHomes.add(home);
                    }
                    mHomesRecycler.setAdapter(mHomesAdapter);
                }
                else if(response.code() == 404) {
                    mBackText.setVisibility(View.VISIBLE);
                    mBackText.setText(LM.translate("noHomes", context));
                } else {
                    mBackText.setVisibility(View.VISIBLE);
                    mBackText.setText(LM.translate("error", context));
                }
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                mHomesProgressBar.setVisibility(View.GONE);

                mBackText.setVisibility(View.VISIBLE);
                mBackText.setText(t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences preferences = getContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }

    View.OnClickListener createHomeListener = v -> {
        AddHomeDialog dialog = new AddHomeDialog();
        dialog.show(requireActivity().getSupportFragmentManager(), "Test Dialog");
    };

    // on home item clicked implements
    @Override
    public void onClickHome(int position, String homeId) {
        Intent intent = new Intent(context, StuffsActivity.class);
        // Sending homeID to StuffsActivity
        intent.putExtra("homeId", homeId);
        startActivity(intent);
    }

    private void findViews(View view) {
        mHomesRecycler = view.findViewById(R.id.recycler_homes);
        mBackText = view.findViewById(R.id.text_homes_back_fragment);
        mCreateHomeText = view.findViewById(R.id.text_create_home);
        mHomesProgressBar = view.findViewById(R.id.progress_homes);
    }
}