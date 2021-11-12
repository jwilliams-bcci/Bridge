package com.burgess.bridge.defectitem;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;

import java.util.List;

import data.Tables.Fault_Table;

public class FaultsRecyclerAdapter extends RecyclerView.Adapter<FaultsRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Fault_Table> mFaults;
    private final LayoutInflater mLayoutInflater;
    private TextView mFaultResult;
    private OnButtonClickListener mOnButtonClickListener;

    public FaultsRecyclerAdapter(Context context, List<Fault_Table> faultList, OnButtonClickListener onButtonClickListener) {
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
        Fault_Table fault = mFaults.get(position);
        holder.mFaultId = fault.id;
        holder.mFault = fault.text;
        holder.mFaultPrint = fault.display_text;
        holder.mFaultButton.setText(fault.text);
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
