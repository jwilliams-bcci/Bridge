package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.InspectionHistory_DAO;
import data.Tables.InspectionHistory_Table;

public class InspectionHistoryRepository {
    private InspectionHistory_DAO mInspectionHistoryDao;

    public InspectionHistoryRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionHistoryDao = db.mInspectionHistoryDao();
    }

    public LiveData<List<InspectionHistory_Table>> getInspectionHistory(int inspection_id) {
        return mInspectionHistoryDao.getInspectionHistory(inspection_id);
    }

    public LiveData<List<InspectionHistory_Table>> getInspectionHistoryNumberSort(int inspection_id) {
        return mInspectionHistoryDao.getInspectionHistoryNumberSort(inspection_id);
    }
    public LiveData<List<InspectionHistory_Table>> getInspectionHistoryDescriptionSort(int inspection_id) {
        return mInspectionHistoryDao.getInspectionHistoryDescriptionSort(inspection_id);
    }

    public LiveData<List<InspectionHistory_Table>> getInspectionHistoryFilteredNumberSort(String category_name, int inspection_id) {
        return mInspectionHistoryDao.getInspectionHistoryFilteredNumberSort(category_name, inspection_id);
    }
    public LiveData<List<InspectionHistory_Table>> getInspectionHistoryFilteredDescriptionSort(String category_name, int inspection_id) {
        return mInspectionHistoryDao.getInspectionHistoryFilteredDescriptionSort(category_name, inspection_id);
    }

    public void insert(InspectionHistory_Table inspectionHistory) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionHistoryDao.insert(inspectionHistory);
        });
    }
}
