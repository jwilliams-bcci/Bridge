package com.burgess.bridge.ekotrope_abovegradewallslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_AboveGradeWallRepository;
import data.Tables.Ekotrope_AboveGradeWall_Table;

public class Ekotrope_AboveGradeWallsListViewModel extends AndroidViewModel {
    private Ekotrope_AboveGradeWallRepository mAboveGradeWallsRepository;

    public Ekotrope_AboveGradeWallsListViewModel(@NonNull Application application) {
        super(application);
        mAboveGradeWallsRepository = new Ekotrope_AboveGradeWallRepository(application);
    }

    public LiveData<List<Ekotrope_AboveGradeWall_Table>> getAboveGradeWalls(String plan_id) {
        return mAboveGradeWallsRepository.getAboveGradeWalls(plan_id);
    }
}
