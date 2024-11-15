package com.burgess.bridge.ekotrope_lighting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_LightingRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Lighting_Table;

public class Ekotrope_LightingViewModel extends AndroidViewModel {
    private Ekotrope_LightingRepository mEkotropeLightingRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_LightingViewModel(@NonNull Application application) {
        super(application);
        mEkotropeLightingRepository = new Ekotrope_LightingRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Lighting_Table getLighting(String mPlanId) {
        return mEkotropeLightingRepository.getLighting(mPlanId);
    }

    public void updateLighting(Ekotrope_Lighting_Table mLighting) {
        mEkotropeLightingRepository.update(mLighting);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
