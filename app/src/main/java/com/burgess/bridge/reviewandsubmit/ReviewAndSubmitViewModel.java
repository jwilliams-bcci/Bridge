package com.burgess.bridge.reviewandsubmit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionRepository;
import data.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class ReviewAndSubmitViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private InspectionRepository mInspectionRepository;

    public ReviewAndSubmitViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public LiveData<List<InspectionDefect_Table>> getAllInspectionDefects(int inspectionId) {
        return mInspectionDefectRepository.getAllInspectionDefects(inspectionId);
    }

    public List<InspectionDefect_Table> getAllInspectionDefectsSync(int inspectionId) {
        return mInspectionDefectRepository.getAllInspectionDefectsSync(inspectionId);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReview(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReview(inspectionId);
    }

    public LiveData<Inspection_Table> getInspection(int inspectionId) {
        return mInspectionRepository.getInspection(inspectionId);
    }

    public void completeInspection(int inspectionId) {
        mInspectionRepository.completeInspection(inspectionId);
    }
}
