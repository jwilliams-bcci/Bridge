package com.burgess.bridge.ekotrope_windows;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_WindowRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowsViewModel extends AndroidViewModel {
    private Ekotrope_WindowRepository mEkotropeWindowsRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_WindowsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeWindowsRepository = new Ekotrope_WindowRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Window_Table getWindow(String mPlanId, int mWindowIndex) {
        return mEkotropeWindowsRepository.getWindow(mPlanId, mWindowIndex);
    }

    public void updateWindow(Ekotrope_Window_Table mWindow) {
        mEkotropeWindowsRepository.update(mWindow);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
