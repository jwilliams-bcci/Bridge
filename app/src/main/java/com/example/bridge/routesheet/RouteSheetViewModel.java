package com.example.bridge.routesheet;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.InspectionRepository;
import data.Tables.Inspection_Table;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mRepository;
    private final LiveData<List<Inspection_Table>> mAllInspectionsForRouteSheet;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InspectionRepository(application);
        mAllInspectionsForRouteSheet = mRepository.getAllInspectionsForRouteSheet();
    }

    public LiveData<List<Inspection_Table>> getAllInspectionsForRouteSheet() {
        return mAllInspectionsForRouteSheet;
    }

    public void insert(Inspection_Table inspection) {
        mRepository.insert(inspection);
    }
}
