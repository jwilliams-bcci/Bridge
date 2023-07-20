package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.PastInspection_DAO;
import data.Tables.PastInspection_Table;

public class PastInspectionRepository {
    private PastInspection_DAO mPastInspectionDao;

    public PastInspectionRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mPastInspectionDao = db.mPastInspectionDao();
    }

    public LiveData<List<PastInspection_Table>> getPastInspections(int location_id) {
        return mPastInspectionDao.getPastInspections(location_id);
    }

    public void insert(PastInspection_Table pastInspection) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mPastInspectionDao.insert(pastInspection);
        });
    }
}
