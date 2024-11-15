package com.burgess.bridge.ekotrope_framedfloors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_FramedFloor_Table;

public class Ekotrope_FramedFloorsViewModel extends AndroidViewModel {
    private Ekotrope_FramedFloorRepository mEkotropeFramedFloorsRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_FramedFloorsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeFramedFloorsRepository = new Ekotrope_FramedFloorRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_FramedFloor_Table getFramedFloor(String mPlanId, int mFramedFloorIndex) {
        return mEkotropeFramedFloorsRepository.getFramedFloor(mPlanId, mFramedFloorIndex);
    }

    public void updateFramedFloor(Ekotrope_FramedFloor_Table mFramedFloor) {
        mEkotropeFramedFloorsRepository.update(mFramedFloor);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}