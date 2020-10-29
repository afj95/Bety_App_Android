package com.myprojects.bety2.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.myprojects.bety2.R;
import com.myprojects.bety2.classes.Stuff;
import java.util.List;

public class StuffAdapter extends RecyclerView.Adapter<StuffAdapter.ViewHolder> {

    private List<Stuff> stuffs;
    private Context context;
    private OnItemCheckedListener mListener;

    public StuffAdapter(List<Stuff> stuffs, Context context, OnItemCheckedListener listener) {
        this.stuffs = stuffs;
        this.context = context;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_stuff_template2, parent, false);

        return new ViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Stuff stuff = stuffs.get(position);

        holder.stuff = stuff;
        holder.mStuffText.setText(stuff.getStuff());
//        holder.mQuantityText.setText(stuff.getQuantity());
    }

    @Override
    public int getItemCount() {
        return stuffs.size();

    }

// =============================================================================================

    static class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        TextView mStuffText;//, mQuantityText;
        CheckBox mStuffCheckBox;
        OnItemCheckedListener listener;
        Stuff stuff;

        ViewHolder(@NonNull View itemView, OnItemCheckedListener listener) {
            super(itemView);

            mStuffText = itemView.findViewById(R.id.text_stuff);
            mStuffCheckBox = itemView.findViewById(R.id.check_box_stuff);
//            mQuantityText = itemView.findViewById(R.id.text_quantity);

            this.listener = listener;

            mStuffCheckBox.setOnCheckedChangeListener(this);
        }

        @SuppressLint("ResourceAsColor")
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            listener.onCheckChanged(getAdapterPosition(), isChecked, mStuffText, stuff);

            if(isChecked) {
                itemView.setBackgroundResource(R.drawable.recycler_stuff_shape_checked);
            } else {
                itemView.setBackgroundResource(R.drawable.recycler_stuff_shape);
            }
        }
    }
// =============================================================================================

    public interface OnItemCheckedListener {
        void onCheckChanged(int position, boolean isChecked, TextView stuffText, Stuff stuff);
    }

}
