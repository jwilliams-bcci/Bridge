package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_lighting_table", primaryKeys = {"plan_id"})
public class Ekotrope_Lighting_Table {
    @NonNull
    public String plan_id;
    public Double percent_interior_fluorescent;
    public Double percent_interior_led;
    public Double percent_exterior_fluorescent;
    public Double percent_exterior_led;
    public Double percent_garage_fluorescent;
    public Double percent_garage_led;
    public boolean is_changed;

    public Ekotrope_Lighting_Table() {
        plan_id = "";
        is_changed = false;
    }

    public Ekotrope_Lighting_Table(@NonNull String plan_id, Double percent_interior_fluorescent,
                                   Double percent_interior_led, Double percent_exterior_fluorescent,
                                   Double percent_exterior_led, Double percent_garage_fluorescent,
                                   Double percent_garage_led, boolean is_changed) {
        this.plan_id = plan_id;
        this.percent_interior_fluorescent = percent_interior_fluorescent;
        this.percent_interior_led = percent_interior_led;
        this.percent_exterior_fluorescent = percent_exterior_fluorescent;
        this.percent_exterior_led = percent_exterior_led;
        this.percent_garage_fluorescent = percent_garage_fluorescent;
        this.percent_garage_led = percent_garage_led;
        this.is_changed = is_changed;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("percentInteriorFluorescent", percent_interior_fluorescent);
            jsonObject.put("percentInteriorLed", percent_interior_led);
            jsonObject.put("percentExteriorFluorescent", percent_exterior_fluorescent);
            jsonObject.put("percentExteriorLed", percent_exterior_led);
            jsonObject.put("percentGarageFluorescent", percent_garage_fluorescent);
            jsonObject.put("percentGarageLed", percent_garage_led);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
