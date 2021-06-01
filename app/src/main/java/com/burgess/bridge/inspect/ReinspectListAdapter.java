package com.burgess.bridge.inspect;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import org.jetbrains.annotations.NotNull;

import data.Tables.InspectionHistory_Table;

public class ReinspectListAdapter extends ListAdapter<InspectionHistory_Table, InspectViewHolder> {
    private int mInspectionHistoryId;

    protected ReinspectListAdapter(@NonNull @NotNull DiffUtil.ItemCallback<InspectionHistory_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @NotNull
    @Override
    public InspectViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return InspectViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull InspectViewHolder holder, int position) {
        InspectionHistory_Table current = getItem(position);
        boolean showSection = true;
        if (position > 0) {
            InspectionHistory_Table previous = getItem(position - 1);
            if (previous.defect_category_name.equals(current.defect_category_name)) {
                showSection = false;
            } else {
                showSection = true;
            }
        }
    }
}
