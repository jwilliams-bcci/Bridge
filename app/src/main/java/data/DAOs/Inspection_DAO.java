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

    @Query("DELETE FROM inspection_table WHERE id = :id")
    void delete(int id);

    @Query("UPDATE inspection_table SET " +
            "inspection_type_id = :inspection_type_id, inspection_date = :inspection_date, division_id = :division_id, location_id = :location_id, " +
            "builder_name = :builder_name, builder_id = :builder_id, super_name = :super_name, inspector_id = :inspector_id, " +
            "inspector = :inspector, community = :community, community_id = :community_id, city = :city, inspection_class = :inspection_class, " +
            "inspection_type = :inspection_type, reinspect = :reinspect, inspection_order = :inspection_order, address = :address, " +
            "inspection_status_id = :inspection_status_id, inspection_status = :inspection_status, super_phone = :super_phone, super_email = :super_email, " +
            "super_present = :super_present, incomplete_reason = :incomplete_reason, incomplete_reason_id = :incomplete_reason_id, notes = :notes " +
            "WHERE id = :id")
    void update(int id, int inspection_type_id, OffsetDateTime inspection_date, int division_id, int location_id, String builder_name, int builder_id,
                String super_name, int inspector_id, String inspector, String community, int community_id, String city, int inspection_class,
                String inspection_type, boolean reinspect, int inspection_order, String address, int inspection_status_id, String inspection_status,
                String super_phone, String super_email, int super_present, String incomplete_reason, int incomplete_reason_id, String notes);

    @Query("SELECT * FROM RouteSheet_View " +
            "WHERE inspector_id = :inspector_id " +
            "ORDER BY is_complete DESC, route_sheet_order")
    LiveData<List<RouteSheet_View>> getInspectionsForRouteSheet(int inspector_id);

    @Query("SELECT * FROM inspection_table WHERE id = :inspection_id")
    LiveData<Inspection_Table> getInspection(int inspection_id);

    @Query("SELECT * FROM inspection_table WHERE id = :inspection_id")
    Inspection_Table getInspectionSync(int inspection_id);

    @Query("SELECT id FROM inspection_table WHERE inspector_id = :inspector_id")
    List<Integer> getAllInspectionIds(int inspector_id);

    @Query("SELECT reinspect FROM inspection_table WHERE id = :inspection_id")
    boolean getReinspect(int inspection_id);

    @Query("UPDATE inspection_table SET is_complete = 1, is_failed = 0, end_time = :end_time WHERE id = :inspection_id")
    void completeInspection(OffsetDateTime end_time, int inspection_id);

    @Query("UPDATE inspection_table SET is_failed = 1 WHERE id = :inspection_id")
    void markInspectionFailed(int inspection_id);

    @Query("UPDATE inspection_table SET is_uploaded = 1 WHERE id = :inspection_id")
    void uploadInspection(int inspection_id);

    @Query("UPDATE inspection_table SET route_sheet_order = :new_order WHERE id = :inspection_id")
    void updateRouteSheetIndex(int inspection_id, int new_order);

    @Query("UPDATE inspection_table SET start_time = :start_time WHERE id = :inspection_id")
    void startInspection(OffsetDateTime start_time, int inspection_id);

    @Query("SELECT COUNT() FROM inspection_table WHERE is_complete = 0")
    int getIndividualRemainingInspections();

    @Query("UPDATE inspection_table SET trainee_id = :trainee_id WHERE id = :inspection_id")
    void assignTrainee(int trainee_id, int inspection_id);
}