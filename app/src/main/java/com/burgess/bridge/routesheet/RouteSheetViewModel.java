package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.DefectItem_InspectionType_XRefRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private DefectItemRepository mDefectItemRepository;
    private DefectItem_InspectionType_XRefRepository mDIITRepository;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mDIITRepository = new DefectItem_InspectionType_XRefRepository(application);
    }

    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet(int inspectorId) {
        return mInspectionRepository.getAllInspectionsForRouteSheet(inspectorId);
    }

    public List<Integer> getAllInspectionIds(int inspectorId) {
        return mInspectionRepository.getAllInspectionIds(inspectorId);
    }

    public void insertInspection(Inspection_Table inspection) {
        mInspectionRepository.insert(inspection);
    }

    public void insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        mInspectionDefectRepository.insert(inspectionDefect);
    }

    public boolean multifamilyDefectExists(int priorInspectionDefectId, int inspectionId) {
        return mInspectionDefectRepository.multifamilyDefectExists(priorInspectionDefectId, inspectionId);
    }

    public void insertInspectionHistory(InspectionHistory_Table inspectionHistory) {
        mInspectionHistoryRepository.insert(inspectionHistory);
    }

    public void updateRouteSheetIndex(int inspection_id, int new_order) {
        mInspectionRepository.updateRouteSheetIndex(inspection_id, new_order);
    }

    public void insertDefectItem(DefectItem_Table defectItem) {
        mDefectItemRepository.insert(defectItem);
    }

    public void insertReference(DefectItem_InspectionType_XRef relation) {
        mDIITRepository.insert(relation);
    }

    public Inspection_Table getInspection(int inspection_id) {
        return mInspectionRepository.getInspectionSync(inspection_id);
    }

    public void deleteInspectionDefects(int inspection_id) {
        mInspectionDefectRepository.delete(inspection_id);
    }

    public void deleteInspection(int id) {
        mInspectionRepository.delete(id);
    }

    public void deleteInspectionHistories(int inspection_id) {
        mInspectionHistoryRepository.deleteForInspection(inspection_id);
    }
}
