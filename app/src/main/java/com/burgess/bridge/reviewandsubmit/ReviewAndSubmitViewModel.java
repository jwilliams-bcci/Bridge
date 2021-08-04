package com.burgess.bridge.reviewandsubmit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import data.Repositories.BuilderRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionRepository;
import data.Tables.Builder_Table;
import data.Views.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class ReviewAndSubmitViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private InspectionRepository mInspectionRepository;
    private BuilderRepository mBuilderRepository;

    public ReviewAndSubmitViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mInspectionRepository = new InspectionRepository(application);
        mBuilderRepository = new BuilderRepository(application);
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

    public List<InspectionDefect_Table> getReinspectionRequiredDefects(int inspectionId) {
        return mInspectionDefectRepository.getReinspectionRequiredDefects(inspectionId);
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

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }

    public void completeInspection(Date endTime, int inspectionId) {
        mInspectionRepository.completeInspection(endTime, inspectionId);
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

    public void deleteInspectionDefect(int inspectionDefectId) {
        mInspectionDefectRepository.deleteInspectionDefect(inspectionDefectId);
    }

    public Builder_Table getBuilder(int builderId) {
        return mBuilderRepository.getBuilder(builderId);
    }
}
