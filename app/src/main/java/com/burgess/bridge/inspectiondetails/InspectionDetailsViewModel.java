package com.burgess.bridge.inspectiondetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.OffsetDateTime;
import java.util.Date;

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

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mRepository.getInspectionSync(inspectionId);
    }

    public void startInspection(OffsetDateTime startTime, int inspectionId) {
        mRepository.startInspection(startTime, inspectionId);
    }
}
