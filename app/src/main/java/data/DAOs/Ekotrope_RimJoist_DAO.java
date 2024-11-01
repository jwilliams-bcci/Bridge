package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_RimJoist_Table;

@Dao
public interface Ekotrope_RimJoist_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_RimJoist_Table rimJoist);

    @Query("DELETE FROM ekotrope_rimjoist_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_rimjoist_table WHERE plan_id = :plan_id AND [index] = :index")
    void deleteByPlanId(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_rimjoist_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_RimJoist_Table getRimJoist(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_rimjoist_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_RimJoist_Table>> getRimJoists(String plan_id);

    @Query("SELECT * FROM ekotrope_rimjoist_table WHERE plan_id = :plan_id")
    List<Ekotrope_RimJoist_Table> getRimJoistsSync(String plan_id);
}
