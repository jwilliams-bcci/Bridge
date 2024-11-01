package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_slab_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_Slab_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String typeName;
    public double underslabInsulationR;
    public boolean isFullyInsulated;
    public double underslabInsulationWidth;
    public double perimeterInsulationDepth;
    public double perimeterInsulationR;
    public boolean thermalBreak;

    public Ekotrope_Slab_Table() {
    }

    public Ekotrope_Slab_Table(String plan_id, int index, String name, String typeName,
                               double underslabInsulationR, boolean isFullyInsulated,
                               double underslabInsulationWidth, double perimeterInsulationDepth,
                               double perimeterInsulationR, boolean thermalBreak) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.typeName = typeName;
        this.underslabInsulationR = underslabInsulationR;
        this.isFullyInsulated = isFullyInsulated;
        this.underslabInsulationWidth = underslabInsulationWidth;
        this.perimeterInsulationDepth = perimeterInsulationDepth;
        this.perimeterInsulationR = perimeterInsulationR;
        this.thermalBreak = thermalBreak;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("index", index);
            jsonObj.put("name", typeName);
            jsonObj.put("underslabInsulationR", underslabInsulationR);
            jsonObj.put("isFullyInsulated", isFullyInsulated);
            jsonObj.put("underslabInsulationWidth", underslabInsulationWidth);
            jsonObj.put("perimeterInsulationDepth", perimeterInsulationDepth);
            jsonObj.put("perimeterInsulationR", perimeterInsulationR);
            jsonObj.put("thermalBreak", thermalBreak);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
