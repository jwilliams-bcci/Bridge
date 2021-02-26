package com.example.bridge;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import data.RouteSheet_View;

public class InspectionListAdapter extends ListAdapter<RouteSheet_View, InspectionViewHolder> {
    protected InspectionListAdapter(@NonNull InspectionDiff diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public InspectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return InspectionViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull InspectionViewHolder holder, int position) {
        RouteSheet_View current = getItem(position);
        holder.bind(current.community, current.address, current.inspection_type, current.notes);
    }

    public static class InspectionDiff extends DiffUtil.ItemCallback<RouteSheet_View> {
        @Override
        public boolean areItemsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.inspection_id == newItem.inspection_id;
        }
    }
}
