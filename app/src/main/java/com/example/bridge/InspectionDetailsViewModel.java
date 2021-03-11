package com.example.bridge;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import data.Repositories.InspectionRepository;
import data.Tables.Inspection_Table;

public class InspectionDetailsViewModel extends AndroidViewModel {
    private InspectionRepository mRepository;

    public InspectionDetailsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InspectionRepository(application);
    }

    public LiveData<Inspection_Table> getInspection(int inspectionId) {
        return mRepository.getInspection(inspectionId);
    }
}
