package android.app.mobilebattery.Fragments;

import android.app.mobilebattery.Activity.ChatWindow;
import android.app.mobilebattery.Adapter.ChatListAdapter;
import android.app.mobilebattery.Pojo.ChatListData;
import android.app.mobilebattery.Pojo.ChatMaster;
import android.app.mobilebattery.R;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ChatFragment extends Fragment implements ChatListAdapter.Action{

    DatabaseReference reference;

    private EditText searchBySenderName, searchByReceiverName;
    private ImageButton refreshFilter;
    private TextView noResultFoundTV;

    private ArrayList<ChatListData> chatListData;
    private ArrayList<ChatMaster> chatMasterListData;

    private RecyclerView chatRecyclerList;
    private ChatListAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_list_fragment, container, false);
        
        init(view);

        reference = FirebaseDatabase.getInstance().getReference();

        getChatMasterData();

        methodListener();

        return view;
    }

    private void init(View v) {

        progressBar = v.findViewById(R.id.progressBar);
        searchBySenderName = v.findViewById(R.id.searchBySenderName);
        searchByReceiverName = v.findViewById(R.id.searchByReceiverName);
        refreshFilter = v.findViewById(R.id.refreshFilter);
        noResultFoundTV = v.findViewById(R.id.noResultFoundTV);
        chatRecyclerList = v.findViewById(R.id.chatRecyclerList);

    }

    private void methodListener() {

        searchBySenderName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = s.toString()/*.toLowerCase()*/;
                Log.d("12345", searchString);
                getSearchedData(searchString, "from");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchByReceiverName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String searchString = s.toString()/*.toLowerCase()*/;
                Log.d("12345", searchString);
//                getSearchedData(searchString, "to");

                searchInList(searchString);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        refreshFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBySenderName.setText("");
                searchBySenderName.clearFocus();
                searchByReceiverName.setText("");
                searchByReceiverName.clearFocus();
                getChatMasterData();
            }
        });

    }

    private void searchInList(String searchString) {

        final ArrayList<ChatMaster> user = new ArrayList<>();

        for (int i = 0; i < chatMasterListData.size(); i++) {
            ChatMaster listPojo = chatMasterListData.get(i);
            if (listPojo.getTo().toLowerCase().contains(searchString)) {
                ChatMaster pojo = new ChatMaster();
                pojo.setChatUID(listPojo.getChatUID());
                pojo.setFrom(listPojo.getFrom());
                pojo.setFromDisplayName(listPojo.getFromDisplayName());
                pojo.setImage(listPojo.getImage());
                pojo.setLastMessage(listPojo.getLastMessage());
                pojo.setTimestamp(listPojo.getTimestamp());
                pojo.setTo(listPojo.getTo());
                pojo.setToDisplayName(listPojo.getToDisplayName());
                user.add(pojo);

            }
        }
        loadChatList(user);

    }

    private void getSearchedData(String sData, String orderOn) {
        progressBar.setVisibility(View.VISIBLE);

        chatMasterListData = new ArrayList<>();
//        final DatabaseReference mReference = reference.child("ChatMaster");
        Query query = reference.child("ChatMaster").orderByChild(orderOn).startAt(sData).endAt(sData+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ChatMaster master = ds.getValue(ChatMaster.class);

                    chatMasterListData.add(master);
                }

                loadChatList(chatMasterListData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error In Fetch data", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void loadChatList(ArrayList<ChatMaster> chatListData) {

        if (chatListData.size() == 0){
            noResultFoundTV.setVisibility(View.VISIBLE);
        }else {
            noResultFoundTV.setVisibility(View.GONE);
            Collections.reverse(chatListData);
        }


        progressBar.setVisibility(View.GONE);
        chatRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecyclerList.setItemAnimator(new DefaultItemAnimator());

        adapter = new ChatListAdapter(getActivity(), R.layout.chat_list_fragment_item, chatListData);
        adapter.action = this;

        chatRecyclerList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void getChatMasterData() {

        progressBar.setVisibility(View.VISIBLE);

        chatMasterListData = new ArrayList<>();

        final DatabaseReference chatMasterReference = reference.child("ChatMaster");

//        Query query = chatMasterReference.orderByChild("timestamp").limitToFirst();

        chatMasterReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()){

                    ChatMaster master = ds.getValue(ChatMaster.class);

                    if (ds.getValue() != null)
//                    Log.d("12345", ds.getValue().toString());

                    chatMasterListData.add(master);
                }

                loadChatList(chatMasterListData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void openChatWindow(int position) {

        ChatMaster master = chatMasterListData.get(position);

        if (master != null){

            Intent intent = new Intent(getActivity(), ChatWindow.class);
            intent.putExtra("chatKey", master.getChatUID());
            intent.putExtra("chatLabel", master.getFromDisplayName()+", "+master.getToDisplayName());
            startActivity(intent);
        }

    }
}
