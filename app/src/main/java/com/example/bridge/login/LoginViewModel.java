package com.example.bridge.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.CannedCommentRepository;
import data.Repositories.DefectItemRepository;
import data.Tables.CannedComment_Table;
import data.Tables.DefectItem_Table;

public class LoginViewModel extends AndroidViewModel {
    private DefectItemRepository mDefectItemRepository;
    private CannedCommentRepository mCannedCommentRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mDefectItemRepository = new DefectItemRepository(application);
        mCannedCommentRepository = new CannedCommentRepository(application);
    }

    public void insertDefectItem(DefectItem_Table defectItem) {
        mDefectItemRepository.insert(defectItem);
    }

    public void insertCannedComment(CannedComment_Table cannedComment) {
        mCannedCommentRepository.insert(cannedComment);
    }
}
