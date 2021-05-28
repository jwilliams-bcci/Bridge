package com.burgess.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.InspectionHistory;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
    }

    public LiveData<List<Inspection_Table>> getAllInspectionsForRouteSheet(int inspector_id) {
        return mInspectionRepository.getAllInspectionsForRouteSheet(inspector_id);
    }

    public void insertInspection(Inspection_Table inspection) {
        mInspectionRepository.insert(inspection);
    }

    public void insertInspectionHistory(InspectionHistory_Table inspectionHistory) {
        mInspectionHistoryRepository.insert(inspectionHistory);
    }

    public void swapOrder(int inspection_id, int new_order) {
        mInspectionRepository.swapOrder(inspection_id, new_order);
    }
}
