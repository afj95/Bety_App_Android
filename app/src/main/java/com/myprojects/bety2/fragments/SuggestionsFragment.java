package com.myprojects.bety2.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.myprojects.bety2.R;

public class SuggestionsFragment extends Fragment {

    private TextView mSuggestionsText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_suggestions, container, false);

        mSuggestionsText = view.findViewById(R.id.text_suggestions);

        mSuggestionsText.setText("Suggestions");

        return view;
    }
}