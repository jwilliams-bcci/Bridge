package com.burgess.bridge.requestqueue;

import static com.burgess.bridge.Constants.INSPECTION_ID;
import static com.burgess.bridge.Constants.INSPECTION_ID_NOT_FOUND;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.R;
import com.burgess.bridge.BridgeLogger;

import java.util.List;

import data.Tables.SubmitRequest_Table;

public class RequestQueueActivity extends AppCompatActivity {
    private RequestQueueViewModel viewModel;
    private ConstraintLayout constraintLayout;
    private RecyclerView recyclerView;

    private int inspectionId;
    private LiveData<List<SubmitRequest_Table>> submitRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_queue);
        setSupportActionBar(findViewById(R.id.request_queue_toolbar));
        viewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RequestQueueViewModel.class);

        // Prepare logger...
        BridgeLogger.getInstance(this);

        // Get intent data...
        Intent intent = getIntent();
        inspectionId = intent.getIntExtra(INSPECTION_ID, INSPECTION_ID_NOT_FOUND);

        initializeViews();
        initializeDisplayContent();
    }

    private void initializeViews() {
        constraintLayout = findViewById(R.id.request_queue_constraint_layout);
        recyclerView = findViewById(R.id.request_queue_recycler);
    }

    private void initializeDisplayContent() {
        submitRequestList = viewModel.getSubmitRequests(inspectionId);
        RequestQueueListAdapter adapter = new RequestQueueListAdapter(new RequestQueueListAdapter.RequestQueueDiff());
        recyclerView.setAdapter(adapter);
        viewModel.getSubmitRequests(inspectionId).observe(this, adapter::setCurrentList);
    }
}