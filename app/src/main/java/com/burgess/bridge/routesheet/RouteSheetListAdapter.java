package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.inspectiondetails.InspectionDetailsActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import data.Repositories.InspectionRepository;
import data.Views.RouteSheet_View;

public class RouteSheetListAdapter extends ListAdapter<RouteSheet_View, RouteSheetViewHolder> implements Filterable, ItemTouchHelperAdapter {
    public static final String TAG = "ROUTE_SHEET";

    private List<RouteSheet_View> currentList;
    private List<RouteSheet_View> dataSet = new ArrayList<>();

    protected RouteSheetListAdapter(@NonNull InspectionDiff diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public RouteSheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inspection_list, parent, false);
        return new RouteSheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RouteSheetViewHolder holder, int position) {
        try {
            RouteSheet_View i = currentList.get(position);

            // Set text fields...
            String addressDisplay;
            if (i.job_number.toLowerCase().startsWith("lot")) {
                addressDisplay = i.address + " - " + i.job_number;
            } else {
                addressDisplay = i.address;
            }
            holder.getTextInspectionAddress().setText(addressDisplay);
            holder.getTextInspectionCommunity().setText(i.community);
            holder.getTextInspectionType().setText(i.inspection_type);
            holder.getCardView().setBackgroundColor(Color.WHITE);
            holder.getTextInspectionUploaded().setVisibility(View.GONE);
            holder.getImageViewReupload().setVisibility(View.GONE);
            holder.getTextCallbackNotes().setVisibility(View.GONE);
            holder.getImageViewNoteAttached().setVisibility(View.GONE);

            // If the inspection is complete, set the color to yellow and show other view components
            if (i.is_complete) {
                holder.getCardView().setBackgroundColor(Color.YELLOW);
                holder.getTextInspectionUploaded().setVisibility(View.VISIBLE);
                holder.getTextInspectionUploaded().setText(String.format(Locale.US, "Uploaded %d of %d items.", i.num_uploaded, i.num_total));
                holder.getImageViewReupload().setVisibility(View.VISIBLE);
            }

            // If the inspection upload failed, set the color to red and show reupload icon
            if (i.is_failed) {
                holder.getCardView().setBackgroundColor(Color.RED);
                holder.getImageViewReupload().setVisibility(View.VISIBLE);
            }

            // If the inspection is complete, set the color to green
            if (i.is_uploaded) {
                holder.getCardView().setBackgroundColor(Color.GREEN);
                holder.getImageViewReupload().setVisibility(View.GONE);
            }

            // If there's a note, show the note icon
            if (!i.notes.equals("null")) {
                holder.getImageViewNoteAttached().setVisibility(View.VISIBLE);
                holder.getTextCallbackNotes().setVisibility(View.VISIBLE);
                holder.getTextCallbackNotes().setText(i.notes);
                holder.getTextCallbackNotes().setSelected(true);
            }

            // Set click listener on the view, depending on whether the inspection is complete or not...
            holder.itemView.setOnClickListener(v -> {
                if (i.is_complete) {
                    BridgeLogger.log('I', TAG, "Trying reupload for ID: " + i.id);
                    BridgeAPIQueue.getInstance().getRequestQueue().cancelAll(i.id);
                    BridgeLogger.log('I', TAG, "Cancelled requests for ID: " + i.id);
                    Intent intent = new Intent(v.getContext(), ReviewAndSubmitActivity.class);
                    intent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, i.id);
                    v.getContext().startActivity(intent);
                } else {
                    Intent intent = new Intent(v.getContext(), InspectionDetailsActivity.class);
                    intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, i.id);
                    v.getContext().startActivity(intent);
                }
            });
        } catch (Exception e) {
            Log.e("SEARCH", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return currentList == null ? 0 : currentList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
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
                //Log.i("SEARCH", "dataSet size: " + dataSet.size() + " currentList size: " + currentList.size());
            }
        };
        return searchFilter;
    }

    public void setCurrentList(List<RouteSheet_View> list) {
        currentList = list;
        dataSet.clear();
        dataSet.addAll(currentList);
        submitList(currentList);
        Log.i("SEARCH_DEBUG", "Size of dataSet: " + dataSet.size());
    }

    public List<RouteSheet_View> getCurrentList() {
        return currentList;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(currentList, fromPosition, toPosition);
        Log.i("DRAG_DEBUG", "Item moved from " + fromPosition + " to " + toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public static class InspectionDiff extends DiffUtil.ItemCallback<RouteSheet_View> {
        @Override
        public boolean areItemsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.id == newItem.id;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.num_uploaded == newItem.num_uploaded
                    && oldItem.is_complete == newItem.is_complete
                    && oldItem.is_uploaded == newItem.is_uploaded;
        }
    }
}
