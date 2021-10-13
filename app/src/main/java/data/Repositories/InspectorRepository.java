package data.Repositories;

import android.app.Application;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Inspector_DAO;
import data.Tables.Inspector_Table;

public class InspectorRepository {
    private Inspector_DAO mInspectorDao;

    public InspectorRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectorDao = db.mInspectorDao();
    }

    public List<Inspector_Table> getInspectors() {
        return mInspectorDao.getInspectors();
    }

    public List<Inspector_Table> getInspectorsByDivision(int divisionId) {
        return mInspectorDao.getInspectorsByDivision(divisionId);
    }

    public void insert(Inspector_Table inspector) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectorDao.insert(inspector);
        });
    }
}
