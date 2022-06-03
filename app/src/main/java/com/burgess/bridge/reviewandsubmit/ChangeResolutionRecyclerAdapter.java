package com.burgess.bridge.reviewandsubmit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;

import java.util.List;

import data.Enums.Resolution;

public class ChangeResolutionRecyclerAdapter extends RecyclerView.Adapter<ChangeResolutionRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Resolution> mIncompleteReasons;
    private final LayoutInflater mLayoutInflater;
    private OnButtonClickListener mOnButtonClickListener;
    private static final String TAG = "CHG_RES_FRAG";

    public ChangeResolutionRecyclerAdapter(Context context, List<Resolution> incompleteReasonList, OnButtonClickListener onButtonClickListener) {
        mContext = context;
        mIncompleteReasons = incompleteReasonList;
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
        Resolution reason = mIncompleteReasons.get(position);
        holder.mResolutionCode = reason.code;
        holder.mResolutionButton.setText(reason.toString());
        holder.mResolutionButton.setOnClickListener(v -> {
            Button buttonClicked = (Button) v;
            BridgeLogger.log('I', TAG, "Button clicked - " + buttonClicked.getText());
        });
    }

    @Override
    public int getItemCount() {
        return mIncompleteReasons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mResolutionCode;
        public final Button mResolutionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mResolutionButton = (Button) itemView.findViewById(R.id.item_gridview_button);
        }
    }
}
