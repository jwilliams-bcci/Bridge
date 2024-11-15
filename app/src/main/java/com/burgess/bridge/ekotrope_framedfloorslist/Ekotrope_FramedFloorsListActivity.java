package com.burgess.bridge.ekotrope_framedfloorslist;

import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;
import static com.burgess.bridge.ekotrope_framedfloors.Ekotrope_FramedFloorsActivity.PLAN_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class Ekotrope_FramedFloorsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerFramedFloors;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_FramedFloorsListViewModel mEkotropeFramedFloorsListViewModel;
    private Ekotrope_FramedFloorsListAdapter mFramedFloorsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_framed_floors_list);
        setSupportActionBar(findViewById(R.id.ekotrope_framed_floors_list_toolbar));

        mEkotropeFramedFloorsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_FramedFloorsListViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerFramedFloors = findViewById(R.id.ekotrope_framed_floors_list_recycler);
    }

    private void initializeDisplayContent() {
        mFramedFloorsListAdapter = new Ekotrope_FramedFloorsListAdapter(new Ekotrope_FramedFloorsListAdapter.Ekotrope_FramedFloorsDiff());
        mRecyclerFramedFloors.setAdapter(mFramedFloorsListAdapter);
        mFramedFloorsListAdapter.setInspectionId(mInspectionId);
        mFramedFloorsListAdapter.setEkotropeProjectId(mProjectId);
        mRecyclerFramedFloors.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeFramedFloorsListViewModel.getFramedFloors(mPlanId).observe(this, framedFloors -> {
            mFramedFloorsListAdapter.setCurrentList(framedFloors);
        });
    }
}