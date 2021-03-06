package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Inspection_DAO;
import data.Tables.Inspection_Table;

public class InspectionRepository {
    private Inspection_DAO mInspectionDao;
    private LiveData<List<Inspection_Table>> mAllInspectionsForRouteSheet;

    public InspectionRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionDao = db.mInspectionDao();
        mAllInspectionsForRouteSheet = mInspectionDao.getInspections();
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionDao.getInspection(inspection_id);
    }

    public LiveData<List<Inspection_Table>> getAllInspectionsForRouteSheet() {
        return mAllInspectionsForRouteSheet;
    }

    public void insert(Inspection_Table inspection) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDao.insert(inspection);
        });
    }
}
