package data.Tables;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "canned_comment_table")
public class CannedComment_Table {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String text;
    public int isEnergy;

    public CannedComment_Table() {}

    public CannedComment_Table(String text, int isEnergy) {
        this.text = text;
        this.isEnergy = isEnergy;
    }
}