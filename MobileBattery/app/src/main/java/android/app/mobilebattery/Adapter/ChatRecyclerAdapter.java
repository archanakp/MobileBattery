package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.ChatListData;
import android.app.mobilebattery.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRecyclerAdapter extends RecyclerView.Adapter<ChatRecyclerAdapter.MyViewHolder> {

    private Context context;
    private int resource;
    private List<ChatListData> chatList = new ArrayList<>();
//    private TextView chatMessage, messageTime;
    private ImageView senderProfileImage;

    private LayoutInflater inflater;

    public ChatRecyclerAdapter(Context context, int resource, ArrayList<ChatListData> chatList) {

        this.context = context;
        this.resource = resource;
        this.chatList = chatList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view ;

//        if (helper.getTo().equals("send")){
        view = inflater.inflate(R.layout.chat_send_message_item, parent, false);
//        chatMessage = view.findViewById(R.id.messageSendTV);
//        messageTime = view.findViewById(R.id.messageTime);
//        }else {
//            view = inflater.inflate(R.layout.chat_receive_message_item, parent, false);
//            chatMessage = view.findViewById(R.id.senderMessage);
//            messageTime = view.findViewById(R.id.messageTime);
//            senderProfileImage = view.findViewById(R.id.senderProfileImage);
//
//        }



        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ChatListData data = chatList.get(position);


        holder.chatMessage.setText(data.getMessage());
        Date date = new Date(data.getTimestamp());

        holder.messageTime.setText(calculateDateDiffrence(date));

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView chatMessage, messageTime;

        public MyViewHolder(@NonNull View view) {
            super(view);

            chatMessage = view.findViewById(R.id.messageSendTV);
            messageTime = view.findViewById(R.id.messageTime);
        }
    }


}
