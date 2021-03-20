package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.InspectionDefect_DAO;
import data.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;

public class InspectionDefectRepository {
    private InspectionDefect_DAO mInspectionDefectDao;

    public InspectionDefectRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionDefectDao = db.mInspectionDefectDao();
    }

    public LiveData<InspectionDefect_Table> getInspectionDefect(int inspection_defect_id) {
        return mInspectionDefectDao.getInspectionDefect(inspection_defect_id);
    }

    public LiveData<List<InspectionDefect_Table>> getAllInspectionDefects(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefects(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReview(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReview(inspection_id);
    }

    public void insert(InspectionDefect_Table inspectionDefect) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDefectDao.insert(inspectionDefect);
        });
    }
}
