package com.burgess.bridge.ekotrope_componentlist;

import static com.burgess.bridge.Constants.COMPONENT_DISTRIBUTION_SYSTEM;
import static com.burgess.bridge.Constants.COMPONENT_DUCT;
import static com.burgess.bridge.Constants.COMPONENT_MECHANICAL_EQUIPMENT;
import static com.burgess.bridge.Constants.COMPONENT_MECHANICAL_VENTILATION;
import static com.burgess.bridge.Constants.COMPONENT_TYPE;
import static com.burgess.bridge.Constants.DUCT_INDEX;
import static com.burgess.bridge.Constants.DUCT_INDEX_NOT_FOUND;
import static com.burgess.bridge.Constants.EKOTROPE_PLAN_ID;
import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

import java.util.List;

public class Ekotrope_ComponentListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerComponents;
    private Toolbar mToolbar;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private int mDsId;
    private String mComponentType;
    private Ekotrope_ComponentListViewModel mEkotropeComponentListViewModel;
    private Ekotrope_ComponentListAdapter mComponentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_component_list);
        setSupportActionBar(findViewById(R.id.ekotrope_component_list_toolbar));

        mEkotropeComponentListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_ComponentListViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(EKOTROPE_PLAN_ID);
        mDsId = intent.getIntExtra(DUCT_INDEX, DUCT_INDEX_NOT_FOUND);
        mComponentType = intent.getStringExtra(COMPONENT_TYPE);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mToolbar = findViewById(R.id.ekotrope_component_list_toolbar);
        mRecyclerComponents = findViewById(R.id.ekotrope_component_list_recycler);
    }

    private void initializeDisplayContent() {
        mToolbar.setTitle(String.format(mComponentType));

        mComponentListAdapter = new Ekotrope_ComponentListAdapter(new Ekotrope_ComponentListAdapter.Ekotrope_ComponentListDiff());
        mRecyclerComponents.setAdapter(mComponentListAdapter);
        mComponentListAdapter.setInspectionId(mInspectionId);
        mComponentListAdapter.setEkotropeProjectId(mProjectId);
        mComponentListAdapter.setEkotropePlanId(mPlanId);
        mComponentListAdapter.setDsId(mDsId);
        mComponentListAdapter.setComponentType(mComponentType);
        mRecyclerComponents.setLayoutManager(new LinearLayoutManager(this));
        switch (mComponentType) {
            case COMPONENT_MECHANICAL_EQUIPMENT:
                mEkotropeComponentListViewModel.getMechanicalEquipments(mPlanId).observe(this, components -> {
                    mComponentListAdapter.setCurrentList(components);
                });
                break;
            case COMPONENT_DISTRIBUTION_SYSTEM:
                mEkotropeComponentListViewModel.getDistributionSystems(mPlanId).observe(this, components -> {
                    mComponentListAdapter.setCurrentList(components);
                });
                break;
            case COMPONENT_DUCT:
                mEkotropeComponentListViewModel.getDucts(mPlanId, mDsId).observe(this, components -> {
                    mComponentListAdapter.setCurrentList(components);
                });
                break;
            case COMPONENT_MECHANICAL_VENTILATION:
                mEkotropeComponentListViewModel.getMechanicalVentilations(mPlanId).observe(this, components -> {
                    mComponentListAdapter.setCurrentList(components);
                });
                break;
        }
    }
}