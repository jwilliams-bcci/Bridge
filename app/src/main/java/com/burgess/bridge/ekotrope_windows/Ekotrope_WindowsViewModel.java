package com.burgess.bridge.ekotrope_windows;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_WindowRepository;
import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowsViewModel extends AndroidViewModel {
    private Ekotrope_WindowRepository mEkotropeWindowsRepository;

    public Ekotrope_WindowsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeWindowsRepository = new Ekotrope_WindowRepository(application);
    }

    public Ekotrope_Window_Table getWindow(String mPlanId, int mWindowIndex) {
        return mEkotropeWindowsRepository.getWindow(mPlanId, mWindowIndex);
    }

    public void updateWindow(Ekotrope_Window_Table mWindow) {
        mEkotropeWindowsRepository.update(mWindow);
    }
}
