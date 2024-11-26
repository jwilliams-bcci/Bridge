package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_range_oven_table", primaryKeys = {"plan_id"})
public class Ekotrope_RangeOven_Table {
    @NonNull
    public String plan_id;
    public String fuel_type;
    public boolean is_induction_range;
    public boolean is_convection_oven;
    public boolean is_changed;

    public Ekotrope_RangeOven_Table() {
        plan_id = "";
        is_induction_range = false;
        is_convection_oven = false;
        is_changed = false;
    }

    public Ekotrope_RangeOven_Table(@NonNull String plan_id, String fuel_type,
                                    boolean is_induction_range, boolean is_convection_oven,
                                    boolean is_changed) {
        this.plan_id = plan_id;
        this.fuel_type = fuel_type;
        this.is_induction_range = is_induction_range;
        this.is_convection_oven = is_convection_oven;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (is_changed) {
                jsonObject.put("fuelType", fuel_type);
                jsonObject.put("isInductionRange", is_induction_range);
                jsonObject.put("isConvectionOven", is_convection_oven);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
