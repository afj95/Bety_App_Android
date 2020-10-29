package com.myprojects.bety2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myprojects.bety2.R;
import com.myprojects.bety2.classes.Home;
import com.myprojects.bety2.classes.User;

import java.util.List;

public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.ViewHolder> {

    private List<User> mMembers;

    public MembersAdapter(List<Home> mHomes, Context context) {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_members_template, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MembersAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

// =============================================================================================

//  ViewHolder Class
    static class ViewHolder extends RecyclerView.ViewHolder {



        ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
    }

// =============================================================================================
}