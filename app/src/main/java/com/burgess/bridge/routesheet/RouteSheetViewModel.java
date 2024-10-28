package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.DefectItem_InspectionType_XRefRepository;
import data.Repositories.Ekotrope_FramedFloorRepository;
import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Repositories.PastInspectionRepository;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;
import data.Tables.Ekotrope_FramedFloor_Table;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Tables.PastInspection_Table;
import data.Views.RouteSheet_View;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private InspectionDefectRepository mInspectionDefectRepository;
    private DefectItemRepository mDefectItemRepository;
    private DefectItem_InspectionType_XRefRepository mDIITRepository;
    private PastInspectionRepository mPastInspectionRepository;
    private Ekotrope_FramedFloorRepository mEkotropeFramedFloorRepository;
    private String reportUrl;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mDIITRepository = new DefectItem_InspectionType_XRefRepository(application);
        mPastInspectionRepository = new PastInspectionRepository(application);
        reportUrl = "";
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

    public int multifamilyDefectExists(int firstInspectionDetailId) {
        return mInspectionDefectRepository.multifamilyDefectExists(firstInspectionDetailId);
    }

    public void updateExistingMFCDefect(int defectStatusId, String comment, int id) {
        mInspectionDefectRepository.updateExistingMFCDefect(defectStatusId, comment, id);
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

    public void insertPastInspection(PastInspection_Table pastInspection) {
        mPastInspectionRepository.insert(pastInspection);
    }

    public void updateUrl(String newUrl) {
        reportUrl = newUrl;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void updateEkotropePlanId(String planId, int inspectionId) {
        mInspectionRepository.updateEkotropePlanId(planId, inspectionId);
    }

    public void insertFramedFloor(Ekotrope_FramedFloor_Table framedFloor) {
        mEkotropeFramedFloorRepository.insert(framedFloor);
    }
}
