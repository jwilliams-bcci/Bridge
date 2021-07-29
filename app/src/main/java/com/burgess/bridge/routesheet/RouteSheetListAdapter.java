package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.util.Collections;
import java.util.List;

import data.Views.RouteSheet_View;

public class RouteSheetListAdapter extends ListAdapter<RouteSheet_View, RouteSheetViewHolder> implements ItemTouchHelperAdapter {
    private List<RouteSheet_View> currentList;
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
        RouteSheet_View current = getItem(position);
        CardView view = holder.itemView.findViewById(R.id.item_inspection_list_card_view);
        ImageView imageReinspect = holder.itemView.findViewById(R.id.item_inspection_list_imageview_reinspection);
        holder.mInspectionId = current.id;
        holder.mInspectionTypeId = current.inspection_type_id;
        holder.isComplete = current.is_complete;
        holder.isReinspection = current.reinspect;
        holder.numberUploaded = current.num_uploaded;
        holder.numberToUpload = current.num_total;
        if (current.is_complete) {
            view.setCardBackgroundColor(Color.YELLOW);
        }
        if (current.reinspect) {
            imageReinspect.setVisibility(View.VISIBLE);
        }
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

    public void setCurrentList(List<RouteSheet_View> list) {
        currentList = list;
    }

    public void setDragListener(OnStartDragListener listener) {
        mDragListener = listener;
    }

    public static class InspectionDiff extends DiffUtil.ItemCallback<RouteSheet_View> {
        @Override
        public boolean areItemsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.id == newItem.id;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem == newItem;
        }
    }
}
