package com.burgess.bridge.ekotrope_doorslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_DoorRepository;
import data.Tables.Ekotrope_Door_Table;

public class Ekotrope_DoorsListViewModel extends AndroidViewModel {
    private Ekotrope_DoorRepository mDoorsRepository;

    public Ekotrope_DoorsListViewModel(@NonNull Application application) {
        super(application);
        mDoorsRepository = new Ekotrope_DoorRepository(application);
    }

    public LiveData<List<Ekotrope_Door_Table>> getDoors(String plan_id) {
        return mDoorsRepository.getDoors(plan_id);
    }
}
