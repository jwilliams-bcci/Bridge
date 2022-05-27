package com.burgess.bridge.reviewandsubmit;

import android.app.DialogFragment;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeResolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeResolutionFragment extends DialogFragment implements OnButtonClickListener {
    private static ChangeResolutionFragment mFragment;
    private View mView;

    public ChangeResolutionFragment() {
        // Required empty public constructor
    }
    public static ChangeResolutionFragment newInstance() {
        mFragment = new ChangeResolutionFragment();
        return  mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_resolution, container, false);
    }

    @Override
    public void onButtonClick(String buttonName) {

    }
}