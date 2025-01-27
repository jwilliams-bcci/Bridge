package com.burgess.bridge.inspectiondetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.time.OffsetDateTime;

import data.Repositories.InspectionRepository;
import data.Tables.Inspection_Table;

public class InspectionDetailsViewModel extends AndroidViewModel {
    private InspectionRepository mRepository;

    public InspectionDetailsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InspectionRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mRepository.getInspectionSync(inspectionId);
    }

    public void startInspection(OffsetDateTime startTime, int inspectionId) {
        mRepository.startInspection(startTime, inspectionId);
    }

    public void updateJotformAccessed(int inspectionId) {
        mRepository.updateJotformAccessed(inspectionId);
    }
}
