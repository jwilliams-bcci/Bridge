package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import data.BridgeRoomDatabase;
import data.DAOs.Ekotrope_Data_DAO;
import data.Tables.Ekotrope_Data_Table;

public class EkotropeDataRepository {
    private Ekotrope_Data_DAO mEkotropeDataDao;

    public EkotropeDataRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mEkotropeDataDao = db.mEkotropeDataDao();
    }

    public LiveData<Ekotrope_Data_Table> getInspection(int inspection_id) {
        return mEkotropeDataDao.getInspection(inspection_id);
    }

    public Ekotrope_Data_Table getInspectionSync(int inspectionId) {
        return mEkotropeDataDao.getInspectionSync(inspectionId);
    }

    public void insert(Ekotrope_Data_Table i) { //ToDo: Ekotrope data needs to be used
        Ekotrope_Data_Table existingInspection = getInspectionSync(i.id);
        if (existingInspection != null) {
            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
                mEkotropeDataDao.update(i.id, i.inspection_type_id, i.inspection_date, i.division_id, i.location_id, i.builder_name, i.builder_id,
                        i.super_name, i.inspector_id, i.inspector, i.community, i.community_id, i.city, i.inspection_class, i.inspection_type,
                        i.reinspect, i.inspection_order, i.address, i.inspection_status_id, i.inspection_status, i.super_phone, i.super_email,
                        i.super_present, i.incomplete_reason, i.incomplete_reason_id, i.notes, i.job_number, i.require_risk_assessment, i.ekotrope_project_id);
            });
        } else {
            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
                mEkotropeDataDao.insert(i);
            });
        }
    }

    public void delete(int inspectionId) {
        mEkotropeDataDao.delete(inspectionId);
    }
}
