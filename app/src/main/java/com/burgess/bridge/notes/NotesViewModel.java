package com.burgess.bridge.notes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import java.util.List;

import data.Repositories.InspectionHistoryRepository;
import data.Tables.InspectionHistory_Table;

public class NotesViewModel extends AndroidViewModel {
    private static final String TAG = "NOTES_VIEW_MODEL";
    private final InspectionHistoryRepository inspectionHistoryRepository;

    public NotesViewModel(@NonNull Application application) {
        super(application);
        inspectionHistoryRepository = new InspectionHistoryRepository(application);
    }

    //region Database Get Calls
    public List<InspectionHistory_Table> getNotes(int inspectionId) {
        return inspectionHistoryRepository.getNotes(inspectionId);
    }
    //endregion
}
