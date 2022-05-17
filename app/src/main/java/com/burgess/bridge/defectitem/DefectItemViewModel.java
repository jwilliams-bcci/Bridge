package com.burgess.bridge.defectitem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.CannedCommentRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.DirectionRepository;
import data.Repositories.FaultRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Repositories.RoomRepository;
import data.Tables.DefectItem_Table;
import data.Tables.Direction_Table;
import data.Tables.Fault_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;
import data.Tables.Room_Table;

public class DefectItemViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private CannedCommentRepository mCannedCommentRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private RoomRepository mRoomRepository;
    private DirectionRepository mDirectionRepository;
    private FaultRepository mFaultRepository;

    public DefectItemViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mRoomRepository = new RoomRepository(application);
        mDirectionRepository = new DirectionRepository(application);
        mFaultRepository = new FaultRepository(application);
    }

    public LiveData<DefectItem_Table> getDefectItem(int defect_item_id) {
        return mDefectItemRepository.getDefectItem(defect_item_id);
    }

    public DefectItem_Table getDefectItemSync(int defect_item_id) {
        return mDefectItemRepository.getDefectItemSync(defect_item_id);
    }

    public LiveData<List<String>> getEnergyCannedComments() {
        return mCannedCommentRepository.getEnergyCannedComments();
    }

    public List<String> getEnergyCannedCommentsSync() {
        return mCannedCommentRepository.getEnergyCannedCommentsSync();
    }

    public LiveData<List<String>> getCannedComments() {
        return mCannedCommentRepository.getCannedComments();
    }

    public List<String> getCannedCommentsSync() {
        return mCannedCommentRepository.getCannedCommentsSync();
    }

    public long insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        return mInspectionDefectRepository.insert(inspectionDefect);
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionRepository.getInspection(inspection_id);
    }

    public Inspection_Table getInspectionSync(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }

    public InspectionDefect_Table getInspectionDefect(int inspectionDefectId) {
        return mInspectionDefectRepository.getInspectionDefect(inspectionDefectId);
    }

    public void updateInspectionDefect(InspectionDefect_Table inspectionDefect) {
        mInspectionDefectRepository.updateInspectionDefect(inspectionDefect);
    }

    public String getInspectionHistoryComment(int inspectionHistoryId) {
        return mInspectionHistoryRepository.getComment(inspectionHistoryId);
    }

    public void updateIsReviewed (int inspectionHistoryId) {
        mInspectionHistoryRepository.updateIsReviewed(inspectionHistoryId);
    }

    public void updateReviewedStatus(int defectStatusId, int inspectionHistoryId) {
        mInspectionHistoryRepository.updateReviewedStatus(defectStatusId, inspectionHistoryId);
    }

    public void updateInspectionDefectId(int inspectionDefectId, int inspectionHistoryId) {
        mInspectionHistoryRepository.updateInspectionDefectId(inspectionDefectId, inspectionHistoryId);
    }

    public List<Room_Table> getRooms() {
        return mRoomRepository.getRooms();
    }

    public List<Direction_Table> getDirections() {
        return mDirectionRepository.getDirections();
    }

    public List<Fault_Table> getFaults() {
        return mFaultRepository.getFaults();
    }
}
