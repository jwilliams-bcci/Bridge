package com.burgess.bridge.ekotrope_doorslist;

import static com.burgess.bridge.ekotrope_doors.Ekotrope_DoorsActivity.PLAN_ID;

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

public class Ekotrope_DoorsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerDoors;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_DoorsListViewModel mEkotropeDoorsListViewModel;
    private Ekotrope_DoorsListAdapter mDoorsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_doors_list);
        setSupportActionBar(findViewById(R.id.ekotrope_doors_list_toolbar));

        mEkotropeDoorsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_DoorsListViewModel.class);

        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerDoors = findViewById(R.id.ekotrope_doors_recycler);
    }

    private void initializeDisplayContent() {
        mDoorsListAdapter = new Ekotrope_DoorsListAdapter(new Ekotrope_DoorsListAdapter.Ekotrope_DoorsDiff());
        mRecyclerDoors.setAdapter(mDoorsListAdapter);
        mDoorsListAdapter.setInspectionId(mInspectionId);
        mDoorsListAdapter.setEkotropeProjectId(mProjectId);
        mRecyclerDoors.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeDoorsListViewModel.getDoors(mPlanId).observe(this, doors -> {
            mDoorsListAdapter.setCurrentList(doors);
        });
    }
}