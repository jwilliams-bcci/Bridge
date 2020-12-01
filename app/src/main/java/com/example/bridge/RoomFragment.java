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
import android.widget.TextView;

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
    private View mView;

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
        mView = inflater.inflate(R.layout.fragment_room, container, false);
        List<Room> rooms = DataManager.getInstance().getRooms();

        RecyclerView recyclerRooms = (RecyclerView) mView.findViewById(R.id.room_recycler_buttons);
        Context contextFragment = this.getContext();
        Context contextActivity = getActivity();
        recyclerRooms.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerRooms.setAdapter(new RoomsRecyclerAdapter(getActivity(), rooms));

        return mView;
    }

    public void appendResultText(String textToAppend) {
        TextView result = mView.findViewById(R.id.room_text_result);
        result.append(textToAppend + " ");
    }
}