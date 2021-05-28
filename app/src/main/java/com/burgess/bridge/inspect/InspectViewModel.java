package com.burgess.bridge.inspect;

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

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredNumberSort(String category_name, int inspection_type_id, boolean isReinspect, int inspection_id) {
        if (category_name.equals("ALL")) {
            if (isReinspect) {
                return mDefectItemRepository.getAllReinspectionDefectItemsNumberSort(inspection_id);
            } else {
                return mDefectItemRepository.getAllDefectItemsNumberSort(inspection_type_id);
            }
        } else {
            if (isReinspect) {
                return mDefectItemRepository.getAllReinspectionDefectItemsFilteredNumberSort(category_name, inspection_id);
            } else {
                return mDefectItemRepository.getAllDefectItemsFilteredNumberSort(category_name, inspection_type_id);
            }
        }
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItemsFilteredDescriptionSort(String category_name, int inspection_type_id, boolean isReinspect, int inspection_id) {
        if (category_name.equals("ALL")) {
            if (isReinspect) {
                return mDefectItemRepository.getAllReinspectionDefectItemsDescriptionSort(inspection_id);
            } else {
                return mDefectItemRepository.getAllDefectItemsDescriptionSort(inspection_type_id);
            }
        } else {
            if (isReinspect) {
                return mDefectItemRepository.getAllReinspectionDefectItemsFilteredDescriptionSort(category_name, inspection_id);
            } else {
                return mDefectItemRepository.getAllDefectItemsFilteredDescriptionSort(category_name, inspection_type_id);
            }
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
