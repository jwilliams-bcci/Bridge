package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_Refrigerator_Table;

@Dao
public interface Ekotrope_Refrigerator_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Refrigerator_Table refrigerator);

    @Query("DELETE FROM ekotrope_refrigerator_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_refrigerator_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_refrigerator_table WHERE plan_id = :plan_id")
    Ekotrope_Refrigerator_Table getRefrigerator(String plan_id);

    @Query("UPDATE ekotrope_refrigerator_table SET " +
            "refrigerator_consumption = :refrigerator_consumption, is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, Double refrigerator_consumption);
}
