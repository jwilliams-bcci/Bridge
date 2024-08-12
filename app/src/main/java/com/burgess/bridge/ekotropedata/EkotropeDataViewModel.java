package com.burgess.bridge.ekotropedata;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import data.Repositories.EkotropeDataRepository;
import data.Tables.Ekotrope_Data_Table;

public class EkotropeDataViewModel extends AndroidViewModel {
    private EkotropeDataRepository mEkotropeDataRepository;

    public EkotropeDataViewModel(@NonNull Application application) {
        super(application);
        mEkotropeDataRepository = new EkotropeDataRepository(application);
    }
    public LiveData<Ekotrope_Data_Table> getInspection(int inspection_id) {
        return mEkotropeDataRepository.getInspection(inspection_id);
    }

    public Ekotrope_Data_Table getInspectionSync(int inspection_id) {
        return mEkotropeDataRepository.getInspectionSync(inspection_id);
    }

    public void insertInspection(Ekotrope_Data_Table inspection) {
        mEkotropeDataRepository.insert(inspection);
    }
}