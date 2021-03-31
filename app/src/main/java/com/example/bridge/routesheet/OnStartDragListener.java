package com.example.bridge.routesheet;

import androidx.recyclerview.widget.RecyclerView;

public interface OnStartDragListener {
    void onStartDrag(RecyclerView.ViewHolder viewHolder);
    void onEndDrag(RecyclerView.ViewHolder viewHolder);
}
