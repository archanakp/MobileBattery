package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.CashInHandPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CashInHandAdapter extends PagedListAdapter<CashInHandPojo.CashInHandItem, CashInHandAdapter.ItemViewHolder> {

    private Context context;

    public CashInHandAction cashInHandAction;

    public CashInHandAdapter(Context context) {
        super(DIFF_CALLBACK);

        this.context = context;
    }

    public interface CashInHandAction{
//        void editCashList(int position);

        void deleteCashList(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.cash_in_hand_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.deleteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cashInHandAction != null)
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        cashInHandAction.deleteCashList(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        CashInHandPojo.CashInHandItem item = getItem(position);

        if (item != null){

            holder.agentName.setText(item.getAgent_name());
            holder.transferredTo.setText(item.getTranferred_to_agent());
            holder.amount.setText("$" + item.getAmount());

            try {
                Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getCreated_at());
                String newstr = new SimpleDateFormat("yyyy-MM-dd").format(date);
                holder.transferDate.setText(newstr);

            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (item.getTransferred_cash_received()) {
                holder.transferStatus.setText("Received");
                holder.transferStatusBcg.setBackgroundResource(R.color.greenBcg);
            } else {
                holder.transferStatus.setText("Pending");
                holder.transferStatusBcg.setBackgroundResource(R.color.redBcg);
            }
        }

    }

    private static DiffUtil.ItemCallback<CashInHandPojo.CashInHandItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CashInHandPojo.CashInHandItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull CashInHandPojo.CashInHandItem oldItem, @NonNull CashInHandPojo.CashInHandItem newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull CashInHandPojo.CashInHandItem oldItem, @NonNull CashInHandPojo.CashInHandItem newItem) {
                    return !(oldItem != newItem);
                }
            };



    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView transferredTo, agentName, transferDate;
        TextView transferStatus, amount;
        ImageView deleteDetails;

        RelativeLayout transferStatusBcg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            transferStatusBcg = itemView.findViewById(R.id.transferStatusBcg);
            transferStatus = itemView.findViewById(R.id.transferStatus);
            deleteDetails = itemView.findViewById(R.id.deleteDetails);
            transferredTo = itemView.findViewById(R.id.transferredTo);
            agentName = itemView.findViewById(R.id.agentName);
            transferDate = itemView.findViewById(R.id.transferDate);
            amount = itemView.findViewById(R.id.amount);
        }
    }

}
