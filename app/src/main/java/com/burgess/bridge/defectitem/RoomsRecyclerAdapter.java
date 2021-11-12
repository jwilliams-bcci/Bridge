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

import data.Tables.Room_Table;

public class RoomsRecyclerAdapter extends RecyclerView.Adapter<RoomsRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Room_Table> mRooms;
    private final LayoutInflater mLayoutInflater;
    private OnButtonClickListener mOnButtonClickListener;

    public RoomsRecyclerAdapter(Context context, List<Room_Table> roomList, OnButtonClickListener onButtonClickListener) {
        mContext = context;
        mRooms = roomList;
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
        Room_Table room = mRooms.get(position);
        holder.mRoomId = room.id;
        holder.mRoomName = room.room_name;
        holder.mRoomButton.setText(room.room_name);
        holder.mRoomButton.setOnClickListener(v -> {
            TextView result = v.findViewById(R.id.room_text_result);
            Button buttonClicked = (Button) v;
            Log.d("ROOM", "Button clicked - " + buttonClicked.getText());
            try {
                mOnButtonClickListener.onButtonClick((String) buttonClicked.getText());
            } catch (Exception e) {
                Log.d("ROOM", e.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRooms.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public int mRoomId;
        public String mRoomName;
        public final Button mRoomButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mRoomButton = (Button) itemView.findViewById(R.id.item_gridview_button);
        }
    }
}
