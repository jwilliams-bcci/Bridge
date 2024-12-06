package com.burgess.bridge.routesheet;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.burgess.bridge.apiqueue.BridgeAPIQueue;
import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.R;
import com.burgess.bridge.inspectiondetails.InspectionDetailsActivity;
import com.burgess.bridge.reviewandsubmit.ReviewAndSubmitActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import data.Views.RouteSheet_View;

public class RouteSheetListAdapter extends ListAdapter<RouteSheet_View, RouteSheetViewHolder> implements Filterable, ItemTouchHelperAdapter {
    public static final String TAG = "ROUTE_SHEET";

    private RouteSheetViewModel routeSheetVM;
    private List<RouteSheet_View> currentList;
    private List<RouteSheet_View> dataSet = new ArrayList<>();

    private String authToken;
    private String inspectorId;

    private boolean showMenu = false;

    protected RouteSheetListAdapter(@NonNull InspectionDiff diffCallback, RouteSheetViewModel routeSheetVM) {
        super(diffCallback);
        this.routeSheetVM = routeSheetVM;
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
            if (!TextUtils.isEmpty(i.JobNumber) && i.JobNumber.toLowerCase().startsWith("lot")) {
                addressDisplay = i.Address + " - " + i.JobNumber;
            } else {
                addressDisplay = i.Address;
            }
            holder.textAddress.setText(addressDisplay);
            holder.textCommunity.setText(i.Community);
            holder.textInspectionType.setText(i.InspectionType);
            holder.cardView.setBackgroundColor(Color.WHITE);
            holder.textUploaded.setVisibility(View.GONE);
            holder.imageViewReupload.setVisibility(View.GONE);
            holder.imageViewNoteAttached.setVisibility(View.GONE);
            holder.textCallbackNotes.setVisibility(View.GONE);
            holder.constraintLayoutMenu.setVisibility(View.GONE);
            holder.verticalDivider.setVisibility(View.GONE);
            holder.buttonReupload.setVisibility(View.GONE);
            holder.buttonResetInspection.setVisibility(View.VISIBLE);
            holder.buttonViewQueue.setVisibility(View.GONE);

            // If the inspection is complete, set the color to yellow and show other view components
            if (i.IsComplete) {
                holder.cardView.setBackgroundColor(Color.YELLOW);
                holder.textUploaded.setVisibility(View.VISIBLE);
                holder.textUploaded.setText(String.format(Locale.US, "Uploaded %d of %d items.", i.NumUploaded, i.NumTotal));
                holder.buttonReupload.setVisibility(View.VISIBLE);
                holder.verticalDivider.setVisibility(View.VISIBLE);
                holder.buttonViewQueue.setVisibility(View.VISIBLE);
            }

            // If the inspection upload failed, set the color to red and show reupload icon and button
            if (i.IsFailed) {
                holder.cardView.setBackgroundColor(Color.RED);
                holder.buttonReupload.setVisibility(View.VISIBLE);
                holder.buttonReupload.setEnabled(true);
                holder.verticalDivider.setVisibility(View.VISIBLE);
            }

            // If the inspection is uploaded, set the color to green, disable buttons
            if (i.IsUploaded) {
                holder.cardView.setBackgroundColor(Color.GREEN);
                holder.constraintLayoutMenu.setVisibility(View.GONE);
                holder.imageViewReupload.setVisibility(View.GONE);
            }

            // If there's a note, show the note icon
            if (!TextUtils.isEmpty(i.Notes) && !i.Notes.equals("null")) {
                holder.imageViewNoteAttached.setVisibility(View.VISIBLE);
                holder.textCallbackNotes.setVisibility(View.VISIBLE);
                holder.textCallbackNotes.setText(i.Notes);
                holder.textCallbackNotes.setSelected(true);
            }

            // Set click listener on the view, depending on whether the inspection is complete or not...
            holder.itemView.setOnClickListener(v -> {
                if (i.IsComplete) {
                    Snackbar.make(v, "Inspection already completed", Snackbar.LENGTH_LONG).show();
                } else if (i.DivisionID == 20 && i.InspectionTypeID != 1154) {
                    boolean reobPresent = false;
                    for (int lcv = 0; lcv < currentList.size(); lcv++) {
                        RouteSheet_View inspection = currentList.get(lcv);
                        if (!inspection.IsUploaded && inspection.InspectionTypeID == 1154) {
                            reobPresent = true;
                        }
                    }
                    if (reobPresent) {
                        Snackbar.make(v, "There is a re-ob at this location, please complete that first.", Snackbar.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(v.getContext(), InspectionDetailsActivity.class);
                        intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, i.InspectionID);
                        v.getContext().startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(v.getContext(), InspectionDetailsActivity.class);
                    intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, i.InspectionID);
                    v.getContext().startActivity(intent);
                }
            });

            holder.imageViewShowMenu.setOnClickListener(v -> {
                showMenu = !showMenu;
                if (showMenu) {
                    holder.constraintLayoutMenu.setVisibility(View.VISIBLE);
                    holder.imageViewShowMenu.setBackgroundResource(android.R.drawable.arrow_up_float);
                } else {
                    holder.constraintLayoutMenu.setVisibility(View.GONE);
                    holder.imageViewShowMenu.setBackgroundResource(android.R.drawable.arrow_down_float);
                }
            });

            holder.buttonResetInspection.setOnClickListener(v -> {
                routeSheetVM.deleteInspectionDefects(i.InspectionID);
                routeSheetVM.deleteInspection(i.InspectionID);
                routeSheetVM.deleteInspectionHistories(i.InspectionID);
                routeSheetVM.updateInspections(getAuthToken(), getInspectorId());
            });

            holder.buttonReupload.setOnClickListener(v -> {
                if (i.IsFailed) {
                    BridgeLogger.log('I', TAG, "Trying reupload for ID: " + i.InspectionID);
                    BridgeAPIQueue.getInstance().getRequestQueue().cancelAll(i.InspectionID);
                    BridgeLogger.log('I', TAG, "Cancelled requests for ID: " + i.InspectionID);
                    Intent intent = new Intent(v.getContext(), ReviewAndSubmitActivity.class);
                    intent.putExtra(ReviewAndSubmitActivity.INSPECTION_ID, i.InspectionID);
                    v.getContext().startActivity(intent);
                } else {
                    Snackbar.make(v, "Upload still in progress", Snackbar.LENGTH_LONG).show();
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

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
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
                        if (item.Community.toLowerCase().contains(constraint)) {
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
            return oldItem.InspectionID == newItem.InspectionID;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull RouteSheet_View oldItem, @NonNull RouteSheet_View newItem) {
            return oldItem.NumUploaded == newItem.NumUploaded
                    && oldItem.IsComplete == newItem.IsComplete
                    && oldItem.IsUploaded == newItem.IsUploaded;
        }
    }
}
