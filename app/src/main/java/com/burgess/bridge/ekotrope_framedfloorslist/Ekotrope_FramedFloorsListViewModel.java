package com.burgess.bridge.ekotrope_framedfloorslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Tables.Ekotrope_FramedFloor_Table;

public class Ekotrope_FramedFloorsListViewModel extends AndroidViewModel {
    private Ekotrope_FramedFloorRepository mFramedFloorsRepository;

    public Ekotrope_FramedFloorsListViewModel(@NonNull Application application) {
        super(application);
        mFramedFloorsRepository = new Ekotrope_FramedFloorRepository(application);
    }

    public LiveData<List<Ekotrope_FramedFloor_Table>> getFramedFloors(String plan_id) {
        return mFramedFloorsRepository.getFramedFloors(plan_id);
    }
}
