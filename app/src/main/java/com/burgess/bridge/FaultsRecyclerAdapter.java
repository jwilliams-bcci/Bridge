package com.burgess.bridge;

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

import data.Fault;

public class FaultsRecyclerAdapter extends RecyclerView.Adapter<FaultsRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Fault> mFaults;
    private final LayoutInflater mLayoutInflater;
    private TextView mFaultResult;
    private OnButtonClickListener mOnButtonClickListener;

    public FaultsRecyclerAdapter(Context context, List<Fault> faultList, OnButtonClickListener onButtonClickListener) {
        mContext = context;
        mFaults = faultList;
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
        Fault fault = mFaults.get(position);
        holder.mFaultId = fault.getFaultId();
        holder.mFault = fault.getFault();
        holder.mFaultPrint = fault.getFaultPrint();
        holder.mFaultButton.setText(fault.getFault());
        holder.mFaultButton.setOnClickListener(v -> {
            TextView result = v.findViewById(R.id.fault_text_result);
            Button buttonClicked = (Button) v;
            Log.d("FAULT", "Button clicked - " + buttonClicked.getText());
            try {
                mOnButtonClickListener.onButtonClick(holder.mFaultPrint);
            } catch (Exception e) {
                Log.d("FAULT", e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFaults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mFaultId;
        public String mFault;
        public String mFaultPrint;
        public final Button mFaultButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mFaultButton = (Button) itemView.findViewById(R.id.item_gridview_button);
        }
    }
}
