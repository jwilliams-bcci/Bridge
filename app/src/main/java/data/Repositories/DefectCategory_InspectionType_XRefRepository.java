package data.Repositories;

import android.app.Application;

import data.BridgeRoomDatabase;
import data.DAOs.DefectCategory_InspectionType_DAO;
import data.Tables.DefectCategory_InspectionType_XRef;

public class DefectCategory_InspectionType_XRefRepository {
    private DefectCategory_InspectionType_DAO mDCITDao;

    public DefectCategory_InspectionType_XRefRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mDCITDao = db.mDefectCategory_InspectionTypeDao();
    }

    public void insert(DefectCategory_InspectionType_XRef reference) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mDCITDao.insert(reference);
        });
    }
}
