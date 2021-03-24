package com.example.bridge.routesheet;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.ListAdapter;

import com.example.bridge.ItemTouchHelperAdapter;
import com.example.bridge.OnStartDragListener;

import data.Tables.Inspection_Table;

public class RouteSheetListAdapter extends ListAdapter<Inspection_Table, RouteSheetViewHolder> implements ItemTouchHelperAdapter {

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
        holder.mInspectionTypeId = current.inspection_type_id;
        holder.bind(current.community, current.address, current.inspection_type, current.notes);
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {

    }

    @Override
    public void onItemDismiss(int position) {

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
