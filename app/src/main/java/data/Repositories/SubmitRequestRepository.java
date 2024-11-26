package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.SubmitRequest_DAO;
import data.Tables.SubmitRequest_Table;

public class SubmitRequestRepository {
    private SubmitRequest_DAO mSubmitRequestDao;

    public SubmitRequestRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mSubmitRequestDao = db.mSubmitRequestDao();
    }

    public void insert(SubmitRequest_Table submitRequest) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mSubmitRequestDao.insert(submitRequest);
        });
    }

    public void delete(int inspection_id) {
        mSubmitRequestDao.delete(inspection_id);
    }

    public LiveData<List<SubmitRequest_Table>> getSubmitRequests(int inspectionId) {
        return mSubmitRequestDao.getSubmitRequests(inspectionId);
    }

    public void markComplete(int inspection_id) {
        mSubmitRequestDao.markComplete(inspection_id);
    }

    public void markFailed(int inspection_id) {
        mSubmitRequestDao.markFailed(inspection_id);
    }
}
