package com.example.bridge.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.CannedCommentRepository;
import data.Repositories.DefectCategory_InspectionType_XRefRepository;
import data.Repositories.DefectItemRepository;
import data.Repositories.DefectItem_InspectionType_XRefRepository;
import data.Tables.CannedComment_Table;
import data.Tables.DefectCategory_InspectionType_XRef;
import data.Tables.DefectItem_InspectionType_XRef;
import data.Tables.DefectItem_Table;

public class LoginViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private CannedCommentRepository mCannedCommentRepository;
    private DefectItem_InspectionType_XRefRepository mRelationRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
        mRelationRepository = new DefectItem_InspectionType_XRefRepository(application);
    }

    public void insertDefectItem(DefectItem_Table defectItem) {
        mDefectItemRepository.insert(defectItem);
    }

    public void insertCannedComment(CannedComment_Table cannedComment) {
        mCannedCommentRepository.insert(cannedComment);
    }

    public void insertReference(DefectItem_InspectionType_XRef relation) {
        mRelationRepository.insert(relation);
    }
}
