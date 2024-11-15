package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_Duct_Table;

@Dao
public interface Ekotrope_Duct_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Duct_Table duct);

    @Query("DELETE FROM ekotrope_duct_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_duct_table WHERE plan_id = :plan_id and ds_id = :ds_id and [index] = :index")
    void delete(String plan_id, int ds_id, int index);

    @Query("SELECT * FROM ekotrope_duct_table WHERE plan_id = :plan_id and ds_id = :ds_id and [index] = :index")
    Ekotrope_Duct_Table getDuct(String plan_id, int ds_id, int index);

    @Query("SELECT * FROM ekotrope_duct_table WHERE plan_id = :plan_id and ds_id = :ds_id")
    LiveData<List<Ekotrope_Duct_Table>> getDucts(String plan_id, int ds_id);

    @Query("SELECT * FROM ekotrope_duct_table WHERE plan_id = :plan_id and ds_id = :ds_id")
    List<Ekotrope_Duct_Table> getDuctsSync(String plan_id, int ds_id);

    @Query("SELECT * FROM ekotrope_duct_table WHERE plan_id = :plan_id and ds_id = :ds_id and is_changed = 1")
    List<Ekotrope_Duct_Table> getDuctsForUpdate(String plan_id, int ds_id);

    @Query("UPDATE ekotrope_duct_table SET " +
            "location = :location, percent_supply_area = :percent_supply_area, " +
            "percent_return_area = :percent_return_area, is_changed = 1 " +
            "WHERE plan_id = :plan_id and ds_id = :ds_id and [index] = :index")
    void update(String location, Double percent_supply_area, Double percent_return_area,
                String plan_id, int ds_id, int index);
}
