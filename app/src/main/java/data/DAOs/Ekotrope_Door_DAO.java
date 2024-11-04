package data.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import data.Tables.Ekotrope_Door_Table;

@Dao
public interface Ekotrope_Door_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_Door_Table door);

    @Query("DELETE FROM ekotrope_door_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_door_table WHERE plan_id = :plan_id AND [index] = :index")
    void delete(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_door_table WHERE plan_id = :plan_id AND [index] = :index")
    Ekotrope_Door_Table getDoor(String plan_id, int index);

    @Query("SELECT * FROM ekotrope_door_table WHERE plan_id = :plan_id")
    LiveData<List<Ekotrope_Door_Table>> getDoors(String plan_id);

    @Query("SELECT * FROM ekotrope_door_table WHERE plan_id = :plan_id")
    List<Ekotrope_Door_Table> getDoorsSync(String plan_id);

    @Query("SELECT * FROM ekotrope_door_table WHERE plan_id = :plan_id AND isChanged = 1")
    List<Ekotrope_Door_Table> getDoorsForUpdate(String plan_id);

    @Query("UPDATE ekotrope_door_table SET " +
            "name = :name, typeName = :typeName, remove = :remove, installedWallIndex = :installedWallIndex, " +
            "installedFoundationWallIndex = :installedFoundationWallIndex, doorArea = :doorArea, " +
            "uFactor = :uFactor, isChanged = 1 " +
            "WHERE plan_id = :plan_id AND [index] = :index")
    public void update(String plan_id, int index, String name, String typeName, boolean remove,
                       int installedWallIndex, int installedFoundationWallIndex, double doorArea,
                       double uFactor);
}
