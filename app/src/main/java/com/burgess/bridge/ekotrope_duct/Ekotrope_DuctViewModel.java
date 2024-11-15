package com.burgess.bridge.ekotrope_duct;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_DuctRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Duct_Table;

public class Ekotrope_DuctViewModel extends AndroidViewModel {
    private Ekotrope_DuctRepository mEkotropeDuctRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_DuctViewModel(@NonNull Application application) {
        super(application);
        mEkotropeDuctRepository = new Ekotrope_DuctRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Duct_Table getDuct(String mPlanId, int index, int dsId) {
        return mEkotropeDuctRepository.getDuct(mPlanId, index, dsId);
    }

    public void updateDuct(Ekotrope_Duct_Table mDuct) {
        mEkotropeDuctRepository.update(mDuct);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
