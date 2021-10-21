package com.burgess.bridge.editresolution;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionRepository;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class EditResolutionViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectionDefectRepository mInspectionDefectRepository;

    public EditResolutionViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }

    public long addInspectionDefect(InspectionDefect_Table inspectionDefect) {
        return mInspectionDefectRepository.insert(inspectionDefect);
    }

    public InspectionDefect_Table getInspectionDefect(int inspectionDefectId) {
        return mInspectionDefectRepository.getInspectionDefect(inspectionDefectId);
    }
}
