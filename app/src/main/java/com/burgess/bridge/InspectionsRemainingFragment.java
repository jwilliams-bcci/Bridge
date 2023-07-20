package com.burgess.bridge;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import org.w3c.dom.Text;

public class InspectionsRemainingFragment extends Fragment {
    private TextView mTextToolbarInspectionsRemainingIndividual;
    private TextView mTextToolbarInspectionsRemainingTeam;

    public InspectionsRemainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inspections_remaining, container, false);
        mTextToolbarInspectionsRemainingIndividual = view.findViewById(R.id.toolbar_individual_inspections_remaining);
        mTextToolbarInspectionsRemainingTeam = view.findViewById(R.id.toolbar_team_inspections_remaining);

        mTextToolbarInspectionsRemainingIndividual.setText("11");
        mTextToolbarInspectionsRemainingTeam.setText("22");

        // Inflate the layout for this fragment
        return view;
    }
}