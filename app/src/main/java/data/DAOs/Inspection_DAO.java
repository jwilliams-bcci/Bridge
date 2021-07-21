package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Inspection_Table;
import data.Views.RouteSheet_View;

@Dao
public interface Inspection_DAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Inspection_Table inspection);

    @Query("DELETE FROM inspection_table")
    void deleteAll();

    @Query("SELECT * FROM inspection_table WHERE is_complete = 0 AND inspector_id = :inspector_id ORDER BY route_sheet_order ASC")
    LiveData<List<Inspection_Table>> getInspections(int inspector_id);

    @Query("SELECT * FROM routesheet_view")
    LiveData<List<RouteSheet_View>> getInspectionsForRouteSheet();

    @Query("SELECT * FROM inspection_table WHERE id = :inspection_id")
    LiveData<Inspection_Table> getInspection(int inspection_id);

    @Query("SELECT inspection_type_id FROM inspection_table WHERE id = :inspection_id")
    LiveData<Integer> getInspectionTypeId(int inspection_id);

    @Query("SELECT reinspect FROM inspection_table WHERE id = :inspection_id")
    boolean getReinspect(int inspection_id);

    @Query("UPDATE inspection_table SET is_complete = 1 WHERE id = :inspection_id")
    void completeInspection(int inspection_id);

    //TODO: implement query to reorder the route sheet order based on drag and drop
    @Query("UPDATE inspection_Table SET route_sheet_order = :new_order WHERE id = :inspection_id")
    void swapOrder(int inspection_id, int new_order);
}
