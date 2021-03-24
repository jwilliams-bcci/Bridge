package com.example.bridge;

import com.example.bridge.routesheet.RouteSheetViewHolder;

public interface ItemTouchHelperContract {
    void onRowMoved (int fromPosition, int toPosition);

    void onRowSelected (RouteSheetViewHolder viewHolder);

    void onRowClear (RouteSheetViewHolder viewHolder);
}
