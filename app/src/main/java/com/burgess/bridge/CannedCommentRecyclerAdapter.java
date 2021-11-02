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

import data.CannedComment;

public class CannedCommentRecyclerAdapter extends RecyclerView.Adapter<CannedCommentRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<String> mCannedComments;
    private final LayoutInflater mLayoutInflater;
    private OnButtonClickListener mOnButtonClickListener;

    public CannedCommentRecyclerAdapter(Context context, List<String> cannedCommentList, OnButtonClickListener onButtonClickListener) {
        mContext = context;
        mCannedComments = cannedCommentList;
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
        String cannedComment = mCannedComments.get(position);
        holder.mCannedCommentButton.setText(cannedComment);
        holder.mCannedCommentButton.setOnClickListener(v -> {
            TextView result = v.findViewById(R.id.canned_comment_text_result);
            Button buttonClicked = (Button) v;
            Log.d("CANNED_COMMENT", "Button clicked - " + cannedComment);
            try {
                mOnButtonClickListener.onButtonClick(cannedComment);
            } catch (Exception e) {
                Log.e("CANNED_COMMENT", e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCannedComments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mCannedCommentId;
        public String mCannedComment;
        public final Button mCannedCommentButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCannedCommentButton = (Button) itemView.findViewById(R.id.item_gridview_button);
        }
    }
}
