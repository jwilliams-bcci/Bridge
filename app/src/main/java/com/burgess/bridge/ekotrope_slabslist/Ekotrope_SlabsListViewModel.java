package com.burgess.bridge.ekotrope_slabslist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_SlabRepository;
import data.Tables.Ekotrope_Slab_Table;

public class Ekotrope_SlabsListViewModel extends AndroidViewModel {
    private Ekotrope_SlabRepository mSlabsRepository;

    public Ekotrope_SlabsListViewModel(@NonNull Application application) {
        super(application);
        mSlabsRepository = new Ekotrope_SlabRepository(application);
    }

    public LiveData<List<Ekotrope_Slab_Table>> getSlabs(String plan_id) {
        return mSlabsRepository.getSlabs(plan_id);
    }
}
