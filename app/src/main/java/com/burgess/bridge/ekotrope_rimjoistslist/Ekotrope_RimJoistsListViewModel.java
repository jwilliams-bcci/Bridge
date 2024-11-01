package com.burgess.bridge.ekotrope_rimjoistslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_RimJoistRepository;
import data.Tables.Ekotrope_RimJoist_Table;

public class Ekotrope_RimJoistsListViewModel extends AndroidViewModel {
    private Ekotrope_RimJoistRepository mRimJoistsRepository;

    public Ekotrope_RimJoistsListViewModel(@NonNull Application application) {
        super(application);
        mRimJoistsRepository = new Ekotrope_RimJoistRepository(application);
    }

    public LiveData<List<Ekotrope_RimJoist_Table>> getRimJoists(String plan_id) {
        return mRimJoistsRepository.getRimJoists(plan_id);
    }
}
