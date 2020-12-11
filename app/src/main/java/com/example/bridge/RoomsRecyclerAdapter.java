package com.example.bridge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import data.Room;

public class RoomsRecyclerAdapter extends RecyclerView.Adapter<RoomsRecyclerAdapter.ViewHolder> {
    private final Context mContext;
    private final List<Room> mRooms;
    private final LayoutInflater mLayoutInflater;
    private TextView mRoomResult;
    private OnButtonClickListener mOnButtonClickListener;

    public RoomsRecyclerAdapter(Context context, List<Room> roomList, OnButtonClickListener onButtonClickListener) {
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
        Room room = mRooms.get(position);
        holder.mRoomId = room.getRoomId();
        holder.mRoomName = room.getRoomName();
        holder.mRoomButton.setText(room.getRoomName());
        holder.mRoomButton.setOnClickListener((v) -> {
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
