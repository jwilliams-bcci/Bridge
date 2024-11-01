package com.burgess.bridge.ekotrope_doors;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_DoorRepository;
import data.Tables.Ekotrope_Door_Table;

public class Ekotrope_DoorsViewModel extends AndroidViewModel {
    private Ekotrope_DoorRepository mEkotropeDoorsRepository;

    public Ekotrope_DoorsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeDoorsRepository = new Ekotrope_DoorRepository(application);
    }

    public Ekotrope_Door_Table getDoor(String mPlanId, int mDoorIndex) {
        return mEkotropeDoorsRepository.getDoor(mPlanId, mDoorIndex);
    }

    public void updateDoor(Ekotrope_Door_Table mDoor) {
        mEkotropeDoorsRepository.update(mDoor);
    }
}
