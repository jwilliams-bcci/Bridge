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

    public LiveData<List<DefectItem_Table>> getAllDefectItems(int inspection_type_id) {
        return mDefectItemRepository.getAllDefectItems(inspection_type_id);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFiltered(String category_name) {
        return mDefectItemRepository.getAllDefectItemsFiltered(category_name);
    }

    public LiveData<List<String>> getDefectCategories(int inspection_type_id) {
        return mDefectItemRepository.getDefectCategories(inspection_type_id);
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionRepository.getInspection(inspection_id);
    }

    public LiveData<Integer> getInspectionTypeId(int inspection_id) {
        return mInspectionRepository.getInspectionTypeId(inspection_id);
    }
}
