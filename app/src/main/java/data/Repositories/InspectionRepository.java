package data.Repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.time.OffsetDateTime;
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

    public List<Integer> getAllInspectionIds(int inspector_id) {
        return mInspectionDao.getAllInspectionIds(inspector_id);
    }

    public boolean getReinspect(int inspection_id) {
        return mInspectionDao.getReinspect(inspection_id);
    }

    public void insert(Inspection_Table i) {
        Inspection_Table existingInspection = getInspectionSync(i.id);
        if (existingInspection != null) {
            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
                mInspectionDao.update(i.id, i.inspection_type_id, i.inspection_date, i.division_id, i.location_id, i.builder_name, i.builder_id,
                        i.super_name, i.inspector_id, i.inspector, i.community, i.community_id, i.city, i.inspection_class, i.inspection_type,
                        i.reinspect, i.inspection_order, i.address, i.inspection_status_id, i.inspection_status, i.super_phone, i.super_email,
                        i.super_present, i.incomplete_reason, i.incomplete_reason_id, i.notes);
            });
        } else {
            BridgeRoomDatabase.databaseWriteExecutor.execute(() -> {
                mInspectionDao.insert(i);
            });
        }
    }

    public void delete(int inspectionId) {
        mInspectionDao.delete(inspectionId);
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
