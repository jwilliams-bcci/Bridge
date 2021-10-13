package com.burgess.bridge.assigntrainee;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import data.Repositories.InspectionRepository;
import data.Repositories.InspectorRepository;
import data.Tables.Inspection_Table;
import data.Tables.Inspector_Table;

public class AssignTraineeViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectorRepository mInspectorRepository;

    public AssignTraineeViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectorRepository = new InspectorRepository(application);
    }

    public List<Inspector_Table> getInspectorsByDivision(int divisionId) {
        return mInspectorRepository.getInspectorsByDivision(divisionId);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }

    public void assignTrainee(int traineeId, int inspectionId) {
        mInspectionRepository.assignTrainee(traineeId, inspectionId);
    }
}
