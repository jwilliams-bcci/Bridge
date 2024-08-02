package com.burgess.bridge.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.burgess.bridge.R;
import com.burgess.bridge.routesheet.RouteSheetViewModel;

public class BaseActivity extends AppCompatActivity {
    public BaseViewModel mBaseViewModel;
    public TextView mIndividualRemainingInspections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(findViewById(R.id.base_toolbar));

        mBaseViewModel = new ViewModelProvider((ViewModelStoreOwner) this, (ViewModelProvider.Factory) new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(BaseViewModel.class);
        mIndividualRemainingInspections = findViewById(R.id.toolbar_individual_inspections_remaining);
    }

    @Override
    public void setContentView(int layoutResId) {
        super.setContentView(layoutResId);
        updateInspectionsRemaining();
    }

    public void updateInspectionsRemaining() {
        mIndividualRemainingInspections.setText("23");
    }
}