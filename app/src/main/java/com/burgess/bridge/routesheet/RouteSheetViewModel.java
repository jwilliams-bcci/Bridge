package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.InspectionDefectRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Tables.InspectionDefect_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;
    private InspectionDefectRepository mInspectionDefectRepository;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
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

    public void deleteInspection(int inspectionId) {
        mInspectionRepository.delete(inspectionId);
    }

    public void insertInspectionHistory(InspectionHistory_Table inspectionHistory) {
        mInspectionHistoryRepository.insert(inspectionHistory);
    }

    public void updateRouteSheetIndex(int inspection_id, int new_order) {
        mInspectionRepository.updateRouteSheetIndex(inspection_id, new_order);
    }
}
