package com.example.bridge;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import data.DataManager;
import data.Room;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends DialogFragment implements OnButtonClickListener {
    private static RoomFragment mFragment;
    private View mView;
    private EditText mRoomResultsDialog;

    public RoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RoomFragment newInstance() {
        mFragment = new RoomFragment();
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_room, container, false);
        List<Room> rooms = DataManager.getInstance().getRooms();
        mRoomResultsDialog = mView.findViewById(R.id.room_text_result);
        TextView roomResultsDefectItem = getActivity().findViewById(R.id.defect_item_text_room);

        RecyclerView recyclerRooms = (RecyclerView) mView.findViewById(R.id.room_recycler_buttons);
        recyclerRooms.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerRooms.setAdapter(new RoomsRecyclerAdapter(getActivity(), rooms, this));

        Button saveAndExit = mView.findViewById(R.id.room_button_save_and_exit);
        saveAndExit.setOnClickListener(v -> {
            roomResultsDefectItem.setText(mRoomResultsDialog.getText());
            mFragment.dismiss();
        });

        return mView;
    }

    @Override
    public void onButtonClick(String buttonName) {
        mRoomResultsDialog.append(buttonName + " ");
    }
}