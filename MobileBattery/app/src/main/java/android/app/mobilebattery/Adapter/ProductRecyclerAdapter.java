package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.Pojo.ProductPojo;
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

public class ProductRecyclerAdapter extends PagedListAdapter<ProductPojo.ProductItem, ProductRecyclerAdapter.ItemViewHolder> {

    private Context context;

    public Action action;

    public ProductRecyclerAdapter(Context context){
        super(DIFF_CALLBACK);

        this.context = context;

    }

    public interface Action{

        void editBattery(int position);
        void deleteBattery(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.product_list_item, parent, false);

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


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        ProductPojo.ProductItem item = getItem(position);


        if (item != null) {
            Log.d("12345", "item  "+item.getBrand_name());

            holder.batteryBrandNameTV.setText(item.getBrand_name());
            holder.batterySkuTV.setText(item.getBattery_sku());
            holder.batterySizeTV.setText(item.getBattery_size());

            Picasso.get()
                    .load(item.getBattery_image())
                    .placeholder(R.drawable.app_logo)
                    .into(holder.batteryImageView);
        }
    }


    private static DiffUtil.ItemCallback<ProductPojo.ProductItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ProductPojo.ProductItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull ProductPojo.ProductItem oldItem, @NonNull ProductPojo.ProductItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ProductPojo.ProductItem oldItem, @NonNull ProductPojo.ProductItem newItem) {
                    return !(oldItem != newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView batteryBrandNameTV, batterySkuTV,  batterySizeTV;
        ImageView batteryImageView, editListBtn, deleteListBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            batteryImageView = itemView.findViewById(R.id.batteryImageView);
            batteryBrandNameTV = itemView.findViewById(R.id.batteryBrandNameTV);
            batterySkuTV = itemView.findViewById(R.id.batterySkuTV);
            batterySizeTV = itemView.findViewById(R.id.batterySizeTV);
            editListBtn = itemView.findViewById(R.id.editListBtn);
            deleteListBtn = itemView.findViewById(R.id.deleteListBtn);
        }
    }

}
