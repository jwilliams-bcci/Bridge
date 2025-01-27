package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import data.Views.RouteSheet_View;

public class RouteSheetListAdapter extends ListAdapter<RouteSheet_View, RouteSheetViewHolder> implements Filterable {
    public static final String TAG = "ROUTE_SHEET";
    private List<RouteSheet_View> originalList;
    private List<RouteSheet_View> filteredList;

    private final RouteSheetViewHolder.OnItemClickListener itemClickListener;
    private final RouteSheetViewHolder.OnItemButtonClickListener buttonClickListener;

    protected RouteSheetListAdapter(@NonNull InspectionDiff diffCallback,
                                    RouteSheetViewHolder.OnItemClickListener itemClickListener,
                                    RouteSheetViewHolder.OnItemButtonClickListener listener) {
        super(diffCallback);
        this.itemClickListener = itemClickListener;
        this.buttonClickListener = listener;
    }

    @NonNull
    @Override
    public RouteSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_list, parent, false);
        return new RouteSheetViewHolder(view, itemClickListener, buttonClickListener, this);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteSheetViewHolder holder, int position) {
        RouteSheet_View i = getCurrentList().get(position);
        holder.bind(i);
    }

    @Override
    public int getItemCount() {
        return getCurrentList().size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    protected RouteSheet_View getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public Filter getFilter() {
        return new CommunityFilter();
    }

    public void submitOriginalList(List<RouteSheet_View> originalList) {
        this.originalList = originalList;
        submitList(originalList);
    }

    public void moveItem(int fromPosition, int toPosition) {
        List<RouteSheet_View> items = new ArrayList<>(getCurrentList());
        Collections.swap(items, fromPosition, toPosition);
        submitList(items);
    }

    public static class InspectionDiff extends DiffUtil.ItemCallback<RouteSheet_View> {
        @Override
        public boolean areItemsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.InspectionID == newItem.InspectionID;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.NumUploaded == newItem.NumUploaded
                    && oldItem.IsComplete == newItem.IsComplete
                    && oldItem.IsUploaded == newItem.IsUploaded
                    && oldItem.IsFailed == newItem.IsFailed;
        }
    }
    private class CommunityFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // No filter, return original list
                filteredList.addAll(originalList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (RouteSheet_View item : originalList) {
                    if (item.Community.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults filterResults) {
            submitList((List<RouteSheet_View>) filterResults.values);
        }
    }
}
