package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.time.OffsetDateTime;
import java.util.Date;
import java.util.List;

import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

@Dao
public interface Inspection_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Inspection_Table inspection);

    @Query("DELETE FROM inspection_table")
    void deleteAll();

    @Query("DELETE FROM inspection_table WHERE InspectionID = :id")
    void delete(int id);

    @Query("UPDATE inspection_table SET " +
            "InspectionTypeID = :inspection_type_id, InspectionDate = :inspection_date, DivisionID = :division_id, LocationID = :location_id, " +
            "BuilderName = :builder_name, BuilderID = :builder_id, SuperName = :super_name, InspectorID = :inspector_id, " +
            "Inspector = :inspector, Community = :community, CommunityID = :community_id, City = :city, InspectionClass = :inspection_class, " +
            "InspectionType = :inspection_type, ReInspect = :reinspect, InspectionOrder = :inspection_order, Address = :address, " +
            "InspectionStatusID = :inspection_status_id, InspectionStatus = :inspection_status, SuperPhone = :super_phone, SuperEmailAddress = :super_email, " +
            "SuperintendentPresent = :super_present, IncompleteReason = :incomplete_reason, IncompleteReasonID = :incomplete_reason_id, Notes = :notes, JobNumber= :job_number, " +
            "RequireRiskAssessment = :require_risk_assessment, EkotropeProjectID = :ekotrope_project_id " +
            "WHERE InspectionID = :id")
    void update(int id, int inspection_type_id, String inspection_date, int division_id, int location_id, String builder_name, int builder_id,
                String super_name, int inspector_id, String inspector, String community, int community_id, String city, int inspection_class,
                String inspection_type, boolean reinspect, int inspection_order, String address, int inspection_status_id, String inspection_status,
                String super_phone, String super_email, int super_present, String incomplete_reason, int incomplete_reason_id, String notes, String job_number,
                boolean require_risk_assessment, String ekotrope_project_id);

    @Query("SELECT * FROM RouteSheet_View " +
            "WHERE InspectorID = :inspector_id " +
            "ORDER BY IsComplete DESC, RouteSheetOrder")
    LiveData<List<RouteSheet_View>> getInspectionsForRouteSheet(int inspector_id);

    @Query("SELECT * FROM inspection_table WHERE InspectionID = :inspection_id")
    LiveData<Inspection_Table> getInspection(int inspection_id);

    @Query("SELECT * FROM inspection_table WHERE InspectionID = :inspection_id")
    Inspection_Table getInspectionSync(int inspection_id);

    @Query("SELECT InspectionID FROM inspection_table WHERE InspectorID = :inspector_id")
    List<Integer> getAllInspectionIds(int inspector_id);

    @Query("SELECT ReInspect FROM inspection_table WHERE InspectionID = :inspection_id")
    boolean getReinspect(int inspection_id);

    @Query("UPDATE inspection_table SET IsComplete = 1, IsFailed = 0, EndTime = :end_time WHERE InspectionID = :inspection_id")
    void completeInspection(OffsetDateTime end_time, int inspection_id);

    @Query("UPDATE inspection_table SET IsFailed = 1 WHERE InspectionID = :inspection_id")
    void markInspectionFailed(int inspection_id);

    @Query("UPDATE inspection_table SET IsUploaded = 1 WHERE InspectionID = :inspection_id")
    void uploadInspection(int inspection_id);

    @Query("UPDATE inspection_table SET RouteSheetOrder = :new_order WHERE InspectionID = :inspection_id")
    void updateRouteSheetIndex(int inspection_id, int new_order);

    @Query("UPDATE inspection_table SET StartTime = :start_time WHERE InspectionID = :inspection_id")
    void startInspection(OffsetDateTime start_time, int inspection_id);

    @Query("SELECT COUNT() FROM inspection_table WHERE IsComplete = 0")
    int getIndividualRemainingInspections();

    @Query("UPDATE inspection_table SET TraineeID = :trainee_id WHERE InspectionID = :inspection_id")
    void assignTrainee(int trainee_id, int inspection_id);

    @Query("UPDATE inspection_table SET EkotropePlanID = :ekotrope_plan_id WHERE InspectionID = :inspection_id")
    void updateEkotropePlanId(String ekotrope_plan_id, int inspection_id);

    @Query("SELECT COUNT() FROM inspection_table WHERE IsUploaded = 0 AND LocationID = :location_id")
    int getPendingMFCInspectionsAtLocation(int location_id);
}