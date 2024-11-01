package com.burgess.bridge.ekotrope_windowslist;

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

    private String mPlanId;
    private Ekotrope_WindowsListViewModel mEkotropeWindowsListViewModel;
    private Ekotrope_WindowsListAdapter mWindowsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ekotrope_windows_list);
        setSupportActionBar(findViewById(R.id.ekotrope_windows_list_toolbar));

        mEkotropeWindowsListViewModel = new ViewModelProvider(this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(Ekotrope_WindowsListViewModel.class);

        Intent intent = getIntent();
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
        mRecyclerWindows.setLayoutManager(new LinearLayoutManager(this));
        mEkotropeWindowsListViewModel.getWindows(mPlanId).observe(this, windows -> {
            mWindowsListAdapter.setCurrentList(windows);
        });
    }
}