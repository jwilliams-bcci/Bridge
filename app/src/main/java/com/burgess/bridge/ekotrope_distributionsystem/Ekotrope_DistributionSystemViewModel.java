package com.burgess.bridge.ekotrope_distributionsystem;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_DistributionSystemRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_DistributionSystem_Table;

public class Ekotrope_DistributionSystemViewModel extends AndroidViewModel {
    private Ekotrope_DistributionSystemRepository mEkotropeDistributionSystemRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_DistributionSystemViewModel(@NonNull Application application) {
        super(application);
        mEkotropeDistributionSystemRepository = new Ekotrope_DistributionSystemRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_DistributionSystem_Table getDistributionSystem(String mPlanId, int index) {
        return mEkotropeDistributionSystemRepository.getDistributionSystem(mPlanId, index);
    }

    public void updateDistributionSystem(Ekotrope_DistributionSystem_Table mDistributionSystem) {
        mEkotropeDistributionSystemRepository.update(mDistributionSystem);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
