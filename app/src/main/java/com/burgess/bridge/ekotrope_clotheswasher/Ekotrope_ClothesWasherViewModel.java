package com.burgess.bridge.ekotrope_clotheswasher;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_ClothesWasherRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_ClothesWasher_Table;

public class Ekotrope_ClothesWasherViewModel extends AndroidViewModel {
    private Ekotrope_ClothesWasherRepository mEkotropeClothesWasherRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_ClothesWasherViewModel(@NonNull Application application) {
        super(application);
        mEkotropeClothesWasherRepository = new Ekotrope_ClothesWasherRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_ClothesWasher_Table getClothesWasher(String mPlanId) {
        return mEkotropeClothesWasherRepository.getClothesWasher(mPlanId);
    }

    public void updateClothesWasher(Ekotrope_ClothesWasher_Table mClothesWasher) {
        mEkotropeClothesWasherRepository.update(mClothesWasher);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
