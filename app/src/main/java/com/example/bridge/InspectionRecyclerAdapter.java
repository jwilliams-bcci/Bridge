package com.example.bridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class InspectionRecyclerAdapter extends RecyclerView.Adapter {
    private final Context mContext;
    private final List<Inspection> mInspectionList;
    private final LayoutInflater mLayoutInflater;
    private TextView mTextCommunity;
    private TextView mTextAddress;
    private TextView mTextInspectionType;
    private TextView mTextInspectionNotes;

    public InspectionRecyclerAdapter(Context context, List<Inspection> inspectionList) {
        mContext = context;
        mInspectionList = inspectionList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_inspection_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Inspection inspection = mInspectionList.get(position);
        mTextCommunity.setText(inspection.getCommunity());
        mTextAddress.setText(inspection.getAddress());
        mTextInspectionType.setText(inspection.getInspectionType());
        mTextInspectionNotes.setText(inspection.getNotes());
    }

    @Override
    public int getItemCount() {
        return mInspectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextCommunity = (TextView) itemView.findViewById(R.id.text_recycler_community);
            mTextAddress = (TextView) itemView.findViewById(R.id.text_recycler_address);
            mTextInspectionType = (TextView) itemView.findViewById(R.id.text_recycler_inspection_type);
            mTextInspectionNotes = (TextView) itemView.findViewById(R.id.text_recycler_inspection_notes);
        }
    }
}
