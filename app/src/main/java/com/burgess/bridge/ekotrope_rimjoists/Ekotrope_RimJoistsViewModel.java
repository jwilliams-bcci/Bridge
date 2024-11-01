package com.burgess.bridge.ekotrope_rimjoists;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_RimJoistRepository;
import data.Tables.Ekotrope_RimJoist_Table;

public class Ekotrope_RimJoistsViewModel extends AndroidViewModel {
    private Ekotrope_RimJoistRepository mEkotropeRimJoistsRepository;

    public Ekotrope_RimJoistsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeRimJoistsRepository = new Ekotrope_RimJoistRepository(application);
    }

    public Ekotrope_RimJoist_Table getRimJoist(String mPlanId, int mRimJoistIndex) {
        return mEkotropeRimJoistsRepository.getRimJoist(mPlanId, mRimJoistIndex);
    }
}
