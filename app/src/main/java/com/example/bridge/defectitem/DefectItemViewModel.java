package com.example.bridge.defectitem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.CannedCommentRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.InspectionDefectRepository;
import data.Tables.DefectItem_Table;
import data.Tables.InspectionDefect_Table;

public class DefectItemViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private CannedCommentRepository mCannedCommentRepository;
    private InspectionDefectRepository mInspectionDefectRepository;

    public DefectItemViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
        mInspectionDefectRepository = new InspectionDefectRepository(application);
    }

    public LiveData<DefectItem_Table> getDefectItem(int defect_item_id) {
        return mDefectItemRepository.getDefectItem(defect_item_id);
    }

    public LiveData<List<String>> getCannedComments() {
        return mCannedCommentRepository.getCannedComments();
    }

    public void insertInspectionDefect(InspectionDefect_Table inspectionDefect) {
        mInspectionDefectRepository.insert(inspectionDefect);
    }
}
