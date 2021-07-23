package data.Views;

import androidx.room.DatabaseView;

import java.util.Date;

@DatabaseView("SELECT i.id, i.inspector_id, i.inspection_type_id, i.community, i.address, i.inspection_type, i.notes, i.inspection_date, i.is_complete, i.is_uploaded, SUM(CASE WHEN id.is_uploaded = 1 THEN 1 ELSE 0 END) AS num_uploaded, COUNT(id.id) AS num_total " +
                "FROM inspection_table i " +
                "LEFT JOIN inspection_defect_table id ON id.inspection_id = i.id GROUP BY i.id")
public class RouteSheet_View {
    public int id;
    public int inspector_id;
    public int inspection_type_id;
    public String community;
    public String address;
    public String inspection_type;
    public String notes;
    public Date inspection_date;
    public boolean is_complete;
    public boolean is_uploaded;
    public int num_uploaded;
    public int num_total;
}
