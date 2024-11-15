package data.DAOs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import data.Tables.Ekotrope_ClothesWasher_Table;

@Dao
public interface Ekotrope_ClothesWasher_DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Ekotrope_ClothesWasher_Table washer);

    @Query("DELETE FROM ekotrope_clothes_washer_table")
    void deleteAll();

    @Query("DELETE FROM ekotrope_clothes_washer_table WHERE plan_id = :plan_id")
    void delete(String plan_id);

    @Query("SELECT * FROM ekotrope_clothes_washer_table WHERE plan_id = :plan_id")
    Ekotrope_ClothesWasher_Table getClothesWasher(String plan_id);

    @Query("UPDATE ekotrope_clothes_washer_table SET " +
            "available = :available, " +
            "defaults_type = :defaults_type, " +
            "load_type = :load_type, " +
            "labeled_energy_rating = :labeled_energy_rating, " +
            "integrated_modified_energy_factor = :integrated_modified_energy_factor, " +
            "is_changed = 1 " +
            "WHERE plan_id = :plan_id")
    void update(String plan_id, boolean available, String defaults_type,
                String load_type, Double labeled_energy_rating,
                Double integrated_modified_energy_factor);
}
