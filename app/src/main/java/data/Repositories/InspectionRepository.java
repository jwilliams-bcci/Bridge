package data.Repositories;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

    public Inspection_Table getInspectionSync(int inspectionId) {
        return mInspectionDao.getInspectionSync(inspectionId);
    }

    public Inspection_Table threadGetInspectionSync(int inspectionId) throws ExecutionException, InterruptedException {
        Callable<Inspection_Table> callable = () -> mInspectionDao.getInspectionSync(inspectionId);
        Future<Inspection_Table> future = BridgeRoomDatabase.databaseReadExecutor.submit(callable);
        return future.get();
    }

    public LiveData<List<RouteSheet_View>> getAllInspectionsForRouteSheet(int inspector_id) {
        return mInspectionDao.getInspectionsForRouteSheet(inspector_id);
    }

    public List<Integer> getAllInspectionIds(int inspector_id) {
        return mInspectionDao.getAllInspectionIds(inspector_id);
    }

    public List<Integer> threadGetAllInspectionIds(int inspector_id) throws ExecutionException, InterruptedException {
        Callable<List<Integer>> callable = () -> mInspectionDao.getAllInspectionIdsThread(inspector_id);
        Future<List<Integer>> future = BridgeRoomDatabase.databaseReadExecutor.submit(callable);
        return future.get();
    }

    public boolean getReinspect(int inspection_id) {
        return mInspectionDao.getReinspect(inspection_id);
    }

    public void insert(Inspection_Table i) {
        Inspection_Table existingInspection = null;
        try {
            existingInspection = threadGetInspectionSync(i.InspectionID);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (existingInspection != null) {
            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
                mInspectionDao.update(i.InspectionID, i.InspectionTypeID, i.InspectionDate, i.DivisionID, i.LocationID, i.BuilderName, i.BuilderID,
                        i.SuperName, i.InspectorID, i.Inspector, i.Community, i.CommunityID, i.City, i.InspectionClass, i.InspectionType,
                        i.ReInspect, i.InspectionOrder, i.Address, i.InspectionStatusID, i.InspectionStatus, i.SuperPhone, i.SuperEmailAddress,
                        i.SuperintendentPresent, i.IncompleteReason, i.IncompleteReasonID, i.Notes, i.JobNumber, i.RequireRiskAssessment, i.EkotropeProjectID);
            });
        } else {
            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
                mInspectionDao.insert(i);
            });
        }
    }

    public void delete(int inspectionId) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDao.delete(inspectionId);
        });
    }

    public void completeInspection(OffsetDateTime end_time, int inspection_id) {
        mInspectionDao.completeInspection(end_time, inspection_id);
    }

    public void markInspectionFailed(int inspectionId) {
        mInspectionDao.markInspectionFailed(inspectionId);
    }

    public void uploadInspection(int inspection_id) {
        mInspectionDao.uploadInspection(inspection_id);
    }

    public void updateRouteSheetIndex(int inspection_id, int new_order) {
        mInspectionDao.updateRouteSheetIndex(inspection_id, new_order);
    }

    public void updateRouteSheetIndexes(List<RouteSheet_View> items) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDao.updateItemPositions(items);
        });
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

    public void updateEkotropePlanId(String ekotropePlanId, int inspectionId) {
        BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
            mInspectionDao.updateEkotropePlanId(ekotropePlanId, inspectionId);
        });
    }

    public int getPendingMFCInspectionsAtLocation(int locationId) {
        return mInspectionDao.getPendingMFCInspectionsAtLocation(locationId);
    }

    public void clearStatusFlags(int inspectionId) {
        mInspectionDao.clearStatusFlags(inspectionId);
    }

    public void updateJotformAccessed(int inspectionId) {
        mInspectionDao.updateJotformAccessed(inspectionId);
    }
}
