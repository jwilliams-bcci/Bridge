package com.burgess.bridge.ekotrope_abovegradewallslist;

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

public class Ekotrope_AboveGradeWallsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerAboveGradeWalls;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_AboveGradeWallsListViewModel mEkotropeAboveGradeWallsListViewModel;
    private Ekotrope_AboveGradeWallsListAdapter mAboveGradeWallsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_above_grade_walls_list);
        setSupportActionBar(findViewById(R.id.ekotrope_above_grade_walls_list_toolbar));

        mEkotropeAboveGradeWallsListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_AboveGradeWallsListViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerAboveGradeWalls = findViewById(R.id.ekotrope_above_grade_walls_recycler);
    }

    private void initializeDisplayContent() {
        mAboveGradeWallsListAdapter = new Ekotrope_AboveGradeWallsListAdapter(new Ekotrope_AboveGradeWallsListAdapter.Ekotrope_AboveGradeWallsDiff());
        mRecyclerAboveGradeWalls.setAdapter(mAboveGradeWallsListAdapter);
        mAboveGradeWallsListAdapter.setInspectionId(mInspectionId);
        mAboveGradeWallsListAdapter.setEkotropeProjectId(mProjectId);
        mRecyclerAboveGradeWalls.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeAboveGradeWallsListViewModel.getAboveGradeWalls(mPlanId).observe(this, aboveGradeWalls -> {
            mAboveGradeWallsListAdapter.setCurrentList(aboveGradeWalls);
        });
    }
}