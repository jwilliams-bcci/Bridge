package com.burgess.bridge;

import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

public class RouteSheetDragEventListener implements View.OnDragListener {
    @Override
    public boolean onDrag(View v, DragEvent event) {
        final int action = event.getAction();

        switch(action) {
            case DragEvent.ACTION_DRAG_STARTED:
                if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    v.setBackgroundColor(Color.BLUE);
                    v.invalidate();
                    return true;
                }
                return false;
            case DragEvent.ACTION_DRAG_ENTERED:
                v.setBackgroundColor(Color.GREEN);
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                v.invalidate();
                return false;
            case DragEvent.ACTION_DRAG_ENDED:
                if (event.getResult()) {
                    Log.d("DRAG","Drop was handled");
                } else {
                    Log.d("DRAG", "Drop failed");
                }
                return true;
            default:
                Log.d("DRAG", "Unknown action type received by OnDragListener");
                break;
        }

        return false;
    }
}
