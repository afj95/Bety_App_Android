package com.myprojects.bety2.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.myprojects.bety2.R;
import com.myprojects.bety2.classes.Home;
import com.myprojects.bety2.dialogs.HomeInfoDialog;
import com.myprojects.bety2.dialogs.MembersDetailsDialog;

import java.util.List;

public class HomesAdapter extends RecyclerView.Adapter<HomesAdapter.ViewHolder> {

    private List<Home> mHomes;
    private Context context;
    private OnHomeClickListener mListener;

    public HomesAdapter(List<Home> mHomes, Context context, OnHomeClickListener mListener) {
        this.mHomes = mHomes;
        this.context = context;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_home_template, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull HomesAdapter.ViewHolder holder, int position) {
        Home home = mHomes.get(position);

        holder.mHomeNameText.setText(home.getName());
        holder.mNumOfMembers.setText(String.valueOf(home.getMembers()));
        holder.mNumOfStuffs.setText(String.valueOf(home.getStuffs()));

        // getting id of the home to send it to api and get stuffs
        holder.homeId = home.get_id();
    }

    @Override
    public int getItemCount() {
        return mHomes.size();
    }

// =============================================================================================

//  ViewHolder Class
    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mHomeNameText, mNumOfMembers, mNumOfStuffs;
        ImageView mMembers, mStuffs, mInfo;
        OnHomeClickListener listener;
        String homeId;

        ViewHolder(@NonNull View itemView, OnHomeClickListener listener) {
            super(itemView);

            mHomeNameText = itemView.findViewById(R.id.text_home_name);
            mNumOfMembers = itemView.findViewById(R.id.text_num_of_members);
            mMembers      = itemView.findViewById(R.id.image_members);
            mStuffs       = itemView.findViewById(R.id.image_stuffs);
            mInfo         = itemView.findViewById(R.id.image_info);
            mNumOfStuffs  = itemView.findViewById(R.id.text_num_of_stuffs);

            this.listener = listener;

            // Clicks events:

            // On Home click
            itemView.setOnClickListener(v -> listener.onClickHome(getAdapterPosition(), homeId));

            // On members image click
            mMembers.setOnClickListener(v -> {
                // open dialog shows the members of this home
                //Toast.makeText(itemView.getContext(), "Testing members", Toast.LENGTH_LONG).show();
                MembersDetailsDialog detailsDialog = new MembersDetailsDialog((Activity) itemView.getContext(), homeId);
                detailsDialog.show();
            });
            // On stuffs image click
            mStuffs.setOnClickListener(v -> {
                Toast.makeText(itemView.getContext(), "Testing stuffs", Toast.LENGTH_LONG).show();
            });
            // On info image click
            mInfo.setOnClickListener(v -> {
                HomeInfoDialog dialog = new HomeInfoDialog((Activity) itemView.getContext(), homeId);
                dialog.show();
            });
        }
    }

// =============================================================================================

    public interface OnHomeClickListener {
        void onClickHome(int position, String homeId);
    }
}