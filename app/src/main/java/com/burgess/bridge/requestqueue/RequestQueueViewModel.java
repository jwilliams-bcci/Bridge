package com.burgess.bridge.requestqueue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import data.Repositories.SubmitRequestRepository;
import data.Tables.SubmitRequest_Table;

public class RequestQueueViewModel extends AndroidViewModel {
    private final SubmitRequestRepository submitRequestRepository;

    public RequestQueueViewModel(@NonNull Application application) {
        super(application);
        submitRequestRepository = new SubmitRequestRepository(application);
    }

    public LiveData<List<SubmitRequest_Table>> getSubmitRequests(int inspectionId) {
        return submitRequestRepository.getSubmitRequests(inspectionId);
    }
}