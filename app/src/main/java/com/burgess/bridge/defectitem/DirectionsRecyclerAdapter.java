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

import data.Tables.Direction_Table;

public class DirectionsRecyclerAdapter extends RecyclerView.Adapter<DirectionsRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Direction_Table> mDirections;
    private final LayoutInflater mLayoutInflater;
    private OnButtonClickListener mOnButtonClickListener;

    public DirectionsRecyclerAdapter(Context context, List<Direction_Table> directionList, OnButtonClickListener onButtonClickListener) {
        mContext = context;
        mDirections = directionList;
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
        Direction_Table direction = mDirections.get(position);
        holder.mDirectionId = direction.id;
        holder.mDirection = direction.direction_description;
        holder.mDirectionButton.setText(direction.direction_description);
        holder.mDirectionButton.setOnClickListener(v -> {
            TextView result = v.findViewById(R.id.direction_text_result);
            Button buttonClicked = (Button) v;
            Log.d("DIRECTION", "Button clicked - " + buttonClicked.getText());
            try {
                mOnButtonClickListener.onButtonClick((String) buttonClicked.getText());
            } catch (Exception e) {
                Log.d("DIRECTION", e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() { return mDirections.size(); }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mDirectionId;
        public String mDirection;
        public final Button mDirectionButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mDirectionButton = (Button) itemView.findViewById(R.id.item_gridview_button);
        }
    }
}
