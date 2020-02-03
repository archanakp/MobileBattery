package android.app.mobilebattery.Activity;

import android.app.mobilebattery.Adapter.ChatRecyclerAdapter;
import android.app.mobilebattery.Pojo.ChatListData;
import android.app.mobilebattery.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatWindow extends AppCompatActivity {

    private TextView pageTitleText;
    private ImageView attachFile;
    private EditText writeMessageET;
    private RelativeLayout sendMessageRL;
    private Toolbar toolbar;
    private RecyclerView chatRecyclerView;

    private String chatUID, title;

    private ArrayList<ChatListData> chatListData;
    private ChatRecyclerAdapter recyclerAdapter;

    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        init();
        setSupportActionBar(toolbar);

        reference = FirebaseDatabase.getInstance().getReference("MessageMaster");

        Intent intent = getIntent();
        chatUID = intent.getStringExtra("chatKey");
        title = intent.getStringExtra("chatLabel");
        Log.d("12345", chatUID);
        pageTitleText.setText(title);
        loadChatData(chatUID);

    }

    private void init() {

        toolbar = findViewById(R.id.toolbar);
        pageTitleText = findViewById(R.id.pageTitleText);
        attachFile = findViewById(R.id.attachFile);
        writeMessageET = findViewById(R.id.writeMessageET);
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        sendMessageRL = findViewById(R.id.sendMessageRL);

    }

    public void goBack(View view) {

        super.onBackPressed();
        finish();

    }

    private void loadChatData(String chatUID) {
        chatListData = new ArrayList<>();
        DatabaseReference mRefrence = reference.child(chatUID);
        Log.d("12345", "data in chat windows load data");

        mRefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("12345", "chat windows");

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    ChatListData data = ds.getValue(ChatListData.class);
                    if (ds.getValue() != null)
                    Log.d("12345", "data in chat windows"+ds.getValue().toString());
                    Log.d("12345", "chat windows");
                    chatListData.add(data);
                }

                showList(chatListData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("12345", "error in chat windows");

            }
        });

    }

    private void showList(ArrayList<ChatListData> chatListData) {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new ChatRecyclerAdapter(ChatWindow.this, R.layout.chat_send_message_item, chatListData);

        chatRecyclerView.setAdapter(recyclerAdapter);


    }
}
