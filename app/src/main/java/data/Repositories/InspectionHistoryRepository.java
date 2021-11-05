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

    public InspectionHistory_Table getInspectionHistorySync(int inspection_history_id) {
        return mInspectionHistoryDao.getInspectionHistorySync(inspection_history_id);
    }

    public void insert(InspectionHistory_Table inspectionHistory) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionHistoryDao.insert(inspectionHistory);
        });
        // TODO: fix updating inspection histories for voided inspections
//        InspectionHistory_Table existingInspectionHistory = getInspectionHistorySync(inspectionHistory.id);
//        if (existingInspectionHistory != null) {
//            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
//                mInspectionHistoryDao.update(existingInspectionHistory.id, inspectionHistory.inspection_id);
//            });
//        } else {
//            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
//                mInspectionHistoryDao.insert(inspectionHistory);
//            });
//        }
    }

    public String getComment(int inspectionHistoryId) {
        return mInspectionHistoryDao.getComment(inspectionHistoryId);
    }

    public void updateIsReviewed(int inspectionHistoryId) {
        mInspectionHistoryDao.updateIsReviewed(inspectionHistoryId);
    }

    public void updateReviewedStatus(int defectStatusId, int inspectionHistoryId) {
        mInspectionHistoryDao.updateReviewedStatus(defectStatusId, inspectionHistoryId);
    }

    public void updateInspectionDefectId(int inspectionDefectId, int inspectionHistoryId) {
        mInspectionHistoryDao.updateInspectionDefectId(inspectionDefectId, inspectionHistoryId);
    }

    public int getItemsToReview(int inspectionId) {
        return mInspectionHistoryDao.getItemsToReview(inspectionId);
    }
}
