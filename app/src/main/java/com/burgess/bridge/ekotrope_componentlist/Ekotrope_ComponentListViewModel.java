package com.burgess.bridge.ekotrope_componentlist;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.Ekotrope_DistributionSystemRepository;
import data.Repositories.Ekotrope_DuctRepository;
import data.Repositories.Ekotrope_MechanicalEquipmentRepository;
import data.Repositories.Ekotrope_MechanicalVentilationRepository;
import data.Tables.Ekotrope_DistributionSystem_Table;
import data.Tables.Ekotrope_Duct_Table;
import data.Tables.Ekotrope_MechanicalEquipment_Table;
import data.Tables.Ekotrope_MechanicalVentilation_Table;

public class Ekotrope_ComponentListViewModel extends AndroidViewModel {
    private Ekotrope_MechanicalEquipmentRepository mMechanicalEquipmentRepository;
    private Ekotrope_DistributionSystemRepository mDistributionSystemRepository;
    private Ekotrope_DuctRepository mDuctRepository;
    private Ekotrope_MechanicalVentilationRepository mMechanicalVentilationRepository;

    public Ekotrope_ComponentListViewModel(@NonNull Application application) {
        super(application);
        mMechanicalEquipmentRepository = new Ekotrope_MechanicalEquipmentRepository(application);
        mDistributionSystemRepository = new Ekotrope_DistributionSystemRepository(application);
        mDuctRepository = new Ekotrope_DuctRepository(application);
        mMechanicalVentilationRepository = new Ekotrope_MechanicalVentilationRepository(application);
    }

    public LiveData<List<Ekotrope_MechanicalEquipment_Table>> getMechanicalEquipments(String plan_id) {
        return mMechanicalEquipmentRepository.getMechanicalEquipments(plan_id);
    }
    public LiveData<List<Ekotrope_DistributionSystem_Table>> getDistributionSystems(String plan_id) {
        return mDistributionSystemRepository.getDistributionSystems(plan_id);
    }
    public LiveData<List<Ekotrope_Duct_Table>> getDucts(String plan_id, int ds_id) {
        return mDuctRepository.getDucts(plan_id, ds_id);
    }
    public LiveData<List<Ekotrope_MechanicalVentilation_Table>> getMechanicalVentilations(String plan_id) {
        return mMechanicalVentilationRepository.getMechanicalVentilations(plan_id);
    }
}
