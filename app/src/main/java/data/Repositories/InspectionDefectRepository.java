package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import data.BridgeRoomDatabase;
import data.DAOs.InspectionDefect_DAO;
import data.Views.ReviewAndSubmit_View;
import data.Tables.InspectionDefect_Table;

public class InspectionDefectRepository {
    private InspectionDefect_DAO mInspectionDefectDao;

    public InspectionDefectRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionDefectDao = db.mInspectionDefectDao();
    }

    public InspectionDefect_Table getInspectionDefect(int inspection_defect_id) {
        return mInspectionDefectDao.getInspectionDefect(inspection_defect_id);
    }

    public int getExistingMFCDefect(int first_inspection_detail_id, int inspection_id) {
        return mInspectionDefectDao.getExistingMFCDefect(first_inspection_detail_id, inspection_id);
    }

    public void updateExistingMFCDefect(int defect_status_id, String comment, int id) {
        mInspectionDefectDao.updateExistingMFCDefect(defect_status_id, comment, id);
    }

    public LiveData<List<InspectionDefect_Table>> getAllInspectionDefects(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefects(inspection_id);
    }

    public List<InspectionDefect_Table> getAllInspectionDefectsSync(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsSync(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortAsc(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewDescriptionSortAsc(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDescriptionSortDesc(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewDescriptionSortDesc(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortAsc(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewItemNumberSortAsc(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewItemNumberSortDesc(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewItemNumberSortDesc(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDefectIDSortAsc(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewDefectIDSortAsc(inspection_id);
    }

    public LiveData<List<ReviewAndSubmit_View>> getInspectionDefectsForReviewDefectIDSortDesc(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewDefectIDSortDesc(inspection_id);
    }

    public List<ReviewAndSubmit_View> getInspectionDefectsForReviewSync(int inspection_id) {
        return mInspectionDefectDao.getInspectionDefectsForReviewSync(inspection_id);
    }

    public List<InspectionDefect_Table> getReinspectionRequiredDefects(int inspection_id) {
        return mInspectionDefectDao.getReinspectionRequiredDefects(inspection_id);
    }

    public long insert(InspectionDefect_Table inspectionDefect) {
        AtomicLong id = new AtomicLong();
        return mInspectionDefectDao.insert(inspectionDefect);
    }

    public void delete(int inspection_id) {
        mInspectionDefectDao.delete(inspection_id);
    }

    public void updateInspectionDefect(InspectionDefect_Table inspectionDefect) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDefectDao.update(inspectionDefect);
        });
    }

    public void markDefectUploaded(int inspectionDefectId) {
        mInspectionDefectDao.markDefectUploaded(inspectionDefectId);
    }

    public int remainingToUpload(int inspectionId) {
        return mInspectionDefectDao.remainingToUpload(inspectionId);
    }

    public void deleteInspectionDefect(int inspectionDefectId) {
        mInspectionDefectDao.deleteInspectionDefect(inspectionDefectId);
    }

    public int getInspectionDefectCount(int inspectionId) {
        return mInspectionDefectDao.getInspectionDefectCount(inspectionId);
    }
}
