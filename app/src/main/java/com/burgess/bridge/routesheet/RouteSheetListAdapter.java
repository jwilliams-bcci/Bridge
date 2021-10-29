package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import data.Views.RouteSheet_View;

public class RouteSheetListAdapter extends ListAdapter<RouteSheet_View, RouteSheetViewHolder> implements ItemTouchHelperAdapter, Filterable {
    private List<RouteSheet_View> currentList;
    private List<RouteSheet_View> dataSet;
    private OnDragListener mDragListener;

    protected RouteSheetListAdapter(@NonNull InspectionDiff diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RouteSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return RouteSheetViewHolder.create(parent);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull RouteSheetViewHolder holder, int position) {
        RouteSheet_View current = getItem(position);
        CardView view = holder.itemView.findViewById(R.id.item_inspection_list_card_view);
        holder.mInspectionId = current.id;
        holder.mInspectionTypeId = current.inspection_type_id;
        holder.isComplete = current.is_complete;
        holder.isReinspection = current.reinspect;
        holder.numberUploaded = current.num_uploaded;
        holder.numberToUpload = current.num_total;
        if (current.is_complete) {
            view.setCardBackgroundColor(Color.YELLOW);
        }
        holder.bind(current.community, current.address, current.inspection_type, current.notes);

        holder.mReorderHandle.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDragListener.onStartDrag(holder);
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return currentList == null ? 0 : dataSet.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        System.out.println("moved");

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

    @Override
    public Filter getFilter() {
        Filter searchFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<RouteSheet_View> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(currentList);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    for (RouteSheet_View item : currentList) {
                        if (item.community.toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (currentList != null && results.values != null) {
                    dataSet.clear();
                    dataSet.addAll((Collection<? extends RouteSheet_View>) results.values);
                    notifyDataSetChanged();
                }
            }
        };
        System.out.println(searchFilter.toString());
        return searchFilter;
    }

    public void setCurrentList(List<RouteSheet_View> list) {
        dataSet = list;
    }

    public void setDragListener(OnDragListener listener) {
        mDragListener = listener;
    }

    public static class InspectionDiff extends DiffUtil.ItemCallback<RouteSheet_View> {
        @Override
        public boolean areItemsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            System.out.println("areItemsTheSame: " + oldItem.address + " -- " + newItem.address + " " + (oldItem.id == newItem.id));
            return oldItem.id == newItem.id;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            System.out.println("areContentsTheSame: " + oldItem.address + " -- " + newItem.address + " " + (oldItem.num_uploaded == newItem.num_uploaded));
            return oldItem.num_uploaded == newItem.num_uploaded;
        }
    }
}
