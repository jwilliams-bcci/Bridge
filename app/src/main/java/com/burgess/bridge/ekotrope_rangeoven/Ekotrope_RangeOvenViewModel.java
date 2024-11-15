package com.burgess.bridge.ekotrope_rangeoven;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_RangeOvenRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_RangeOven_Table;

public class Ekotrope_RangeOvenViewModel extends AndroidViewModel {
    private Ekotrope_RangeOvenRepository mEkotropeRangeOvenRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_RangeOvenViewModel(@NonNull Application application) {
        super(application);
        mEkotropeRangeOvenRepository = new Ekotrope_RangeOvenRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_RangeOven_Table getRangeOven(String mPlanId) {
        return mEkotropeRangeOvenRepository.getRangeOven(mPlanId);
    }

    public void updateRangeOven(Ekotrope_RangeOven_Table mRangeOven) {
        mEkotropeRangeOvenRepository.update(mRangeOven);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
