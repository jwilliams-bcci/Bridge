package data.Views;

import androidx.room.DatabaseView;

import java.time.OffsetDateTime;
import java.util.Date;

@DatabaseView("SELECT i.id, i.inspector_id, i.inspection_type_id, i.inspection_status_id, i.reinspect, " +
                "i.community, i.address, i.inspection_type, i.notes, i.job_number, i.require_risk_assessment, " +
                "i.ekotrope_project_id, i.inspection_date, i.is_complete, i.is_failed, i.is_uploaded, " +
                "SUM(CASE WHEN id.is_uploaded = 1 THEN 1 ELSE 0 END) AS num_uploaded, COUNT(id.id) AS num_total, route_sheet_order " +
                "FROM inspection_table i " +
                "LEFT JOIN inspection_defect_table id ON id.inspection_id = i.id GROUP BY i.id")
public class RouteSheet_View {
    public int id;
    public int inspector_id;
    public int inspection_type_id;
    public int inspection_status_id;
    public boolean reinspect;
    public String community;
    public String address;
    public String inspection_type;
    public String notes;
    public String job_number;
    public boolean require_risk_assessment;
    public String ekotrope_project_id;
    public OffsetDateTime inspection_date;
    public boolean is_complete;
    public boolean is_failed;
    public boolean is_uploaded;
    public int num_uploaded;
    public int num_total;
    public int route_sheet_order;
}
