package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_clothes_dryer_table", primaryKeys = {"plan_id"})
public class Ekotrope_ClothesDryer_Table {
    @NonNull
    public String plan_id;
    public boolean available;
    public String defaults_type;
    public Double combined_energy_factor;
    public String utilization_factor;
    public boolean is_changed;

    public Ekotrope_ClothesDryer_Table() {
        plan_id = "";
        available = false;
        is_changed = false;
    }

    public Ekotrope_ClothesDryer_Table(@NonNull String plan_id, boolean available,
                                        String defaults_type, Double combined_energy_factor,
                                        String utilization_factor, boolean is_changed) {
        this.plan_id = plan_id;
        this.available = available;
        this.defaults_type = defaults_type;
        this.combined_energy_factor = combined_energy_factor;
        this.utilization_factor = utilization_factor;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("available", available);
            jsonObject.put("defaultsType", defaults_type);
            jsonObject.put("combinedEnergyFactor", combined_energy_factor);
            jsonObject.put("utilizationFactor", utilization_factor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
