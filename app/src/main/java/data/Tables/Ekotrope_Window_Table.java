package data.Tables;

import androidx.annotation.NonNull;
import androidx.room.Entity;

import org.json.JSONObject;

@Entity(tableName = "ekotrope_window_table", primaryKeys = {"plan_id", "index"})
public class Ekotrope_Window_Table {
    @NonNull
    public String plan_id;
    public int index;
    public String name;
    public String typeName;
    public boolean remove;
    public double windowArea;
    public String orientation;
    public int installedWallIndex;
    public int installedFoundationWallIndex;
    public double overhangDepth;
    public double distanceOverhangToTop;
    public double distanceOverhangToBottom;
    public double SHGC;
    public double uFactor;
    public double adjacentSummerShading;
    public double adjacentWinterShading;
    public boolean isChanged;

    public Ekotrope_Window_Table() {
    }

    public Ekotrope_Window_Table(String plan_id, int index, String name, String typeName, boolean remove,
                                 double windowArea, String orientation, int installedWallIndex,
                                 int installedFoundationWallIndex, double overhangDepth,
                                 double distanceOverhangToTop, double distanceOverhangToBottom,
                                 double SHGC, double uFactor, double adjacentSummerShading,
                                 double adjacentWinterShading, boolean isChanged) {
        this.plan_id = plan_id;
        this.index = index;
        this.name = name;
        this.typeName = typeName;
        this.remove = remove;
        this.windowArea = windowArea;
        this.orientation = orientation;
        this.installedWallIndex = installedWallIndex;
        this.installedFoundationWallIndex = installedFoundationWallIndex;
        this.overhangDepth = overhangDepth;
        this.distanceOverhangToTop = distanceOverhangToTop;
        this.distanceOverhangToBottom = distanceOverhangToBottom;
        this.SHGC = SHGC;
        this.uFactor = uFactor;
        this.adjacentSummerShading = adjacentSummerShading;
        this.adjacentWinterShading = adjacentWinterShading;
        this.isChanged = isChanged;
    }

    public JSONObject toJsonObj() {
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("index", index);
            jsonObj.put("remove", remove);
            jsonObj.put("windowArea", windowArea);
            jsonObj.put("orientation", orientation);
            jsonObj.put("installedWallIndex", installedWallIndex);
            jsonObj.put("overhangDepth", overhangDepth);
            jsonObj.put("distanceOverhangToTop", distanceOverhangToTop);
            jsonObj.put("distanceOverhangToBottom", distanceOverhangToBottom);
            jsonObj.put("SHGC", SHGC);
            jsonObj.put("uFactor", uFactor);
            jsonObj.put("adjacentSummerShading", adjacentSummerShading);
            jsonObj.put("adjacentWinterShading", adjacentWinterShading);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObj;
    }
}
