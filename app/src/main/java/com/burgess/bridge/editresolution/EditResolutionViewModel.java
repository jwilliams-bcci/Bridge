package com.burgess.bridge.editresolution;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public long addInspectionDefect(InspectionDefect_Table inspectionDefect) throws ExecutionException, InterruptedException {
        return mInspectionDefectRepository.insert(inspectionDefect);
    }

    public InspectionDefect_Table getInspectionDefect(int inspectionDefectId) {
        return mInspectionDefectRepository.getInspectionDefect(inspectionDefectId);
    }

    public void deleteInspectionDefects(int inspection_id) {
        mInspectionDefectRepository.delete(inspection_id);
    }

    public void deleteInspection(int id) {
        mInspectionRepository.delete(id);
    }
}
