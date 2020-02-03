package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.ChatListData;
import android.app.mobilebattery.Pojo.ChatMaster;
import android.app.mobilebattery.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    private Context context;
    private int resource;
    private ArrayList<ChatMaster> arrayList;

    private LayoutInflater inflater;

    public Action action;


    public ChatListAdapter(Context context, int resource, ArrayList<ChatMaster> arrayList){

        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        inflater = LayoutInflater.from(context);

    }

    public interface Action {

        void openChatWindow (int position);

    }


    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.chat_list_fragment_item, parent, false);

        final MyViewHolder viewHolder = new MyViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null){
                    if (viewHolder.getAdapterPosition() != RecyclerView.NO_POSITION){
                        action.openChatWindow(viewHolder.getAdapterPosition());
                    }
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, int position) {

        ChatMaster pojo = arrayList.get(position);

        holder.name.setText(pojo.getFromDisplayName()+","+pojo.getToDisplayName());
        holder.lastMsg.setText(pojo.getLastMessage());

        Date msgDate = new java.util.Date(pojo.getTimestamp());
        holder.lastMsgTime.setText(calculateDateDiffrence(msgDate));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    String calculateDateDiffrence(Date msgDate){


        if (new Date().getYear() == msgDate.getYear()){
            if (returnIntDate(new Date(), "MM") == returnIntDate(msgDate, "MM")){
                if (returnIntDate(new Date(), "DD") == returnIntDate(msgDate, "DD")){
                    return new SimpleDateFormat("HH:MM a").format(msgDate);
                }else if ((returnIntDate(new Date(), "DD") - returnIntDate(msgDate, "DD")) == 1){
                    return "Yesterday";
                }else if ((returnIntDate(new Date(), "DD") - returnIntDate(msgDate, "DD")) == 2){
                    return new SimpleDateFormat("EEEE").format(msgDate);
                }else if ((returnIntDate(new Date(), "DD") - returnIntDate(msgDate, "DD")) == 3){
                    return new SimpleDateFormat("EEEE").format(msgDate);
                }else if ((returnIntDate(new Date(), "DD") - returnIntDate(msgDate, "DD")) == 4){
                    return new SimpleDateFormat("EEEE").format(msgDate);
                }else if ((returnIntDate(new Date(), "DD") - returnIntDate(msgDate, "DD")) == 5){
                    return new SimpleDateFormat("EEE").format(msgDate);
                }else if ((returnIntDate(new Date(), "DD") - returnIntDate(msgDate, "DD")) == 6){
                    return new SimpleDateFormat("EEEE").format(msgDate);
                }else {
                    return new SimpleDateFormat("dd/mm/yyyy").format(msgDate);
                }
            }else {
                return new SimpleDateFormat("dd/mm/yyyy").format(msgDate);
            }
        }else {
            return new SimpleDateFormat("dd/mm/yyyy").format(msgDate);
        }

    }

    int returnIntDate(Date date, String formate){
        return Integer.valueOf(new SimpleDateFormat(formate).format(date));
    }



    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name, lastMsg, lastMsgTime;
        ImageView firstPersonPImage, secondPersonPImage, goNext;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.senderAndReceiverNameTV);
            lastMsg = itemView.findViewById(R.id.lastMessageTV);
            lastMsgTime = itemView.findViewById(R.id.lastMessageTimeTV);
            firstPersonPImage = itemView.findViewById(R.id.personOneProfileImage);
            secondPersonPImage = itemView.findViewById(R.id.personTwoProfileImage);
            goNext = itemView.findViewById(R.id.goNextIV);
        }
    }
}
