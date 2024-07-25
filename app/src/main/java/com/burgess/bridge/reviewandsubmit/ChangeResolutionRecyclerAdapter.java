package com.burgess.bridge.reviewandsubmit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.burgess.bridge.BridgeLogger;
import com.burgess.bridge.OnButtonClickListener;
import com.burgess.bridge.R;
import com.burgess.bridge.ResolutionHelper;

import java.util.List;

import data.Enums.Resolution;

public class ChangeResolutionRecyclerAdapter extends RecyclerView.Adapter<ChangeResolutionRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<ResolutionHelper.Resolution> mIncompleteReasons;
    private final LayoutInflater mLayoutInflater;
    private OnButtonClickListener mOnButtonClickListener;
    private DialogFragment mFragment;
    private static final String TAG = "CHG_RES_FRAG";

    public ChangeResolutionRecyclerAdapter(Context context, List<ResolutionHelper.Resolution> incompleteReasonList, OnButtonClickListener onButtonClickListener, DialogFragment changeResolutionFragment) {
        mContext = context;
        mIncompleteReasons = incompleteReasonList;
        mLayoutInflater = LayoutInflater.from(mContext);
        mOnButtonClickListener = onButtonClickListener;
        mFragment = changeResolutionFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mLayoutInflater.inflate(R.layout.item_change_resolution, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResolutionHelper.Resolution reason = mIncompleteReasons.get(position);
        holder.mResolutionCode = reason.code;
        holder.mResolutionButton.setText(reason.description);
        holder.mResolutionButton.setOnClickListener(v -> {
            try {
                mOnButtonClickListener.onButtonClick(Integer.toString(holder.mResolutionCode));
            } catch (Exception e) {
                BridgeLogger.log('E', TAG, "ERROR IN onBindViewHolder: " + e.getMessage());
            }
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
            mResolutionButton = itemView.findViewById(R.id.item_change_resolution_button);
        }
    }
}
