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
import com.myprojects.bety2.classes.LM;
import com.myprojects.bety2.classes.User;

public class AddMemberDialog extends AppCompatDialogFragment {

    private EditText mEmail;

    private AddMemberListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // Creating Specific dialog...
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_member, null);
        
        mEmail = view.findViewById(R.id.edit_email_dialog);

        builder.setView(view)
                .setTitle(LM.translate("add_member", getActivity()))
                .setPositiveButton(LM.translate("add", getActivity()), (dialog, which) -> {
                    if(!mEmail.getText().toString().isEmpty()) {
                        User user = new User();
                        user.setEmail(mEmail.getText().toString());
                        listener.addNewMember(user);
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
            listener = (AddMemberListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "Must implement AddUserListener");
        }
    }

// =============================================================================================

    public interface AddMemberListener {

        void addNewMember(User user);
    }
}