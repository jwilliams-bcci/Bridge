package com.burgess.bridge.ekotrope_slabs;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_SlabRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_Slab_Table;

public class Ekotrope_SlabsViewModel extends AndroidViewModel {
    private Ekotrope_SlabRepository mEkotropeSlabsRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_SlabsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeSlabsRepository = new Ekotrope_SlabRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_Slab_Table getSlab(String mPlanId, int mSlabIndex) {
        return mEkotropeSlabsRepository.getSlab(mPlanId, mSlabIndex);
    }

    public void updateSlab(Ekotrope_Slab_Table mSlab) {
        mEkotropeSlabsRepository.update(mSlab);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
