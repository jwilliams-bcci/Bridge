package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_refrigerator_table", primaryKeys = {"plan_id"})
public class Ekotrope_Refrigerator_Table {
    @NonNull
    public String plan_id;
    public Double refrigerator_consumption;
    public boolean is_changed;

    public Ekotrope_Refrigerator_Table() {
        plan_id = "";
        is_changed = false;
    }

    public Ekotrope_Refrigerator_Table(@NonNull String plan_id, Double refrigerator_consumption,
                                       boolean is_changed) {
        this.plan_id = plan_id;
        this.refrigerator_consumption = refrigerator_consumption;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (is_changed) {
                jsonObject.put("refrigeratorConsumption", refrigerator_consumption);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
        }
}
