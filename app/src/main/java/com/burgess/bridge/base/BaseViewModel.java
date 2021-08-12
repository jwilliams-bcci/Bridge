package com.burgess.bridge.base;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.InspectionRepository;

public class BaseViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public int getIndividualRemainingInspections() {
        return mInspectionRepository.getIndividualRemainingInspections();
    }
}
