package com.example.bridge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.Direction;
import data.Inspection;

public class UploadsRecyclerAdapter extends RecyclerView.Adapter<UploadsRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Inspection> mUploads;
    private final LayoutInflater mLayoutInflater;
    private OnButtonClickListener mOnButtonClickListener;

    public UploadsRecyclerAdapter(Context context, List<Inspection> uploadsList, OnButtonClickListener onButtonClickListener) {
        mContext = context;
        mUploads = uploadsList;
        mLayoutInflater = LayoutInflater.from(mContext);
        mOnButtonClickListener = onButtonClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_gridview, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() { return mUploads.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mInspectionId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
