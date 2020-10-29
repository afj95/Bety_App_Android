package com.myprojects.bety2.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.myprojects.bety2.R;
import com.myprojects.bety2.adapters.StuffAdapter;
import com.myprojects.bety2.api.ApiManager;
import com.myprojects.bety2.classes.LM;
import com.myprojects.bety2.classes.Stuff;
import com.myprojects.bety2.classes.User;
import com.myprojects.bety2.dialogs.AddMemberDialog;
import com.myprojects.bety2.dialogs.AddStuffDialog;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * ToDo:
 * 1- Press back again to exit.                                                                 DONE
 * 2- Only admins can add new members
 * 3- Delete specific stuff
 * 4- Delete all checked stuff
 **/

public class StuffsActivity
        extends AppCompatActivity
        implements AddStuffDialog.AddStuffListener,
                   AddMemberDialog.AddMemberListener,
                   StuffAdapter.OnItemCheckedListener {

    private String TAG = "StuffsActivity";

    private List<Stuff> mStuffs = new ArrayList<>();
    private RecyclerView mStuffRecycler;
    private SwipeRefreshLayout mStuffSwipeRefresh;
    private RecyclerView.Adapter mStuffAdapter;
    private FloatingActionButton mAddStuffFloating;
    private ProgressBar mProgressBar;
    private TextView mTextBackStuffs;
    private AdView mAdView;

    private String homeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LM.loadLocale(StuffsActivity.this);

        setContentView(R.layout.activity_stuffs);

        findViews();

        // Getting homeId from previous activity (MyHomesFragment)
        Bundle extras = getIntent().getExtras();
        homeId = extras == null ? null : extras.getString("homeId");

        mStuffAdapter = new StuffAdapter(mStuffs, this, this);
        mStuffRecycler.setHasFixedSize(true);
        mStuffRecycler.setLayoutManager(new LinearLayoutManager(this));
        mStuffRecycler.addOnScrollListener(mOnScrollListener);

        getStuffsFromApi();

        // google ads
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        // Listeners

        // Floating button
        mAddStuffFloating.setOnClickListener(v -> openAddStuffDialog() );

        // swipe for refreshing
        mStuffSwipeRefresh.setOnRefreshListener(() -> {
            mStuffs.clear();
            getStuffsFromApi();
            mStuffAdapter.notifyDataSetChanged();
            mStuffSwipeRefresh.setRefreshing(false);
        });

        // random data for testing
        Stuff stuff = Stuff.builder()
                .stuff("Test")
                .homeId(homeId)
                .build();
        mStuffs.add(stuff);
        mStuffRecycler.setAdapter(mStuffAdapter);

//        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mStuffRecycler);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.stuffs_fragment_menu_items, menu);

        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.menu_add_user) {
//            // add member dialog
//            AddMemberDialog dialog = new AddMemberDialog();
//            dialog.show(getSupportFragmentManager(), "add member dialog");
//        }
//        return super.onOptionsItemSelected(item);
//    }

    // Get the data for recyclerView
    private synchronized void getStuffsFromApi() {
        mProgressBar.setVisibility(View.VISIBLE);
        mStuffSwipeRefresh.setEnabled(false);

        String token = getToken();

        ApiManager.connectToApi();

        // Get data from api based on homeId user clicked
        Call<JsonArray> call = ApiManager.apiUrl.getStuffs(homeId, "Bearer " + token);

        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NotNull Call<JsonArray> call, @NotNull Response<JsonArray> response) {
                mProgressBar.setVisibility(View.GONE);

                JsonArray jsonArray = response.body();
                if(response.isSuccessful()) {
                    Gson gson = new GsonBuilder().serializeNulls().create();
                    if (jsonArray.size() > 0) {
                        for (JsonElement e : jsonArray) {
                            Stuff stuff = gson.fromJson(e, Stuff.class);
                            mStuffs.add(stuff);
                        }
                      // In case of no stuffs in this home were added
                    } else {
                        mTextBackStuffs.setVisibility(View.VISIBLE);
                        mTextBackStuffs.setText(LM.translate("empty", getApplicationContext()));
                    }
                  // In case of unsuccessful response
                } else {
                    mTextBackStuffs.setVisibility(View.VISIBLE);
                    mTextBackStuffs.setText(LM.translate("error", getApplicationContext()));
                }
                // Setting the data to the adapter
                mStuffRecycler.setAdapter(mStuffAdapter);
                // Enabling swipe to refresh
                mStuffSwipeRefresh.setEnabled(true);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onFailure(@NotNull Call<JsonArray> call, @NotNull Throwable t) {
                mProgressBar.setVisibility(View.GONE);

                mTextBackStuffs.setVisibility(View.VISIBLE);
                mTextBackStuffs.setText(LM.translate("error ", getApplicationContext()) + t.getMessage());
            }
        });
    }

    private String getToken() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        return preferences.getString("Tokens", null);
    }

    // Hiding floating button on scroll
    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    switch (newState) {
                        case RecyclerView.SCROLL_STATE_IDLE:
                            mAddStuffFloating.setVisibility(View.VISIBLE);
                            break;
                        default:
                            mAddStuffFloating.setVisibility(View.GONE);
                            break;
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            };

    //  Opening addNewStuff dialog
    private void openAddStuffDialog() {
        AddStuffDialog dialog = new AddStuffDialog();
        dialog.show(getSupportFragmentManager(), "Test Dialog");
    }

    // Implement the interface (AddStuffDialog.AddStuffListener)
    // must be an activity
    // DONE
    @Override
    public void addNewStuff(Stuff stuff) {
        ApiManager.connectToApi();

        stuff.setHomeId(homeId);

        Call<JsonObject> call = ApiManager.apiUrl.addStuff(stuff);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), LM.translate("added", StuffsActivity.this), Toast.LENGTH_SHORT).show();
                    mStuffs.clear();
                    getStuffsFromApi();
                    mTextBackStuffs.setVisibility(View.GONE);
                    mStuffAdapter.notifyDataSetChanged();
                }
                else {
                    // failed codes
                    // an error happened in server
                    Toast.makeText(getApplicationContext(), LM.translate("failed", getApplicationContext()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // failed request
                Toast.makeText(getApplicationContext(), LM.translate("error", getApplicationContext()) + " " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

/*//    private Stuff mDeletedStuff = null;
    private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.DOWN) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {


//            final int position = viewHolder.getAdapterPosition();
//            mDeletedStuff = Stuff.builder()
//                    .stuff(mStuffs.get(position).getStuff())
//                    .quantity(mStuffs.get(position).getQuantity())
//                    .build();
//
//            mStuffs.remove(position);
//            mStuffAdapter.notifyDataSetChanged();
//            Snackbar.make(mStuffRecycler, LM.translate("deleted", StuffsActivity.this)
//                    + " " + mDeletedStuff.getStuff() + " ", BaseTransientBottomBar.LENGTH_LONG)
//                    .setAction(LM.translate("undo", StuffsActivity.this), v -> {
//                        mStuffs.add(position, mDeletedStuff);
//                        mStuffAdapter.notifyDataSetChanged();
//                    })
//                    .setActionTextColor(Color.WHITE)
//                    .setTextColor(Color.WHITE)
//                    .show();
        }
//        @Override
//        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
//            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.deleteBackground))
//                    .addActionIcon(R.drawable.ic_delete_black_24dp)
//                    .create()
//                    .decorate();
//
//            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
//        }
    };*/

    @Override
    public void addNewMember(User user) {
        ApiManager.connectToApi();
    }

    // Check box stuff clicked
    @Override
    public void onCheckChanged(int position, boolean isChecked, TextView textStuff, Stuff stuff) {
        
        Toast.makeText(getApplicationContext(), position + " Clicked", Toast.LENGTH_LONG).show();
//        if (isChecked) {
//
//
//        }
//
//        else textStuff.setPaintFlags(textStuff.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    private void findViews() {
        mProgressBar = findViewById(R.id.progress_stuffs);

        mStuffRecycler = findViewById(R.id.recycler_stuffs);

        mTextBackStuffs = findViewById(R.id.text_back_stuffs);

        mAddStuffFloating = findViewById(R.id.floating_add_stuff);

        mAdView = findViewById(R.id.adViewStuffs);

        mStuffSwipeRefresh = findViewById(R.id.swipe_refresh_stuff);
    }
}
