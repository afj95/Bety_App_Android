package com.myprojects.bety2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.myprojects.bety2.R;
import com.myprojects.bety2.classes.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private List<User> mUsers;
    private Context context;

    public MemberAdapter(List<User> mUsers, Context context){
        this.mUsers = mUsers;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_member_template, parent, false);

        return new ViewHolder(view);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull MemberAdapter.ViewHolder holder, int position) {
        User user = mUsers.get(position);

        holder.mUserName.setText(user.getFirstName() + " " + user.getLastName());
        holder.mUserEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView mUserName, mUserEmail;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            mUserName = itemView.findViewById(R.id.text_member_name_recycler_template);
            mUserEmail = itemView.findViewById(R.id.text_member_email_recycler_template);
        }
    }
}
