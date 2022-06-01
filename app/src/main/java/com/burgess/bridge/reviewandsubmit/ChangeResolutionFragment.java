package com.burgess.bridge.reviewandsubmit;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;

import java.util.List;

import data.Enums.IncompleteReason;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangeResolutionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangeResolutionFragment extends DialogFragment implements OnButtonClickListener {
    private static ChangeResolutionFragment mFragment;
    private View mView;
    private List<IncompleteReason> resolutionList;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_resolution, container, false);

        RecyclerView recyclerResolutions = (RecyclerView) mView.findViewById(R.id.change_resolution_recycler_resolutions);
        recyclerResolutions.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        recyclerResolutions.setAdapter(new ChangeResolutionRecyclerAdapter(getActivity(), resolutionList, this));

        Button saveResolution = mView.findViewById(R.id.change_resolution_save);
        saveResolution.setOnClickListener(v -> {
            mFragment.dismiss();
        });

        return mView;
    }

    @Override
    public void onButtonClick(String buttonName) {

    }

    public void setResolutionList(List<IncompleteReason> resolutionList) {
        this.resolutionList = resolutionList;
    }
}