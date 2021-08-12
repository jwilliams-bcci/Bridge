package com.burgess.bridge.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.TextView;

import com.burgess.bridge.R;
import com.burgess.bridge.routesheet.RouteSheetViewModel;

public class BaseActivity extends AppCompatActivity {
    public BaseViewModel mBaseViewModel;
    public TextView mIndividualRemainingInspections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_inspections_remaining);

        mBaseViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(BaseViewModel.class);
        mIndividualRemainingInspections = findViewById(R.id.toolbar_individual_inspections_remaining);
    }

    public void updateInspectionsRemaining() {
        mIndividualRemainingInspections.setText("23");
    }
}