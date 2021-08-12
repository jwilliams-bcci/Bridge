package com.burgess.bridge.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.burgess.bridge.R;
import com.burgess.bridge.routesheet.RouteSheetViewModel;

public abstract class BaseActivity extends AppCompatActivity {
    public BaseViewModel mBaseViewModel;
    public TextView mIndividualRemainingInspections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        setSupportActionBar(findViewById(R.id.base_toolbar));

        mBaseViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(BaseViewModel.class);
        mIndividualRemainingInspections = findViewById(R.id.toolbar_individual_inspections_remaining);
    }

    @Override
    public void setContentView(int layoutResId) {
        ConstraintLayout constraintLayout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout frameLayout = constraintLayout.findViewById(R.id.base_frame_layout);

        getLayoutInflater().inflate(layoutResId, frameLayout, true);
        super.setContentView(R.layout.activity_base);
    }

    public void updateInspectionsRemaining() {
        mIndividualRemainingInspections.setText("23");
    }

    protected abstract int getLayoutResourceId();
}