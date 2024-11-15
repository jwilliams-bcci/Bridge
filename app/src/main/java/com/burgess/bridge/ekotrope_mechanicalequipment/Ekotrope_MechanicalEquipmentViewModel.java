package com.burgess.bridge.ekotrope_mechanicalequipment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import data.Repositories.Ekotrope_ChangeLogRepository;
import data.Repositories.Ekotrope_MechanicalEquipmentRepository;
import data.Tables.Ekotrope_ChangeLog_Table;
import data.Tables.Ekotrope_MechanicalEquipment_Table;

public class Ekotrope_MechanicalEquipmentViewModel extends AndroidViewModel {
    private Ekotrope_MechanicalEquipmentRepository mEkotropeMechanicalEquipmentRepository;
    private Ekotrope_ChangeLogRepository mEkotropeChangeLogRepository;

    public Ekotrope_MechanicalEquipmentViewModel(@NonNull Application application) {
        super(application);
        mEkotropeMechanicalEquipmentRepository = new Ekotrope_MechanicalEquipmentRepository(application);
        mEkotropeChangeLogRepository = new Ekotrope_ChangeLogRepository(application);
    }

    public Ekotrope_MechanicalEquipment_Table getMechanicalEquipment(String mPlanId, int mMechanicalEquipmentIndex) {
        return mEkotropeMechanicalEquipmentRepository.getMechanicalEquipment(mPlanId, mMechanicalEquipmentIndex);
    }

    public void updateMechanicalEquipment(Ekotrope_MechanicalEquipment_Table mMechanicalEquipment) {
        mEkotropeMechanicalEquipmentRepository.update(mMechanicalEquipment);
    }

    public void insertChangeLog(Ekotrope_ChangeLog_Table mChangeLog) {
        mEkotropeChangeLogRepository.insert(mChangeLog);
    }
}
