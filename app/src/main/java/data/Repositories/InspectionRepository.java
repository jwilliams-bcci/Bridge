package data.Repositories;

import android.app.Application;
import android.database.sqlite.SQLiteConstraintException;

import androidx.lifecycle.LiveData;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import data.BridgeRoomDatabase;
import data.DAOs.Inspection_DAO;
import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

public class InspectionRepository {
    private Inspection_DAO mInspectionDao;

    public InspectionRepository(Application application) {
        BridgeRoomDatabase db = BridgeRoomDatabase.getDatabase(application);
        mInspectionDao = db.mInspectionDao();
    }

    public LiveData<Inspection_Table> getInspection(int inspection_id) {
        return mInspectionDao.getInspection(inspection_id);
    }

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionDao.getInspectionSync(inspectionId);
    }

    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet(int inspector_id) {
        return mInspectionDao.getInspectionsForRouteSheet(inspector_id);
    }

    public LiveData<List<Inspection_Table>> getAllInspectionsForRouteSheet2(int inspector_id) {
        return mInspectionDao.getInspections(inspector_id);
    }

    public List<Integer> getAllInspectionIds(int inspector_id) {
        return mInspectionDao.getAllInspectionIds(inspector_id);
    }

    public LiveData<Integer> getInspectionTypeId(int inspection_id) {
        return mInspectionDao.getInspectionTypeId(inspection_id);
    }

    public boolean getReinspect(int inspection_id) {
        return mInspectionDao.getReinspect(inspection_id);
    }

    public void insert(Inspection_Table inspection) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            try {
                mInspectionDao.insert(inspection);
            } catch (SQLiteConstraintException e) {
                mInspectionDao.update(inspection.id, inspection.inspection_date, inspection.inspector_id, inspection.inspection_status_id, inspection.incomplete_reason_id);
            }
        });
    }

    public void delete(int inspectionId) {
        mInspectionDao.delete(inspectionId);
    }

    public void completeInspection(OffsetDateTime end_time, int inspection_id) {
        mInspectionDao.completeInspection(end_time, inspection_id);
    }

    public void uploadInspection(int inspection_id) {
        mInspectionDao.uploadInspection(inspection_id);
    }

    public void swapOrder(int inspection_id, int new_order) {
        mInspectionDao.swapOrder(inspection_id, new_order);
    }

    public void startInspection(OffsetDateTime start_time, int inspection_id) {
        mInspectionDao.startInspection(start_time, inspection_id);
    }

    public int getIndividualRemainingInspections() {
        return mInspectionDao.getIndividualRemainingInspections();
    }

    public void assignTrainee(int traineeId, int inspectionId) {
        mInspectionDao.assignTrainee(traineeId, inspectionId);
    }
}
