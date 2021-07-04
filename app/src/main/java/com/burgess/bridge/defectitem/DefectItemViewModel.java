package com.burgess.bridge.defectitem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.CannedCommentRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Tables.DefectItem_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.Inspection_Table;

public class DefectItemViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private CannedCommentRepository mCannedCommentRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;

    public DefectItemViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
    }

    public LiveData<DefectItem_Table> getDefectItem(int defect_item_id) {
        return mDefectItemRepository.getDefectItem(defect_item_id);
    }

    public LiveData<List<String>> getCannedComments() {
        return mCannedCommentRepository.getCannedComments();
    }

    public void insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        mInspectionDefectRepository.insert(inspectionDefect);
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionRepository.getInspection(inspection_id);
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
}
