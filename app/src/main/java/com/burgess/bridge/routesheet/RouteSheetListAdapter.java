package com.burgess.bridge.routesheet;

import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import java.util.Collections;
import java.util.List;

import data.Tables.Inspection_Table;

public class RouteSheetListAdapter extends ListAdapter<Inspection_Table, RouteSheetViewHolder> implements ItemTouchHelperAdapter {
    private List<Inspection_Table> currentList;
    private OnStartDragListener mDragListener;

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

        holder.mReorderHandle.setOnTouchListener((v, event) -> {
            Log.d("DRAG", "Action is... " + event.toString());
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDragListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(currentList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(currentList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    public void setCurrentList(List<Inspection_Table> list) {
        currentList = list;
    }

    public void setDragListener(OnStartDragListener listener) {
        mDragListener = listener;
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
