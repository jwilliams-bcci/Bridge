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
    public int installedWallIndex;
    public int installedFoundationWallIndex;
    public double doorArea;
    public double uFactor;

    public Ekotrope_Door_Table() {
    }

    public Ekotrope_Door_Table(String plan_id, int index, String name, String typeName, boolean remove,
                               int installedWallIndex, int installedFoundationWallIndex,
                               double doorArea, double uFactor) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.typeName = typeName;
        this.remove = remove;
        this.installedWallIndex = installedWallIndex;
        this.installedFoundationWallIndex = installedFoundationWallIndex;
        this.doorArea = doorArea;
        this.uFactor = uFactor;
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
