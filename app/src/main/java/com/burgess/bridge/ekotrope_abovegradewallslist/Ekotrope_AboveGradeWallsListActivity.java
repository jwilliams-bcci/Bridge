package com.burgess.bridge.ekotrope_abovegradewallslist;

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

    private String mPlanId;
    private Ekotrope_AboveGradeWallsListViewModel mEkotropeAboveGradeWallsListViewModel;
    private Ekotrope_AboveGradeWallsListAdapter mAboveGradeWallsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_above_grade_walls_list);
        setSupportActionBar(findViewById(R.id.ekotrope_above_grade_walls_list_toolbar));

        mEkotropeAboveGradeWallsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_AboveGradeWallsListViewModel.class);

        Intent intent = getIntent();
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
        mRecyclerAboveGradeWalls.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeAboveGradeWallsListViewModel.getAboveGradeWalls(mPlanId).observe(this, aboveGradeWalls -> {
            mAboveGradeWallsListAdapter.setCurrentList(aboveGradeWalls);
        });
    }
}