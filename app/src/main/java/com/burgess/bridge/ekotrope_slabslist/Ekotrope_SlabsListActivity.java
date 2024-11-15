package com.burgess.bridge.ekotrope_slabslist;

import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;
import static com.burgess.bridge.ekotrope_framedfloors.Ekotrope_FramedFloorsActivity.PLAN_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class Ekotrope_SlabsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerSlabs;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_SlabsListViewModel mEkotropeSlabsListViewModel;
    private Ekotrope_SlabsListAdapter mSlabsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_slabs_list);
        setSupportActionBar(findViewById(R.id.ekotrope_slabs_list_toolbar));

        mEkotropeSlabsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_SlabsListViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerSlabs = findViewById(R.id.ekotrope_slabs_list_recycler);
    }

    private void initializeDisplayContent() {
        mSlabsListAdapter = new Ekotrope_SlabsListAdapter(new Ekotrope_SlabsListAdapter.Ekotrope_SlabsDiff());
        mRecyclerSlabs.setAdapter(mSlabsListAdapter);
        mSlabsListAdapter.setInspectionId(mInspectionId);
        mSlabsListAdapter.setEkotropeProjectId(mProjectId);
        mRecyclerSlabs.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeSlabsListViewModel.getSlabs(mPlanId).observe(this, slabs -> {
            mSlabsListAdapter.setCurrentList(slabs);
        });


    }
}