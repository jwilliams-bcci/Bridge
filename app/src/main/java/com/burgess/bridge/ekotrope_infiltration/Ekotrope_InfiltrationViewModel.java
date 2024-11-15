package com.burgess.bridge.ekotrope_infiltration;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_InfiltrationRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Infiltration_Table;

public class Ekotrope_InfiltrationViewModel extends AndroidViewModel {
    private Ekotrope_InfiltrationRepository mInfiltrationRepository;
    private Ekotrope_ChangeLogRepository mChangeLogRepository;

    public Ekotrope_InfiltrationViewModel(Application application) {
        super(application);
        mInfiltrationRepository = new Ekotrope_InfiltrationRepository(application);
        mChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public void insert(Ekotrope_Infiltration_Table infiltration) {
        mInfiltrationRepository.insert(infiltration);
    }

    public Ekotrope_Infiltration_Table getInfiltration(String mPlanId) {
        return mInfiltrationRepository.getInfiltration(mPlanId);
    }

    public void update(Ekotrope_Infiltration_Table infiltration) {
        mInfiltrationRepository.update(infiltration);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table changeLog) {
        mChangeLogRepository.insert(changeLog);
    }
}
