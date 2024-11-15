package com.burgess.bridge.fragment_ekotrope_inspectiontypes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.InspectionRepository;
import data.Tables.Inspection_Table;

public class Ekotrope_RoughViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;

    public Ekotrope_RoughViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }
}
