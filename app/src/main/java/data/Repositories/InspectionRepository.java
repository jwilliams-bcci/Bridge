package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Inspection_DAO;
import data.Tables.Inspection_Table;

public class InspectionRepository {
    private Inspection_DAO mInspectionDao;

    public InspectionRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionDao = db.mInspectionDao();
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionDao.getInspection(inspection_id);
    }

    public LiveData<List<Inspection_Table>> getAllInspectionsForRouteSheet(int inspector_id) {
        return mInspectionDao.getInspections(inspector_id);
    }

    public LiveData<Integer> getInspectionTypeId(int inspection_id) {
        return mInspectionDao.getInspectionTypeId(inspection_id);
    }

    public boolean getReinspect(int inspection_id) {
        return mInspectionDao.getReinspect(inspection_id);
    }

    public void insert(Inspection_Table inspection) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDao.insert(inspection);
        });
    }

    public void completeInspection(Date end_time, int inspection_id) {
        mInspectionDao.completeInspection(end_time, inspection_id);
    }

    public void swapOrder(int inspection_id, int new_order) {
        mInspectionDao.swapOrder(inspection_id, new_order);
    }

    public void startInspection(Date start_time, int inspection_id) {
        mInspectionDao.startInspection(start_time, inspection_id);
    }
}
