package com.burgess.bridge.ekotrope_fragment_inspectiontypes;

import static com.burgess.bridge.Constants.COMPONENT_DISTRIBUTION_SYSTEM;
import static com.burgess.bridge.Constants.COMPONENT_MECHANICAL_EQUIPMENT;
import static com.burgess.bridge.Constants.COMPONENT_MECHANICAL_VENTILATION;
import static com.burgess.bridge.Constants.COMPONENT_TYPE;
import static com.burgess.bridge.Constants.EKOTROPE_PLAN_ID;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.ekotrope_rimjoists.Ekotrope_RimJoistsActivity.PLAN_ID;

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
import com.burgess.bridge.ekotrope_ceilingslist.Ekotrope_CeilingsListActivity;
import com.burgess.bridge.ekotrope_clothesdryer.Ekotrope_ClothesDryerActivity;
import com.burgess.bridge.ekotrope_clotheswasher.Ekotrope_ClothesWasherActivity;
import com.burgess.bridge.ekotrope_componentlist.Ekotrope_ComponentListActivity;
import com.burgess.bridge.ekotrope_dishwasher.Ekotrope_DishwasherActivity;
import com.burgess.bridge.ekotrope_infiltration.Ekotrope_InfiltrationActivity;
import com.burgess.bridge.ekotrope_lighting.Ekotrope_LightingActivity;
import com.burgess.bridge.ekotrope_rangeoven.Ekotrope_RangeOvenActivity;
import com.burgess.bridge.ekotrope_refrigerator.Ekotrope_RefrigeratorActivity;

import data.Tables.Inspection_Table;

public class Ekotrope_Final extends Fragment {
    private int mInspectionId;
    private Inspection_Table mInspection;
    private Ekotrope_FinalViewModel mEkotropeFinalViewModel;

    private Button mButtonCeilings;
    private Button mButtonMechanicalEquipment;
    private Button mButtonDistributionSystems;
    private Button mButtonMechanicalVentilation;
    private Button mButtonLighting;
    private Button mButtonRefrigerator;
    private Button mButtonDishwasher;
    private Button mButtonClothesDryer;
    private Button mButtonClothesWasher;
    private Button mButtonRangeOven;
    private Button mButtonInfiltration;

    public Ekotrope_Final() {
        super(R.layout.fragment_ekotrope_final);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInspectionId = getArguments().getInt(INSPECTION_ID);
        return inflater.inflate(R.layout.fragment_ekotrope_final, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEkotropeFinalViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getActivity().getApplication())).get(Ekotrope_FinalViewModel.class);
        mInspection = mEkotropeFinalViewModel.getInspectionSync(mInspectionId);

