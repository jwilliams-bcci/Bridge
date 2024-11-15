package com.burgess.bridge.ekotrope_mechanicalventilation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_MechanicalVentilationRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_MechanicalVentilation_Table;

public class Ekotrope_MechanicalVentilationViewModel extends AndroidViewModel {
    private Ekotrope_MechanicalVentilationRepository mEkotropeMechanicalVentilationRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_MechanicalVentilationViewModel(@NonNull Application application) {
        super(application);
        mEkotropeMechanicalVentilationRepository = new Ekotrope_MechanicalVentilationRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_MechanicalVentilation_Table getMechanicalVentilation(String mPlanId, int index) {
        return mEkotropeMechanicalVentilationRepository.getMechanicalVentilation(mPlanId, index);
    }

    public void updateMechanicalVentilation(Ekotrope_MechanicalVentilation_Table mMechanicalVentilation) {
        mEkotropeMechanicalVentilationRepository.update(mMechanicalVentilation);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table changeLogEntry) {
        mEkotropeChangeLogRepository.insert(changeLogEntry);
    }
}
