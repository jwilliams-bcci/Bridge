package com.example.bridge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import data.DataManager;
import data.Inspection;
import data.Inspection_Table;
import data.Location;

public class InspectionRecyclerAdapter extends RecyclerView.Adapter<InspectionRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Inspection_Table> mInspectionList;
    private final LayoutInflater mLayoutInflater;

    public InspectionRecyclerAdapter(Context context, List<Inspection_Table> inspectionList) {
        mContext = context;
        mInspectionList = inspectionList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_inspection_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Inspection_Table inspection = mInspectionList.get(position);
        Location location = DataManager.getInstance().getLocation(inspection.location_id);
        holder.mInspectionId = inspection.inspection_number;
        holder.mBuilderId = inspection.builder_id;
        holder.mLocationId = inspection.location_id;
        holder.mTextCommunity.setText(location.getCommunity());
        holder.mTextAddress.setText(location.getFullAddress());
        holder.mTextInspectionType.setText(inspection.inspection_type);
        holder.mTextInspectionNotes.setText(inspection.notes);
    }

    @Override
    public int getItemCount() {
        return mInspectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mInspectionId;
        public int mBuilderId;
        public int mLocationId;
        public final TextView mTextCommunity;
        public final TextView mTextAddress;
        public final TextView mTextInspectionType;
        public final TextView mTextInspectionNotes;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCommunity = (TextView) itemView.findViewById(R.id.text_recycler_community);
            mTextAddress = (TextView) itemView.findViewById(R.id.text_recycler_address);
            mTextInspectionType = (TextView) itemView.findViewById(R.id.text_recycler_inspection_type);
            mTextInspectionNotes = (TextView) itemView.findViewById(R.id.text_recycler_inspection_notes);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, InspectionDetailsActivity.class);
                    intent.putExtra(InspectionDetailsActivity.INSPECTION_ID, mInspectionId);
                    intent.putExtra(InspectionDetailsActivity.BUILDER_ID, mBuilderId);
                    intent.putExtra(InspectionDetailsActivity.LOCATION_ID, mLocationId);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
