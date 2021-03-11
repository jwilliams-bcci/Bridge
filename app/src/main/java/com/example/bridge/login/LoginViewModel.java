package com.example.bridge.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.DefectItemRepository;
import data.Tables.DefectItem_Table;

public class LoginViewModel extends AndroidViewModel {
    private DefectItemRepository mRepository;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DefectItemRepository(application);
    }

    public void insertDefectItem(DefectItem_Table defectItem) {
        mRepository.insert(defectItem);
    }
}
