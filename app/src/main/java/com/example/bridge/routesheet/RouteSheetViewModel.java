package com.example.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.InspectionRepository;
import data.Tables.Inspection_Table;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mInspectionRepository;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public LiveData<List<Inspection_Table>> getAllInspectionsForRouteSheet(int inspector_id) {
        return mInspectionRepository.getAllInspectionsForRouteSheet(inspector_id);
    }

    public void insert(Inspection_Table inspection) {
        mInspectionRepository.insert(inspection);
    }
}
