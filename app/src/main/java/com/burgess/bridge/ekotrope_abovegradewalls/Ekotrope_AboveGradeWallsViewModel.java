package com.burgess.bridge.ekotrope_abovegradewalls;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_AboveGradeWallRepository;
import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Tables.Ekotrope_AboveGradeWall_Table;
import data.Tables.Ekotrope_ChangeLog_Table;

public class Ekotrope_AboveGradeWallsViewModel extends AndroidViewModel {
    private Ekotrope_AboveGradeWallRepository mEkotropeAboveGradeWallsRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_AboveGradeWallsViewModel(@NonNull Application application) {
        super(application);
        mEkotropeAboveGradeWallsRepository = new Ekotrope_AboveGradeWallRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_AboveGradeWall_Table getAboveGradeWall(String mPlanId, int mAboveGradeWallIndex) {
        return mEkotropeAboveGradeWallsRepository.getAboveGradeWall(mPlanId, mAboveGradeWallIndex);
    }

    public void updateAboveGradeWall(Ekotrope_AboveGradeWall_Table mAboveGradeWall) {
        mEkotropeAboveGradeWallsRepository.update(mAboveGradeWall);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
