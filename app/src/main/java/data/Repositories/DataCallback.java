package data.Repositories;

import androidx.lifecycle.LiveData;

import data.Tables.Inspection_Table;

public interface DataCallback {
    void onDataLoaded(LiveData<Inspection_Table> data);
}
