package com.example.bridge.reviewandsubmit;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import data.Tables.InspectionDefect_Table;

public class ReviewAndSubmitListAdapter extends ListAdapter<InspectionDefect_Table, ReviewAndSubmitViewHolder> {

    protected ReviewAndSubmitListAdapter(@NonNull DiffUtil.ItemCallback<InspectionDefect_Table> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public ReviewAndSubmitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ReviewAndSubmitViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAndSubmitViewHolder holder, int position) {
        InspectionDefect_Table current = getItem(position);
        holder.mInspectionDefectId = current.id;
        holder.bind(current.id, current.comment);
    }

    public static class InspectionDefectDiff extends DiffUtil.ItemCallback<InspectionDefect_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull InspectionDefect_Table oldItem, @NonNull InspectionDefect_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull InspectionDefect_Table oldItem, @NonNull InspectionDefect_Table newItem) {
            return oldItem.id == newItem.id;
        }
    }
}
