package com.code_dream.almanach.chat_tab;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.chat.ChatActivity;
import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.network.services.MessageService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentChat extends Fragment implements ChatTabContract.View, RecyclerViewAdapter.ItemClickListener {

    @BindView(R.id.chat_fragment_recycler_view) RecyclerView recyclerView;
    @BindView(R.id.chat_tab_swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;

    RecyclerViewAdapter adapter;
    ChatTabPresenter presenter;

    ArrayList<Object> recyclerViewContentList = new ArrayList<>();

    public FragmentChat() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, view);

        presenter = new ChatTabPresenter(this);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimary));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getChatRooms();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerViewAdapter(recyclerViewContentList);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Download all past ChatRooms and display it in the RecyclerView.
        presenter.getChatRooms();
    }

    @Override
    public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("chat_room", ((ChatRoom) recyclerViewContentList.get(position)));

        Intent serviceIntent = new Intent(getContext(), MessageService.class);

        getActivity().startService(serviceIntent);
        startActivity(intent);
    }

    @Override
    public void updateList(List<ChatRoom> chatRooms) {
        recyclerViewContentList.clear();
        recyclerViewContentList.addAll(chatRooms);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setSwipeRefreshing(boolean enabled) {
        swipeRefreshLayout.setRefreshing(enabled);
    }
}
