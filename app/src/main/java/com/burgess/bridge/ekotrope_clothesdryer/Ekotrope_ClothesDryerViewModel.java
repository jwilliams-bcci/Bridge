package com.burgess.bridge.ekotrope_clothesdryer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_ClothesDryerRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_ClothesDryer_Table;

public class Ekotrope_ClothesDryerViewModel extends AndroidViewModel {
    private Ekotrope_ClothesDryerRepository mEkotropeClothesDryerRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_ClothesDryerViewModel(@NonNull Application application) {
        super(application);
        mEkotropeClothesDryerRepository = new Ekotrope_ClothesDryerRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_ClothesDryer_Table getClothesDryer(String mPlanId) {
        return mEkotropeClothesDryerRepository.getClothesDryer(mPlanId);
    }

    public void updateClothesDryer(Ekotrope_ClothesDryer_Table mClothesDryer) {
        mEkotropeClothesDryerRepository.update(mClothesDryer);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
