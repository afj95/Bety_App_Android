package com.myprojects.bety2.dialogs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import com.myprojects.bety2.R;
import com.myprojects.bety2.classes.LM;
import com.myprojects.bety2.classes.Stuff;

public class AddStuffDialog extends AppCompatDialogFragment {

    private EditText mStuff, mQuantity;

    private AddStuffListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Creating Specific dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_stuff, null);
        
        mStuff = view.findViewById(R.id.edit_stuff_dialog);
//        mQuantity = view.findViewById(R.id.edit_quantity_dialog);

        builder.setView(view)
                .setTitle(LM.translate("add_stuff", getActivity()))
                .setPositiveButton(LM.translate("add", getActivity()), (dialog, which) -> {
                    if(!mStuff.getText().toString().isEmpty()) {
                        Stuff stuff = Stuff.builder()
                                .stuff(mStuff.getText().toString())
                                .build();

                        listener.addNewStuff(stuff);
                    }
                })
                .setNegativeButton(LM.translate("cancel", getActivity()), (dialog, which) -> {
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddStuffListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement AddStuffListener");
        }
    }

// =============================================================================================

    public interface AddStuffListener {
        void addNewStuff(Stuff stuff);
    }
}