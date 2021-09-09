package com.burgess.bridge.multifamily;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.MultifamilyDetailsRepository;
import data.Tables.MultifamilyDetails_Table;

public class MultifamilyDetailsViewModel extends AndroidViewModel {
    private MultifamilyDetailsRepository mRepository;

    public MultifamilyDetailsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new MultifamilyDetailsRepository(application);
    }

    public MultifamilyDetails_Table getMultifamilyDetails(int inspectionId) {
        return mRepository.getMultifamilyDetails(inspectionId);
    }

    public void insertMultifamilyDetails(MultifamilyDetails_Table multifamilyDetails) {
        mRepository.insertMultifamilyDetails(multifamilyDetails);
    }
}
