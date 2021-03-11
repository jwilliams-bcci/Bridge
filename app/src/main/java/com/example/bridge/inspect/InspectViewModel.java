package com.example.bridge.inspect;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.DefectItemRepository;
import data.Tables.DefectItem_Table;

public class InspectViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;

    public InspectViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
    }

    public LiveData<List<DefectItem_Table>> getAllDefectItems() {
        return mDefectItemRepository.getAllDefectItems();
    }
}
