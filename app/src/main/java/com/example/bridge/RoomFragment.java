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
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import data.DataManager;
import data.Room;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoomFragment extends DialogFragment {
    private static RoomFragment mFragment;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

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
        View view = inflater.inflate(R.layout.fragment_room, container, false);

        final RecyclerView recyclerRooms = (RecyclerView) view.findViewById(R.id.room_recycler_buttons);
        final GridLayoutManager roomsLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerRooms.setLayoutManager(roomsLayoutManager);

        List<Room> rooms = DataManager.getInstance().getRooms();
        final RoomsRecyclerAdapter roomsRecyclerAdapter = new RoomsRecyclerAdapter(recyclerRooms.getContext(), rooms);
        recyclerRooms.setAdapter(roomsRecyclerAdapter);

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Context context = requireActivity();
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.fragment_room, null);

        RecyclerView recyclerRooms = (RecyclerView) dialogView.findViewById(R.id.room_recycler_buttons);
        recyclerRooms.setLayoutManager(new GridLayoutManager(context, 3));
        List<Room> rooms = DataManager.getInstance().getRooms();
        RoomsRecyclerAdapter roomsRecyclerAdapter = new RoomsRecyclerAdapter(context, rooms);
        recyclerRooms.setAdapter(roomsRecyclerAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        builder.setTitle("Choose Room");
        return builder.create();
    }
}