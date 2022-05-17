package com.burgess.bridge.inspect;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Tables.DefectItem_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;

public class InspectViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private InspectionDefectRepository mInspectionDefectRepository;

    public InspectViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredNumberSort(String category_name, int inspection_type_id, int inspection_id) {
        if (category_name.equals("ALL")) {
            return mDefectItemRepository.getAllDefectItemsNumberSort(inspection_type_id);
        } else {
            return mDefectItemRepository.getAllDefectItemsFilteredNumberSort(category_name, inspection_type_id);
        }
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredDescriptionSort(String category_name, int inspection_type_id, int inspection_id) {
        if (category_name.equals("ALL")) {
            return mDefectItemRepository.getAllDefectItemsDescriptionSort(inspection_type_id);
        } else {
            return mDefectItemRepository.getAllDefectItemsFilteredDescriptionSort(category_name, inspection_type_id);
        }
    }

    public LiveData<List<InspectionHistory_Table>> getInspectionHistory(int inspection_id) {
        return mInspectionHistoryRepository.getInspectionHistory(inspection_id);
    }

    public LiveData<List<String>> getDefectCategories(int inspection_type_id) {
        return mDefectItemRepository.getDefectCategories(inspection_type_id);
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionRepository.getInspection(inspection_id);
    }

    public Inspection_Table getInspectionSync(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }

    public boolean getReinspect(int inspection_id) {
        return mInspectionRepository.getReinspect(inspection_id);
    }

    public int getItemsToReview(int inspectionId) {
        return mInspectionHistoryRepository.getItemsToReview(inspectionId);
    }

    public void updateIsReviewed(int inspectionHistoryId) {
        mInspectionHistoryRepository.updateIsReviewed(inspectionHistoryId);
    }

    public void updateReviewedStatus(int defectStatusId, int inspectionHistoryId) {
        mInspectionHistoryRepository.updateReviewedStatus(defectStatusId, inspectionHistoryId);
    }

    public long insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        return mInspectionDefectRepository.insert(inspectionDefect);
    }

    public void updateInspectionDefectId(int inspectionDefectId, int inspectionHistoryId) {
        mInspectionHistoryRepository.updateInspectionDefectId(inspectionDefectId, inspectionHistoryId);
    }

    public void updateInspectionDefect(InspectionDefect_Table inspectionDefect) {
        mInspectionDefectRepository.updateInspectionDefect(inspectionDefect);
    }

    public InspectionDefect_Table getInspectionDefect(int inspectionDefectId) {
        return mInspectionDefectRepository.getInspectionDefect(inspectionDefectId);
    }
}
