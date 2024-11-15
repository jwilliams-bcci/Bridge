package com.burgess.bridge.ekotrope_dishwasher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_DishwasherRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Dishwasher_Table;

public class Ekotrope_DishwasherViewModel extends AndroidViewModel {
    private Ekotrope_DishwasherRepository mEkotropeDishwasherRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_DishwasherViewModel(@NonNull Application application) {
        super(application);
        mEkotropeDishwasherRepository = new Ekotrope_DishwasherRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Dishwasher_Table getDishwasher(String mPlanId) {
        return mEkotropeDishwasherRepository.getDishwasher(mPlanId);
    }

    public void updateDishwasher(Ekotrope_Dishwasher_Table mDishwasher) {
        mEkotropeDishwasherRepository.update(mDishwasher);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
