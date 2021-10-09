package com.myprojects.bety2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.Home;
import com.myprojects.bety2.classes.LM;
import com.myprojects.bety2.dialogs.AddHomeDialog;
import com.myprojects.bety2.fragments.MyHomesFragment;
import com.myprojects.bety2.fragments.ProfileFragment;
import com.myprojects.bety2.fragments.SettingsFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomesActivity extends AppCompatActivity implements AddHomeDialog.AddHomeListener {

    private long mBackPressedTime;
    private TextView mTitleHomes;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LM.loadLocale(this);
        setContentView(R.layout.activity_homes);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mTitleHomes = findViewById(R.id.text_homes_title);
        mTitleHomes.setText(LM.translate("my_homes", this));

        // google ads
        AdView mAdView = findViewById(R.id.adViewHomes);
        MobileAds.initialize(HomesActivity.this, initializationStatus -> {});
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_profile);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(itemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_homes_container, new MyHomesFragment()).commit();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.my_homes_menu_items, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        String mSharedPreferenceLang = LM.mSharedPreferenceLang;
//
//        switch (item.getItemId()) {
//            case R.id.menu_sign_out: {
//                AlertDialog alertDialog = new AlertDialog.Builder(HomesActivity.this)
//                        .setTitle(LM.translate("sign_out", HomesActivity.this))
//                        .setMessage(LM.translate("sure_exit", HomesActivity.this))
//                        .setPositiveButton(LM.translate("yes", HomesActivity.this), ((dialog, which) -> {dialog.dismiss(); logOut(); }))
//                        .setNegativeButton(LM.translate("no", HomesActivity.this), null)
//                        .show();
//
//            }
//            break;
//            case R.id.menu_language: {
//                if (mSharedPreferenceLang.equals("ar"))
//                    LM.setLocale("en", HomesActivity.this);
//                else
//                    LM.setLocale("ar", HomesActivity.this);
//                recreate();
//            }
//            break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + item.getItemId());
//        }
//        return super.onOptionsItemSelected(item);
//    }

//    private void logOut() {
//        ApiManager.connectToApi();
//
//        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
//        String token = preferences.getString("Tokens", null);
//
//        Call<Void> call = ApiManager.apiUrl.logOut("Bearer " + token);
//
//        call.enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if(response.isSuccessful()) {
//                    deleteFromSharedPreferences("Tokens");
//                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
//                    finish();
//                }
//                // In case of unsuccessful response
//                else Toast.makeText(getApplicationContext(), LM.translate("error", getApplicationContext()), Toast.LENGTH_LONG).show();
//            }
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Error, Try again " + t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });
//    }

//    private void deleteFromSharedPreferences(String tokens) {
//        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
//        editor.remove(tokens);
//        editor.apply();
//    }

    @Override
    public void onBackPressed() {
        if(mBackPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else
            Toast.makeText(getApplicationContext(), LM.translate("press_back", this), Toast.LENGTH_SHORT).show();
        mBackPressedTime = System.currentTimeMillis();
    }

    // BottomNavigation listener
    // To change the fragment
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener
            itemSelectedListener = item -> {
        Fragment selectedFragment = null;

        switch (item.getItemId()) {
            case R.id.nav_home: {
                selectedFragment = new MyHomesFragment();
                mTitleHomes.setText(LM.translate("my_homes", this));
                break;
            }
            case R.id.nav_settings: {
                selectedFragment = new SettingsFragment();
                mTitleHomes.setText(LM.translate("settings", this));
                break;
            }
            case R.id.nav_profile: {
                selectedFragment = new ProfileFragment();
                mTitleHomes.setText(LM.translate("profile", this));
                break;
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_homes_container, selectedFragment).commit();
        return true;
    };

    // POST: /homes/
    // header: token from sahredpreference
    // body: homeName
    @Override
    public void addNewHome(Home home) {
        ApiManager.connectToApi();

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String token = preferences.getString("Tokens", null);

        Call<JsonObject> call = ApiManager.apiUrl.addHome(home, "Bearer " + token);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_homes_container, new MyHomesFragment()).commit();
                } else
                    Toast.makeText(getApplicationContext(), LM.translate("error", getApplicationContext()), Toast.LENGTH_LONG).show();
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "ERROR, " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}