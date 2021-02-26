package data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.DAOs.Inspection_DAO;
import data.Tables.Inspection_Table;

public class InspectionRepository {
    private Inspection_DAO mInspectionDao;
    private LiveData<List<RouteSheet_View>> mAllInspectionsForRouteSheet;

    public InspectionRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionDao = db.mInspectionDao();
        mAllInspectionsForRouteSheet = mInspectionDao.getInspectionsForRouteSheet();
    }

    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet() {
        return mAllInspectionsForRouteSheet;
    }

    public void insert(Inspection_Table inspection) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDao.insert(inspection);
        });
    }
}
