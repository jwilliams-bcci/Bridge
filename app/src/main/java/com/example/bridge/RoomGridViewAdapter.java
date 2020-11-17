package com.example.bridge;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import data.DataManager;
import data.Room;

public class RoomGridViewAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Room> mRooms;
    private LayoutInflater mInflater;

    public RoomGridViewAdapter(Context context, List<Room> rooms) {
        mContext = context;
        mRooms = DataManager.getInstance().getRooms();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mRooms.size();
    }

    @Override
    public Object getItem(int i) {
        return mRooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = mInflater.inflate(R.layout.item_gridview_button, null);
        TextView textView = view.findViewById(R.id.gridview_item_text);
        textView.setText(mRooms.get(i).toString());
        Log.d("RoomAdapter", "Added new room " + mRooms.get(i).toString());
        return view;
//        Log.d("RoomAdapter","Adding new button...");
//        Room room = mRooms.get(i);
//        Log.d("RoomAdapter", "Button's text: " + room.getRoomName());
//
//        if(view == null) {
//            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            view = layoutInflater.inflate(R.layout.item_gridview_button, null);
//        }
//        TextView textView = view.findViewById(R.id.gridview_item_text);
//        textView.setText(room.getRoomName());
//
//        return view;
    }
}
