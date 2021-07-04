package com.burgess.bridge.inspect;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionHistoryRepository;
import data.Repositories.InspectionRepository;
import data.Tables.DefectItem_Table;
import data.Tables.InspectionHistory_Table;
import data.Tables.Inspection_Table;

public class InspectViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private InspectionRepository mInspectionRepository;
    private InspectionHistoryRepository mInspectionHistoryRepository;

    public InspectViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mInspectionRepository = new InspectionRepository(application);
        mInspectionHistoryRepository = new InspectionHistoryRepository(application);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredNumberSort(String category_name, int inspection_type_id, int inspection_id) {
        if (category_name.equals("ALL")) {
            return mDefectItemRepository.getAllDefectItemsNumberSort(inspection_type_id);
        } else {
            return mDefectItemRepository.getAllDefectItemsFilteredNumberSort(category_name, inspection_type_id);
        }
    }

    public LiveData<List<InspectionHistory_Table>> getInspectionHistoryFilteredNumberSort(String category_name, int inspection_type_id, int inspection_id) {
        if (category_name.equals("ALL")) {
            return mInspectionHistoryRepository.getInspectionHistoryNumberSort(inspection_id);
        } else {
            return mInspectionHistoryRepository.getInspectionHistoryFilteredNumberSort(category_name, inspection_id);
        }
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredDescriptionSort(String category_name, int inspection_type_id, int inspection_id) {
        if (category_name.equals("ALL")) {
            return mDefectItemRepository.getAllDefectItemsDescriptionSort(inspection_type_id);
        } else {
            return mDefectItemRepository.getAllDefectItemsFilteredDescriptionSort(category_name, inspection_type_id);
        }
    }

    public LiveData<List<InspectionHistory_Table>> getInspectionHistoryFilteredDescriptionSort(String category_name, int inspection_type_id, int inspection_id) {
        if (category_name.equals("ALL")) {
            return mInspectionHistoryRepository.getInspectionHistoryDescriptionSort(inspection_id);
        } else {
            return mInspectionHistoryRepository.getInspectionHistoryFilteredDescriptionSort(category_name, inspection_id);
        }
    }

    public LiveData<List<String>> getDefectCategories(int inspection_type_id) {
        return mDefectItemRepository.getDefectCategories(inspection_type_id);
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionRepository.getInspection(inspection_id);
    }

    public boolean getReinspect(int inspection_id) {
        return mInspectionRepository.getReinspect(inspection_id);
    }

    public LiveData<Integer> getInspectionTypeId(int inspection_id) {
        return mInspectionRepository.getInspectionTypeId(inspection_id);
    }
}
