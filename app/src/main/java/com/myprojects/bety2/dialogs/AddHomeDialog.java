package com.myprojects.bety2.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.myprojects.bety2.R;
import com.myprojects.bety2.classes.Home;
import com.myprojects.bety2.classes.LM;

public class AddHomeDialog extends AppCompatDialogFragment {

    private EditText mHomeName;
    private AddHomeListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Creating Specific dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_home, null);
        
        mHomeName = view.findViewById(R.id.edit_home_dialog);

        builder.setView(view)
                .setTitle(LM.translate("add_home", getActivity()))
                .setPositiveButton(LM.translate("add", getActivity()), (dialog, which) -> {
                    if(!mHomeName.getText().toString().isEmpty()) {
                        Home home = Home.builder()
                                .name(mHomeName.getText().toString())
                                // ToDo: get the email from currentUser
//                                .adminEmail(LoginActivity.currentUser.getEmail())
                                .build();
                        listener.addNewHome(home);
                    }
                })
                .setNegativeButton(LM.translate("cancel", getActivity()), (dialog, which) -> {});

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (AddHomeListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement AddHomeListener");
        }
    }

// =============================================================================================

    public interface AddHomeListener {
        void addNewHome(Home home);
    }
}