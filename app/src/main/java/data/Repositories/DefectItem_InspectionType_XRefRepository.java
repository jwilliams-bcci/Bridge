package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.DefectItem_InspectionType_DAO;
import data.Tables.DefectItem_InspectionType_XRef;

public class DefectItem_InspectionType_XRefRepository {
    private DefectItem_InspectionType_DAO relationDao;

    public DefectItem_InspectionType_XRefRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        relationDao = db.mDefectItem_InspectionTypeDao();
    }

    public void insert(DefectItem_InspectionType_XRef relation) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            relationDao.insert(relation);
        });
    }
}
