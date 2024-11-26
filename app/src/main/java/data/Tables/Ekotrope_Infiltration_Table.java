package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_infiltration_table", primaryKeys = {"plan_id"})
public class Ekotrope_Infiltration_Table {
    @NonNull
    public String plan_id;
    public Double cfm_50;
    public Double ach_50;
    public String measurement_type;
    public boolean is_changed;

    public Ekotrope_Infiltration_Table() {
        plan_id = "";
        is_changed = false;
    }

    public Ekotrope_Infiltration_Table(@NonNull String plan_id, Double cfm_50,
                                       Double ach_50, String measurement_type,
                                       boolean isChanged) {
        this.plan_id = plan_id;
        this.cfm_50 = cfm_50;
        this.ach_50 = ach_50;
        this.measurement_type = measurement_type;
        this.is_changed = isChanged;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            if (is_changed) {
                jsonObj.put("infiltrationUnit", "CFM_50");
                jsonObj.put("infiltrationValue", cfm_50);
                jsonObj.put("infiltrationMeasurementType", measurement_type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
