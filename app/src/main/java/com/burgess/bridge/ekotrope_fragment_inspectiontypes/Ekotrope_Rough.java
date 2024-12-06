package com.burgess.bridge.ekotrope_fragment_inspectiontypes;

import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.ekotrope_framedfloors.Ekotrope_FramedFloorsActivity.PLAN_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.burgess.bridge.R;
import com.burgess.bridge.ekotrope_abovegradewallslist.Ekotrope_AboveGradeWallsListActivity;
import com.burgess.bridge.ekotrope_ceilingslist.Ekotrope_CeilingsListActivity;
import com.burgess.bridge.ekotrope_componentlist.Ekotrope_ComponentListActivity;
import com.burgess.bridge.ekotrope_doorslist.Ekotrope_DoorsListActivity;
import com.burgess.bridge.ekotrope_framedfloorslist.Ekotrope_FramedFloorsListActivity;
import com.burgess.bridge.ekotrope_rimjoistslist.Ekotrope_RimJoistsListActivity;
import com.burgess.bridge.ekotrope_slabslist.Ekotrope_SlabsListActivity;
import com.burgess.bridge.ekotrope_windowslist.Ekotrope_WindowsListActivity;

import data.Tables.Inspection_Table;

public class Ekotrope_Rough extends Fragment {
    private int mInspectionId;
    private Inspection_Table mInspection;
    private Ekotrope_RoughViewModel mEkotropeRoughViewModel;

    private Button mButtonFramedFloors;
    private Button mButtonAboveGradeWalls;
    private Button mButtonWindows;
    private Button mButtonDoors;
    private Button mButtonCeilings;
    private Button mButtonSlabs;
    private Button mButtonRimJoists;
    private Button mButtonDistributionSystems;

    public Ekotrope_Rough() {
        super(R.layout.fragment_ekotrope_rough);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInspectionId = getArguments().getInt(INSPECTION_ID);
        return inflater.inflate(R.layout.fragment_ekotrope_rough, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEkotropeRoughViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(Ekotrope_RoughViewModel.class);
        mInspection = mEkotropeRoughViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
    }

    private void initializeViews() {
        mButtonFramedFloors = getView().findViewById(R.id.fragment_ekotrope_rough_button_framed_floors);
        mButtonAboveGradeWalls = getView().findViewById(R.id.fragment_ekotrope_rough_button_above_grade_walls);
        mButtonWindows = getView().findViewById(R.id.fragment_ekotrope_rough_button_windows);
        mButtonDoors = getView().findViewById(R.id.fragment_ekotrope_rough_button_doors);
        mButtonCeilings = getView().findViewById(R.id.fragment_ekotrope_rough_button_ceilings);
        mButtonSlabs = getView().findViewById(R.id.fragment_ekotrope_rough_button_slabs);
        mButtonRimJoists = getView().findViewById(R.id.fragment_ekotrope_rough_button_rim_joists);
        mButtonDistributionSystems = getView().findViewById(R.id.fragment_ekotrope_rough_button_distribution_systems);
    }

    private void initializeButtonListeners() {
        mButtonFramedFloors.setOnClickListener(view -> {
            Intent framedFloorsListIntent = new Intent(getActivity(), Ekotrope_FramedFloorsListActivity.class);
            framedFloorsListIntent.putExtra(INSPECTION_ID, mInspectionId);
            framedFloorsListIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            framedFloorsListIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(framedFloorsListIntent);
        });

        mButtonAboveGradeWalls.setOnClickListener(view -> {
            Intent aboveGradeWallsIntent = new Intent(getActivity(), Ekotrope_AboveGradeWallsListActivity.class);
            aboveGradeWallsIntent.putExtra(INSPECTION_ID, mInspectionId);
            aboveGradeWallsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            aboveGradeWallsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(aboveGradeWallsIntent);
        });

        mButtonWindows.setOnClickListener(view -> {
            Intent windowsIntent = new Intent(getActivity(), Ekotrope_WindowsListActivity.class);
            windowsIntent.putExtra(INSPECTION_ID, mInspectionId);
            windowsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            windowsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(windowsIntent);
        });

        mButtonDoors.setOnClickListener(view -> {
            Intent doorsIntent = new Intent(getActivity(), Ekotrope_DoorsListActivity.class);
            doorsIntent.putExtra(INSPECTION_ID, mInspectionId);
            doorsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            doorsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(doorsIntent);
        });

        mButtonCeilings.setOnClickListener(view -> {
            Intent ceilingsIntent = new Intent(getActivity(), Ekotrope_CeilingsListActivity.class);
            ceilingsIntent.putExtra(INSPECTION_ID, mInspectionId);
            ceilingsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            ceilingsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(ceilingsIntent);
        });

        mButtonSlabs.setOnClickListener(view -> {
            Intent slabsIntent = new Intent(getActivity(), Ekotrope_SlabsListActivity.class);
            slabsIntent.putExtra(INSPECTION_ID, mInspectionId);
            slabsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            slabsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(slabsIntent);
        });

        mButtonRimJoists.setOnClickListener(view -> {
            Intent rimJoistsIntent = new Intent(getActivity(), Ekotrope_RimJoistsListActivity.class);
            rimJoistsIntent.putExtra(INSPECTION_ID, mInspectionId);
            rimJoistsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            rimJoistsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(rimJoistsIntent);
        });

        mButtonDistributionSystems.setOnClickListener(view -> {
            Intent distributionSystemsIntent = new Intent(getActivity(), Ekotrope_ComponentListActivity.class);
            distributionSystemsIntent.putExtra(INSPECTION_ID, mInspectionId);
            distributionSystemsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.EkotropeProjectID);
            distributionSystemsIntent.putExtra(PLAN_ID, mInspection.EkotropePlanID);
            startActivity(distributionSystemsIntent);
        });
    }
}
