package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_door_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_Door_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String typeName;
    public boolean remove;
    public Integer installedWallIndex;
    public Integer installedFoundationWallIndex;
    public Double doorArea;
    public Double uFactor;
    public boolean isChanged;

    public Ekotrope_Door_Table() {
    }

    public Ekotrope_Door_Table(String plan_id, int index, String name, String typeName, boolean remove,
                               Integer installedWallIndex, Integer installedFoundationWallIndex,
                               Double doorArea, Double uFactor, boolean isChanged) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.typeName = typeName;
        this.remove = remove;
        this.installedWallIndex = installedWallIndex;
        this.installedFoundationWallIndex = installedFoundationWallIndex;
        this.doorArea = doorArea;
        this.uFactor = uFactor;
        this.isChanged = isChanged;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("index", index);
            jsonObj.put("remove", remove);
            jsonObj.put("installedWallIndex", installedWallIndex);
            jsonObj.put("doorArea", doorArea);
            jsonObj.put("uFactor", uFactor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
