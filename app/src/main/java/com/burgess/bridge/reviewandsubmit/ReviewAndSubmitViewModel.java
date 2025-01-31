package com.burgess.bridge.reviewandsubmit;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.time.OffsetDateTime;
import java.util.List;

import data.Repositories.AttachmentRepository;
import data.Repositories.BuilderRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Repositories.MultifamilyDetailsRepository;
import data.Repositories.SubmitRequestRepository;
import data.Tables.Attachment_Table;
import data.Tables.Builder_Table;
import data.Tables.DefectItem_Table;
import data.Tables.MultifamilyDetails_Table;
import data.Tables.SubmitRequest_Table;
import data.Views.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class ReviewAndSubmitViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private InspectionRepository mInspectionRepository;
    private BuilderRepository mBuilderRepository;
    private MultifamilyDetailsRepository mMultifamilyDetailsRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private AttachmentRepository mAttachmentRepository;
    private final SubmitRequestRepository submitRequestRepository;

    public ReviewAndSubmitViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mInspectionRepository = new InspectionRepository(application);
        mBuilderRepository = new BuilderRepository(application);
        mMultifamilyDetailsRepository = new MultifamilyDetailsRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mAttachmentRepository = new AttachmentRepository(application);
        submitRequestRepository = new SubmitRequestRepository(application);
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

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortAsc(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewDescriptionSortAsc(inspectionId);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortDesc(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewDescriptionSortDesc(inspectionId);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortAsc(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewItemNumberSortAsc(inspectionId);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortDesc(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewItemNumberSortDesc(inspectionId);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDefectIDSortAsc(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewDefectIDSortAsc(inspectionId);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDefectIDSortDesc(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewDefectIDSortDesc(inspectionId);
    }

    public List<ReviewAndSubmit_View> getInspectionDefectsForReviewSync(int inspectionId) {
        return mInspectionDefectRepository.getInspectionDefectsForReviewSync(inspectionId);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionRepository.getInspectionSync(inspectionId);
    }

    public void completeInspection(OffsetDateTime endTime, int inspectionId) {
        mInspectionRepository.completeInspection(endTime, inspectionId);
    }

    public void uploadInspection(int inspectionId) {
        mInspectionRepository.uploadInspection(inspectionId);
    }

    public void markInspectionFailed(int inspectionId) {
        mInspectionRepository.markInspectionFailed(inspectionId);
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

    public MultifamilyDetails_Table getMultifamilyDetails(int inspectionId) {
        return mMultifamilyDetailsRepository.getMultifamilyDetails(inspectionId);
    }

    public DefectItem_Table getDefectItem(int defectItemId) {
        return mDefectItemRepository.getDefectItemSync(defectItemId);
    }

    public List<Attachment_Table> getAllInspectionAttachments(int inspectionId) {
        return mAttachmentRepository.getInspectionAttachments(inspectionId);
    }

    public List<Attachment_Table> getAttachmentsToUpload(int inspectionId) {
        return mAttachmentRepository.getAttachmentsToUpload(inspectionId);
    }

    public void updateIsUploaded(int attachmentId) {
        mAttachmentRepository.updateIsUploaded(attachmentId);
    }

    public void insertAttachment(Attachment_Table attachment) {
        mAttachmentRepository.insert(attachment);
    }

    public void updateExistingMFCDefect(int defectStatusId, String comment, int id) {
        mInspectionDefectRepository.updateExistingMFCDefect(defectStatusId, comment, id);
    }

    public int multifamilyDefectExists(int firstInspectionDetailId, int inspectionId) {
        return mInspectionDefectRepository.getExistingMFCDefect(firstInspectionDetailId, inspectionId);
    }

    public void insertSubmitRequest(SubmitRequest_Table submitRequest) {
        submitRequestRepository.insert(submitRequest);
    }
}
