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
    public Double underslabInsulationR;
    public boolean isFullyInsulated;
    public Double underslabInsulationWidth;
    public Double perimeterInsulationDepth;
    public Double perimeterInsulationR;
    public boolean thermalBreak;
    public boolean isChanged;

    public Ekotrope_Slab_Table() {
    }

    public Ekotrope_Slab_Table(String plan_id, int index, String name, String typeName,
                               Double underslabInsulationR, boolean isFullyInsulated,
                               Double underslabInsulationWidth, Double perimeterInsulationDepth,
                               Double perimeterInsulationR, boolean thermalBreak,
                               boolean isChanged) {
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
        this.isChanged = isChanged;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("index", index);
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
