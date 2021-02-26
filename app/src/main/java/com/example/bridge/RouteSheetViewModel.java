package com.example.bridge;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.InspectionRepository;
import data.Tables.Inspection_Table;
import data.RouteSheet_View;

public class RouteSheetViewModel extends AndroidViewModel {
    private InspectionRepository mRepository;
    private final LiveData<List<RouteSheet_View>> mAllInspectionsForRouteSheet;

    public RouteSheetViewModel(@NonNull Application application) {
        super(application);
        mRepository = new InspectionRepository(application);
        mAllInspectionsForRouteSheet = mRepository.getAllInspectionsForRouteSheet();
    }

    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet() {
        return mAllInspectionsForRouteSheet;
    }

    public void insert(Inspection_Table inspection) {
        mRepository.insert(inspection);
    }
}
