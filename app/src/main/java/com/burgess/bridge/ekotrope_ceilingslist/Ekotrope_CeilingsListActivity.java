package com.burgess.bridge.ekotrope_ceilingslist;

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

public class Ekotrope_CeilingsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerCeilings;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_CeilingsListViewModel mEkotropeCeilingsListViewModel;
    private Ekotrope_CeilingsListAdapter mCeilingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_ceilings_list);
        setSupportActionBar(findViewById(R.id.ekotrope_ceilings_list_toolbar));

        mEkotropeCeilingsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_CeilingsListViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerCeilings = findViewById(R.id.ekotrope_ceilings_list_recycler);
    }

    private void initializeDisplayContent() {
        mCeilingsListAdapter = new Ekotrope_CeilingsListAdapter(new Ekotrope_CeilingsListAdapter.Ekotrope_CeilingsDiff());
        mRecyclerCeilings.setAdapter(mCeilingsListAdapter);
        mCeilingsListAdapter.setInspectionId(mInspectionId);
        mCeilingsListAdapter.setEkotropeProjectId(mProjectId);
        mRecyclerCeilings.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeCeilingsListViewModel.getCeilings(mPlanId).observe(this, ceilings -> {
            mCeilingsListAdapter.setCurrentList(ceilings);
        });
    }
}