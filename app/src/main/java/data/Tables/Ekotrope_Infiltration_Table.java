package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_infiltration_table", primaryKeys = {"plan_id"})
public class Ekotrope_Infiltration_Table {
    @NonNull
    public String plan_id;
    public String infiltration_unit;
    public Double infiltration_value;
    public String measurement_type;
    public boolean is_changed;

    public Ekotrope_Infiltration_Table() {
        plan_id = "";
        is_changed = false;
    }

    public Ekotrope_Infiltration_Table(@NonNull String plan_id, String infiltration_unit,
                                       Double infiltration_value, String measurement_type,
                                       boolean isChanged) {
        this.plan_id = plan_id;
        this.infiltration_unit = infiltration_unit;
        this.infiltration_value = infiltration_value;
        this.measurement_type = measurement_type;
        this.is_changed = isChanged;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("infiltrationUnit", infiltration_unit);
            jsonObj.put("infiltrationValue", infiltration_value);
            jsonObj.put("infiltrationMeasurementType", measurement_type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