        initializeViews();
        initializeButtonListeners();
    }

    private void initializeViews() {
        mButtonCeilings = getView().findViewById(R.id.fragment_ekotrope_final_button_ceilings);
        mButtonMechanicalEquipment = getView().findViewById(R.id.fragment_ekotrope_final_button_mechanical_equipment);
        mButtonDistributionSystems = getView().findViewById(R.id.fragment_ekotrope_final_button_distribution_systems);
        mButtonMechanicalVentilation = getView().findViewById(R.id.fragment_ekotrope_final_button_mechanical_ventilation);
        mButtonLighting = getView().findViewById(R.id.fragment_ekotrope_final_button_lighting);
        mButtonRefrigerator = getView().findViewById(R.id.fragment_ekotrope_final_button_refrigerator);
        mButtonDishwasher = getView().findViewById(R.id.fragment_ekotrope_final_button_dishwasher);
        mButtonClothesDryer = getView().findViewById(R.id.fragment_ekotrope_final_button_clothes_dryer);
        mButtonClothesWasher = getView().findViewById(R.id.fragment_ekotrope_final_button_clothes_washer);
        mButtonRangeOven = getView().findViewById(R.id.fragment_ekotrope_final_button_range_oven);
        mButtonInfiltration = getView().findViewById(R.id.fragment_ekotrope_final_button_infiltration);
    }

    private void initializeButtonListeners() {
        mButtonCeilings.setOnClickListener(view -> {
            Intent ceilingsIntent = new Intent(getActivity(), Ekotrope_CeilingsListActivity.class);
            ceilingsIntent.putExtra(INSPECTION_ID, mInspectionId);
            ceilingsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            ceilingsIntent.putExtra(PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(ceilingsIntent);
        });
        mButtonMechanicalEquipment.setOnClickListener(view -> {
            Intent mechanicalEquipmentIntent = new Intent(getActivity(), Ekotrope_ComponentListActivity.class);
            mechanicalEquipmentIntent.putExtra(INSPECTION_ID, mInspectionId);
            mechanicalEquipmentIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            mechanicalEquipmentIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            mechanicalEquipmentIntent.putExtra(COMPONENT_TYPE, COMPONENT_MECHANICAL_EQUIPMENT);
            startActivity(mechanicalEquipmentIntent);
        });
        mButtonDistributionSystems.setOnClickListener(view -> {
            Intent distributionSystemsIntent = new Intent(getActivity(), Ekotrope_ComponentListActivity.class);
            distributionSystemsIntent.putExtra(INSPECTION_ID, mInspectionId);
            distributionSystemsIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            distributionSystemsIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            distributionSystemsIntent.putExtra(COMPONENT_TYPE, COMPONENT_DISTRIBUTION_SYSTEM);
            startActivity(distributionSystemsIntent);
        });
        mButtonMechanicalVentilation.setOnClickListener(view -> {
            Intent mechanicalVentilationIntent = new Intent(getActivity(), Ekotrope_ComponentListActivity.class);
            mechanicalVentilationIntent.putExtra(INSPECTION_ID, mInspectionId);
            mechanicalVentilationIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            mechanicalVentilationIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            mechanicalVentilationIntent.putExtra(COMPONENT_TYPE, COMPONENT_MECHANICAL_VENTILATION);
            startActivity(mechanicalVentilationIntent);
        });
        mButtonLighting.setOnClickListener(view -> {
            Intent lightingIntent = new Intent(getActivity(), Ekotrope_LightingActivity.class);
            lightingIntent.putExtra(INSPECTION_ID, mInspectionId);
            lightingIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            lightingIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(lightingIntent);
        });
        mButtonRefrigerator.setOnClickListener(view -> {
            Intent refrigeratorIntent = new Intent(getActivity(), Ekotrope_RefrigeratorActivity.class);
            refrigeratorIntent.putExtra(INSPECTION_ID, mInspectionId);
            refrigeratorIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            refrigeratorIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(refrigeratorIntent);
        });
        mButtonDishwasher.setOnClickListener(view -> {
            Intent dishwasherIntent = new Intent(getActivity(), Ekotrope_DishwasherActivity.class);
            dishwasherIntent.putExtra(INSPECTION_ID, mInspectionId);
            dishwasherIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            dishwasherIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(dishwasherIntent);
        });
        mButtonClothesDryer.setOnClickListener(view -> {
            Intent clothesDryerIntent = new Intent(getActivity(), Ekotrope_ClothesDryerActivity.class);
            clothesDryerIntent.putExtra(INSPECTION_ID, mInspectionId);
            clothesDryerIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            clothesDryerIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(clothesDryerIntent);
        });
        mButtonClothesWasher.setOnClickListener(view -> {
            Intent clothesWasherIntent = new Intent(getActivity(), Ekotrope_ClothesWasherActivity.class);
            clothesWasherIntent.putExtra(INSPECTION_ID, mInspectionId);
            clothesWasherIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            clothesWasherIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(clothesWasherIntent);
        });
        mButtonRangeOven.setOnClickListener(view -> {
            Intent rangeOvenIntent = new Intent(getActivity(), Ekotrope_RangeOvenActivity.class);
            rangeOvenIntent.putExtra(INSPECTION_ID, mInspectionId);
            rangeOvenIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            rangeOvenIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(rangeOvenIntent);
        });
        mButtonInfiltration.setOnClickListener(view -> {
            Intent infiltrationIntent = new Intent(getActivity(), Ekotrope_InfiltrationActivity.class);
            infiltrationIntent.putExtra(INSPECTION_ID, mInspectionId);
            infiltrationIntent.putExtra(EKOTROPE_PROJECT_ID, mInspection.ekotrope_project_id);
            infiltrationIntent.putExtra(EKOTROPE_PLAN_ID, mInspection.ekotrope_plan_id);
            startActivity(infiltrationIntent);
        });
    }
}
