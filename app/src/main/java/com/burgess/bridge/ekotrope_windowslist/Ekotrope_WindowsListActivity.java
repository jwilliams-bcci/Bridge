package com.burgess.bridge.ekotrope_windowslist;

import static com.burgess.bridge.Constants.EKOTROPE_PROJECT_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;
import static com.burgess.bridge.ekotrope_windows.Ekotrope_WindowsActivity.PLAN_ID;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;

public class Ekotrope_WindowsListActivity extends AppCompatActivity {
    private RecyclerView mRecyclerWindows;

    private int mInspectionId;
    private String mProjectId;
    private String mPlanId;
    private Ekotrope_WindowsListViewModel mEkotropeWindowsListViewModel;
    private Ekotrope_WindowsListAdapter mWindowsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_windows_list);
        setSupportActionBar(findViewById(R.id.ekotrope_windows_list_toolbar));

        mEkotropeWindowsListViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_WindowsListViewModel.class);

        Intent intent = getIntent();
        mInspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);
        mProjectId = intent.getStringExtra(EKOTROPE_PROJECT_ID);
        mPlanId = intent.getStringExtra(PLAN_ID);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        mRecyclerWindows = findViewById(R.id.ekotrope_windows_list_recycler);
    }

    private void initializeDisplayContent() {
        mWindowsListAdapter = new Ekotrope_WindowsListAdapter(new Ekotrope_WindowsListAdapter.Ekotrope_WindowsDiff());
        mRecyclerWindows.setAdapter(mWindowsListAdapter);
        mWindowsListAdapter.setInspectionId(mInspectionId);
        mWindowsListAdapter.setEkotropeProjectId(mProjectId);
        mRecyclerWindows.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeWindowsListViewModel.getWindows(mPlanId).observe(this, windows -> {
            mWindowsListAdapter.setCurrentList(windows);
        });
    }
}