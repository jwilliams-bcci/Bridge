package com.burgess.bridge.ekotrope_refrigerator;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_RefrigeratorRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Refrigerator_Table;

public class Ekotrope_RefrigeratorViewModel extends AndroidViewModel {
    private Ekotrope_RefrigeratorRepository mEkotropeRefrigeratorRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_RefrigeratorViewModel(@NonNull Application application) {
        super(application);
        mEkotropeRefrigeratorRepository = new Ekotrope_RefrigeratorRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Refrigerator_Table getRefrigerator(String mPlanId) {
        return mEkotropeRefrigeratorRepository.getRefrigerator(mPlanId);
    }

    public void updateRefrigerator(Ekotrope_Refrigerator_Table mRefrigerator) {
        mEkotropeRefrigeratorRepository.update(mRefrigerator);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}