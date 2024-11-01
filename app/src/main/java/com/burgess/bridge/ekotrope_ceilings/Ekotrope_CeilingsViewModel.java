package com.burgess.bridge.ekotrope_ceilings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_CeilingRepository;
import data.Tables.Ekotrope_Ceiling_Table;

public class Ekotrope_CeilingsViewModel extends AndroidViewModel {
    private Ekotrope_CeilingRepository mEkotropeCeilingsRepository;

    public Ekotrope_CeilingsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeCeilingsRepository = new Ekotrope_CeilingRepository(application);
    }

    public Ekotrope_Ceiling_Table getCeiling(String mPlanId, int mCeilingIndex) {
        return mEkotropeCeilingsRepository.getCeiling(mPlanId, mCeilingIndex);
    }

    public void updateCeiling(Ekotrope_Ceiling_Table mCeiling) {
        mEkotropeCeilingsRepository.update(mCeiling);
    }
}
