package com.myprojects.bety2.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.myprojects.bety2.R;

public class MembersDetailsDialog extends Dialog implements View.OnClickListener {

    public Activity activity;
    public Button yes, no;

    public MembersDetailsDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_LEFT_ICON);
        setContentView(R.layout.dialog_members_details);


//        yes = (Button) findViewById(R.id.btn_yes);
//        no = (Button) findViewById(R.id.btn_no);
//        yes.setOnClickListener(this);
//        no.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}