package com.burgess.bridge.reviewandsubmit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionRepository;
import data.Views.ReviewAndSubmit_View;
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

    public InspectionDefect_Table getInspectionDefect(int inspectionDefectId) {
        return mInspectionDefectRepository.getInspectionDefect(inspectionDefectId);
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

    public List<ReviewAndSubmit_View> getInspectionDefectsForReviewSync(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewSync(inspectionId);
    }

    public LiveData<Inspection_Table> getInspection(int inspectionId) {
        return mInspectionRepository.getInspection(inspectionId);
    }

    public void completeInspection(int inspectionId) {
        mInspectionRepository.completeInspection(inspectionId);
    }

    public void uploadInspection(int inspectionId) {
        mInspectionRepository.uploadInspection(inspectionId);
    }

    public void markDefectUploaded(int inspectionDefectId) {
        mInspectionDefectRepository.markDefectUploaded(inspectionDefectId);
    }

    public int remainingToUpload(int inspectionId) {
        return mInspectionDefectRepository.remainingToUpload(inspectionId);
    }
}
