package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_duct_table", primaryKeys = {"plan_id", "ds_id", "index"})
public class Ekotrope_Duct_Table {
    @NonNull
    public String plan_id;
    public int ds_id;
    public int index;
    public String location;
    public Double percent_supply_area;
    public Double percent_return_area;
    public boolean is_changed;

    public Ekotrope_Duct_Table() {
        plan_id = "";
        ds_id = -1;
        index = -1;
        is_changed = false;
    }

    public Ekotrope_Duct_Table(@NonNull String plan_id, int ds_id, int index, String location,
                               Double percent_supply_area, Double percent_return_area, boolean is_changed) {
        this.plan_id = plan_id;
        this.ds_id = ds_id;
        this.index = index;
        this.location = location;
        this.percent_supply_area = percent_supply_area;
        this.percent_return_area = percent_return_area;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dustLocation", location);
            jsonObject.put("percentSupplyArea", percent_supply_area);
            jsonObject.put("percentReturnArea", percent_return_area);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
