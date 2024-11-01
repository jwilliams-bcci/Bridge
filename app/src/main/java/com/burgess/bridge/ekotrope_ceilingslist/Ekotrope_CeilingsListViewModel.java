package com.burgess.bridge.ekotrope_ceilingslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_CeilingRepository;
import data.Tables.Ekotrope_Ceiling_Table;

public class Ekotrope_CeilingsListViewModel extends AndroidViewModel {
    private Ekotrope_CeilingRepository mCeilingsRepository;

    public Ekotrope_CeilingsListViewModel(@NonNull Application application) {
        super(application);
        mCeilingsRepository = new Ekotrope_CeilingRepository(application);
    }

    public LiveData<List<Ekotrope_Ceiling_Table>> getCeilings(String plan_id) {
        return mCeilingsRepository.getCeilings(plan_id);
    }
}