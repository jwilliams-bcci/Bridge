package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.Repositories.InspectionRepository;
import data.Views.RouteSheet_View;

public class RouteSheetListAdapter extends ListAdapter<RouteSheet_View, RouteSheetViewHolder> implements Filterable, ItemTouchHelperAdapter {
    private List<RouteSheet_View> currentList;
    private List<RouteSheet_View> dataSet = new ArrayList<>();

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
        try {
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
        } catch (Exception e) {
            Log.e("SEARCH", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }

    @Override
    public Filter getFilter() {
        Filter searchFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                constraint = constraint.toString().toLowerCase().trim();
                currentList.clear();

                if (constraint == null || constraint.length() == 0 || constraint == "") {
                    currentList.addAll(dataSet);
                } else {
                    for (RouteSheet_View item : dataSet) {
                        if (item.community.toLowerCase().contains(constraint)) {
                            currentList.add(item);
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = currentList;
                results.count = currentList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.i("SEARCH", "dataSet size: " + dataSet.size() + " currentList size: " + currentList.size());
            }
        };
        return searchFilter;
    }

    public void setCurrentList(List<RouteSheet_View> list) {
        currentList = list;
        dataSet.clear();
        dataSet.addAll(currentList);
        submitList(currentList);
        Log.i("SEARCH", "Size of dataSet: " + dataSet.size());
    }

    public List<RouteSheet_View> getCurrentList() {
        return currentList;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int lcv = fromPosition; lcv < toPosition; lcv++) {
                Collections.swap(currentList, lcv, lcv+1);
            }
        } else {
            for (int lcv = fromPosition; lcv > toPosition; lcv--) {
                Collections.swap(currentList, lcv, lcv-1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
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
