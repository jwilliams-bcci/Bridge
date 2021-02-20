package data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "canned_comment_table")
public class CannedComment_Table {
    @PrimaryKey
    public int id;
    public String text;
}
