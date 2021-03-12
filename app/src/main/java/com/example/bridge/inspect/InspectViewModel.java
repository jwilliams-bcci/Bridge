package com.example.bridge.inspect;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionRepository;
import data.Tables.DefectItem_Table;
import data.Tables.Inspection_Table;

public class InspectViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private InspectionRepository mInspectionRepository;

    public InspectViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mInspectionRepository = new InspectionRepository(application);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItems() {
        return mDefectItemRepository.getAllDefectItems();
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFiltered(String categoryName) {
        return mDefectItemRepository.getAllDefectItemsFiltered(categoryName);
    }

    public LiveData<List<String>> getDefectCategories() {
        return mDefectItemRepository.getDefectCategories();
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionRepository.getInspection(inspection_id);
    }
}
