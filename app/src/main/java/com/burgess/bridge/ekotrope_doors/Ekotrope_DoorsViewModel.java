package com.burgess.bridge.ekotrope_doors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_DoorRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Door_Table;

public class Ekotrope_DoorsViewModel extends AndroidViewModel {
    private Ekotrope_DoorRepository mEkotropeDoorsRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_DoorsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeDoorsRepository = new Ekotrope_DoorRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Door_Table getDoor(String mPlanId, int mDoorIndex) {
        return mEkotropeDoorsRepository.getDoor(mPlanId, mDoorIndex);
    }

    public void updateDoor(Ekotrope_Door_Table mDoor) {
        mEkotropeDoorsRepository.update(mDoor);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
