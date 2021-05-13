package com.burgess.bridge.inspect;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.burgess.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNoteFragment extends DialogFragment {
    private static AddNoteFragment mFragment;
    private View mView;
    private Button mSaveButton;
    private Button mCancelButton;

    public AddNoteFragment() {
        // Required empty public constructor
    }

    public static AddNoteFragment newInstance() {
        mFragment = new AddNoteFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_note, container, false);
        mSaveButton = mView.findViewById(R.id.add_note_button_save);
        mCancelButton = mView.findViewById(R.id.add_note_button_cancel);

        mSaveButton.setOnClickListener(v -> {
            mFragment.dismiss();
        });

        mCancelButton.setOnClickListener(v -> {
            mFragment.dismiss();
        });

        return mView;
    }
}