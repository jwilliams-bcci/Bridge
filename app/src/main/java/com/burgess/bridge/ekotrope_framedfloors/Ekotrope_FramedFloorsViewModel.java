package com.burgess.bridge.ekotrope_framedfloors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Tables.Ekotrope_FramedFloor_Table;

public class Ekotrope_FramedFloorsViewModel extends AndroidViewModel {
    private Ekotrope_FramedFloorRepository mEkotropeFramedFloorsRepository;

    public Ekotrope_FramedFloorsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeFramedFloorsRepository = new Ekotrope_FramedFloorRepository(application);
    }

    public Ekotrope_FramedFloor_Table getFramedFloor(String mPlanId, int mFramedFloorIndex) {
        return mEkotropeFramedFloorsRepository.getFramedFloor(mPlanId, mFramedFloorIndex);
    }

    public void updateFramedFloor(Ekotrope_FramedFloor_Table mFramedFloor) {
        mEkotropeFramedFloorsRepository.update(mFramedFloor);
    }
}