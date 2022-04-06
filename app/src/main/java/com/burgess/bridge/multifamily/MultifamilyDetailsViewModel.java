package com.burgess.bridge.multifamily;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import data.Repositories.InspectionRepository;
import data.Repositories.MultifamilyDetailsRepository;
import data.Tables.Inspection_Table;
import data.Tables.MultifamilyDetails_Table;

public class MultifamilyDetailsViewModel extends AndroidViewModel {
    private MultifamilyDetailsRepository mMultifamilyDetailsRepository;
    private InspectionRepository mInspectionRepository;

    public MultifamilyDetailsViewModel(@NonNull Application application) {
        super(application);
        mMultifamilyDetailsRepository = new MultifamilyDetailsRepository(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public MultifamilyDetails_Table getMultifamilyDetails(int inspectionId) {
        return mMultifamilyDetailsRepository.getMultifamilyDetails(inspectionId);
    }

    public void insertMultifamilyDetails(MultifamilyDetails_Table multifamilyDetails) {
        mMultifamilyDetailsRepository.insertMultifamilyDetails(multifamilyDetails);
    }

    public void updateMultifamilyDetails(int id, String builder_personnel, String burgess_personnel, String area_observed, String temperature, String weather_conditions) {
        mMultifamilyDetailsRepository.updateMultifamilyDetails(id, builder_personnel, burgess_personnel, area_observed, temperature, weather_conditions);
    }

    public Inspection_Table getInspection(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }
}
