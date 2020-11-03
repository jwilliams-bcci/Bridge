package com.example.bridge;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import data.DataManager;
import data.Inspection;
import data.InspectionHistory;

public class InspectionHistoryRecyclerAdapter extends RecyclerView.Adapter<InspectionHistoryRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<InspectionHistory> mInspectionHistoryList;
    private final LayoutInflater mLayoutInflater;

    public InspectionHistoryRecyclerAdapter(Context context, List<InspectionHistory> inspectionHistoryList) {
        mContext = context;
        mInspectionHistoryList = inspectionHistoryList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_inspection_history_list, parent, false);
        return new InspectionHistoryRecyclerAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        InspectionHistory inspectionHistory = mInspectionHistoryList.get(position);
        holder.mTextHistoryDate.setText(new SimpleDateFormat("MM/dd/yy").format(inspectionHistory.getInspectionDate()));
        holder.mTextHistoryDetails.setText(inspectionHistory.getFullInspectionDetails());
    }

    @Override
    public int getItemCount() {
        return mInspectionHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTextHistoryDate;
        public final TextView mTextHistoryDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextHistoryDate = (TextView) itemView.findViewById(R.id.inspection_history_list_item_text_date);
            mTextHistoryDetails = (TextView) itemView.findViewById(R.id.inspection_history_list_item_text_inspection_history_details);
        }
    }
}
