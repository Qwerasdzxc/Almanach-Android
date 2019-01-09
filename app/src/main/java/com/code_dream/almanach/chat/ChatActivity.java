package com.code_dream.almanach.chat;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.f2prateek.dart.Dart;
import com.f2prateek.dart.InjectExtra;
import com.code_dream.almanach.BaseActivity;
import com.code_dream.almanach.R;
import com.code_dream.almanach.adapters.RecyclerViewAdapter;
import com.code_dream.almanach.models.ChatRoom;
import com.code_dream.almanach.models.Message;
import com.code_dream.almanach.network.services.MessageService;
import com.code_dream.almanach.utility.Registry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity implements ChatContract.View, RecyclerViewAdapter.ItemClickListener, MessageService.MessageClientCallbacks {

    @BindView(R.id.chat_activity_toolbar) Toolbar toolbar;
    @BindView(R.id.chat_recycler_view_message_list) RecyclerView recyclerView;
    @BindView(R.id.chat_edittext) EditText messageEditText;
    @BindView(R.id.chat_activity_progress_bar) ProgressBar progressBar;

    @InjectExtra("chat_room")
    ChatRoom chatRoom;

    private BroadcastReceiver broadcastReceiver;
    private MessageService.MessageServiceInterface messageService;
    private ServiceConnection serviceConnection = new MyServiceConnection();

    private RecyclerViewAdapter adapter;

    private ArrayList<Object> recyclerViewContentList = new ArrayList<>();

    private ChatPresenter presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);
        setupView();

        // Load old messages from the server.
        presenter.loadAllMessages();
    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        super.onDestroy();
    }

    @Override
    public void setupPresenter() {
        presenter = new ChatPresenter(this);
    }

    @Override
    public void setupView() {
        ButterKnife.bind(this);
        Dart.inject(this);

        presenter.setChatRoom(chatRoom);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(chatRoom.getPerson().getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerViewAdapter(recyclerViewContentList);
        adapter.setItemClickListener(this);
        recyclerView.setAdapter(adapter);

        // Scroll to bottom when the keyboard pops-up
        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if (!recyclerViewContentList.isEmpty())
                    recyclerView.smoothScrollToPosition(recyclerViewContentList.size() - 1);
            }
        });
    }

    @OnClick(R.id.chat_button_send)
    public void onSendClick() {
        presenter.onSendClick();
    }

    @Override
    public void onRecyclerViewItemClick(View view, Class<?> viewHolder, int position) {
        // TODO: Implement some action on item click.
    }

    @Override
    public void startChatService() {
        // Broadcast receiver to listen for the broadcast from MessageService.
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                presenter.onReceiveFromService(intent);
            }
        };

        // Establish a connection with the MessageService.
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter(Registry.MESSAGES_BROADCAST_NAME));
        bindService(new Intent(this, MessageService.class), serviceConnection, BIND_AUTO_CREATE);
    }

    @Override
    public String getInputText() {
        return messageEditText.getText().toString();
    }

    @Override
    public void showProgressBar(boolean show) {
        if (show) {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateMessages(List<Message> messages) {
        recyclerViewContentList.clear();
        recyclerViewContentList.addAll(messages);

        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearInputText() {
        messageEditText.getText().clear();
    }

    @Override
    public void addMessageBubble(final Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerViewContentList.add(message);
                adapter.notifyItemInserted(recyclerViewContentList.size());
            }
        });
    }

    @Override
    public void scrollToBottom() {
        recyclerView.scrollToPosition(recyclerViewContentList.size() - 1);
    }

    @Override
    public void sendMessage(Message message) {
        messageService.sendMessage(message);
    }

    @Override
    public void onMessageReceived(final Message message) {
        addMessageBubble(message);
    }

    @Override
    public int getChatRoomId() {
        return chatRoom.getChatRoomId();
    }

    private class MyServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            messageService = (MessageService.MessageServiceInterface) iBinder;
            messageService.getServiceInstance().addMessageClientListener(ChatActivity.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            messageService = null;
        }
    }
}
