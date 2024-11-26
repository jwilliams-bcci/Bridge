package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_clothes_washer_table", primaryKeys = {"plan_id"})
public class Ekotrope_ClothesWasher_Table {
    @NonNull
    public String plan_id;
    public boolean available;
    public String defaults_type;
    public String load_type;
    public Double labeled_energy_rating;
    public Double integrated_modified_energy_factor;
    public boolean is_changed;

    public Ekotrope_ClothesWasher_Table() {
        plan_id = "";
        available = false;
        is_changed = false;
    }

    public Ekotrope_ClothesWasher_Table(@NonNull String plan_id, boolean available,
                                        String defaults_type, String load_type,
                                        Double labeled_energy_rating,
                                        Double integrated_modified_energy_factor,
                                        boolean is_changed) {
        this.plan_id = plan_id;
        this.available = available;
        this.defaults_type = defaults_type;
        this.load_type = load_type;
        this.labeled_energy_rating = labeled_energy_rating;
        this.integrated_modified_energy_factor = integrated_modified_energy_factor;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (is_changed) {
                jsonObject.put("available", available);
                jsonObject.put("defaultsType", defaults_type);
                jsonObject.put("loadType", load_type);
                jsonObject.put("labeledEnergyRating", labeled_energy_rating);
                jsonObject.put("integratedModifiedEnergyFactor", integrated_modified_energy_factor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
