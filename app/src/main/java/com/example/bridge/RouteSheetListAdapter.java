package com.example.bridge;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import data.RouteSheet_View;
import data.Tables.Inspection_Table;

public class RouteSheetListAdapter extends ListAdapter<Inspection_Table, RouteSheetViewHolder> {
    protected RouteSheetListAdapter(@NonNull InspectionDiff diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RouteSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RouteSheetViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteSheetViewHolder holder, int position) {
        Inspection_Table current = getItem(position);
        holder.mInspectionId = current.id;
        holder.bind(current.community, current.address, current.inspection_type, current.notes);
    }

    public static class InspectionDiff extends DiffUtil.ItemCallback<Inspection_Table> {
        @Override
        public boolean areItemsTheSame(@NonNull Inspection_Table oldItem, @NonNull Inspection_Table newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Inspection_Table oldItem, @NonNull Inspection_Table newItem) {
            return oldItem.id == newItem.id;
        }
    }
}
