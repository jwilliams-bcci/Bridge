package com.burgess.bridge.ekotrope_rimjoistslist;

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

public class Ekotrope_RimJoistsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerRimJoists;

    private String mPlanId;
    private Ekotrope_RimJoistsListViewModel mEkotropeRimJoistsListViewModel;
    private Ekotrope_RimJoistsListAdapter mRimJoistsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_rim_joists_list);
        setSupportActionBar(findViewById(R.id.ekotrope_rim_joists_list_toolbar));

        mEkotropeRimJoistsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_RimJoistsListViewModel.class);

        Intent intent = getIntent();
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerRimJoists = findViewById(R.id.ekotrope_rim_joists_list_recycler);
    }

    private void initializeDisplayContent() {
        mRimJoistsListAdapter = new Ekotrope_RimJoistsListAdapter(new Ekotrope_RimJoistsListAdapter.Ekotrope_RimJoistsDiff());
        mRecyclerRimJoists.setAdapter(mRimJoistsListAdapter);
        mRecyclerRimJoists.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeRimJoistsListViewModel.getRimJoists(mPlanId).observe(this, rimJoists -> {
            mRimJoistsListAdapter.setCurrentList(rimJoists);
        });

    }
}