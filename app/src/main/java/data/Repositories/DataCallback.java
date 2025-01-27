package data.Repositories;

import androidx.lifecycle.LiveData;

import java.util.List;

import data.Tables.Inspection_Table;

public interface DataCallback {
    void onDataLoaded(List<Integer> data);
    void onError(Exception e);
}
