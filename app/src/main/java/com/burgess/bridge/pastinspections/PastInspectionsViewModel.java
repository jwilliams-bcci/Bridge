package com.burgess.bridge.pastinspections;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.InspectionRepository;
import data.Repositories.PastInspectionRepository;
import data.Tables.Inspection_Table;
import data.Tables.PastInspection_Table;

public class PastInspectionsViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private PastInspectionRepository mPastInspectionRepository;

    public PastInspectionsViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mPastInspectionRepository = new PastInspectionRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }

    public LiveData<List<PastInspection_Table>> getPastInspections(int locationId) {
        return mPastInspectionRepository.getPastInspections(locationId);
    }
}
