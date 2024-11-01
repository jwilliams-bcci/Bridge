package com.burgess.bridge.ekotrope_windowslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_WindowRepository;
import data.Tables.Ekotrope_Window_Table;

public class Ekotrope_WindowsListViewModel extends AndroidViewModel {
    private Ekotrope_WindowRepository mWindowsRepository;

    public Ekotrope_WindowsListViewModel(@NonNull Application application) {
        super(application);
        mWindowsRepository = new Ekotrope_WindowRepository(application);
    }

    public LiveData<List<Ekotrope_Window_Table>> getWindows(String plan_id) {
        return mWindowsRepository.getWindows(plan_id);
    }
}
