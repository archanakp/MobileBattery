package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class NewBatteryRecyclerAdapter extends PagedListAdapter<NewBatteryPojo.NewBatteryItem, NewBatteryRecyclerAdapter.ItemViewHolder> {

    private Context context;

    public Action action;

    public NewBatteryRecyclerAdapter(Context context){
        super(DIFF_CALLBACK);

        this.context = context;

    }

    public interface Action{

        void editBattery(int position);
        void deleteBattery(int position);
        void assignBattery(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.battery_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.editListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null)
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.editBattery(holder.getAdapterPosition());
            }
        });

        holder.deleteListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null)
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.deleteBattery(holder.getAdapterPosition());
            }
        });

        holder.assignBtrToTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null)
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.assignBattery(holder.getAdapterPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        NewBatteryPojo.NewBatteryItem item = getItem(position);


        if (item != null) {
            Log.d("12345", "item  "+item.getBrand_name());

            holder.batteryBrandNameTextView.setText(item.getBrand_name());
            holder.batterySkuTextView.setText(item.getBattery_sku());
            holder.priceTextView.setText(String.valueOf(item.getCost_price()));
            holder.batteryTypeTextView.setText(item.getBattery_size());

            Picasso.get()
                    .load(item.getBattery_image())
                    .placeholder(R.drawable.app_logo)
                    .into(holder.batteryImageView);
        }
    }


    private static DiffUtil.ItemCallback<NewBatteryPojo.NewBatteryItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<NewBatteryPojo.NewBatteryItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull NewBatteryPojo.NewBatteryItem oldItem, @NonNull NewBatteryPojo.NewBatteryItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull NewBatteryPojo.NewBatteryItem oldItem, @NonNull NewBatteryPojo.NewBatteryItem newItem) {
                    return !(oldItem != newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView batteryBrandNameTextView, batterySkuTextView, priceTextView, batteryTypeTextView;
        ImageView batteryImageView, editListBtn, assignBtrToTech, deleteListBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            batteryBrandNameTextView = itemView.findViewById(R.id.batteryBrandNameTextView);
            batterySkuTextView = itemView.findViewById(R.id.batterySkuTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            batteryTypeTextView = itemView.findViewById(R.id.batteryTypeTextView);
            batteryImageView= itemView.findViewById(R.id.batteryImageView);
            editListBtn= itemView.findViewById(R.id.editListBtn);
            assignBtrToTech= itemView.findViewById(R.id.assignBtrToTech);
            deleteListBtn= itemView.findViewById(R.id.deleteListBtn);
        }
    }

}
