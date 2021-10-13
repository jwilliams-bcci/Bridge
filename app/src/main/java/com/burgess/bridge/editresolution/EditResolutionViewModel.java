package com.burgess.bridge.editresolution;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.InspectionRepository;
import data.Tables.Inspection_Table;

public class EditResolutionViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;

    public EditResolutionViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }
}
