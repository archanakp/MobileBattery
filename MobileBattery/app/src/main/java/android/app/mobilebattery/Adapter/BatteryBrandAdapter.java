package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.BatteryBrandPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BatteryBrandAdapter extends PagedListAdapter<BatteryBrandPojo.BatteryBrandItem, BatteryBrandAdapter.ItemViewHolder> {

    private Context context;

    public EditListItem editListItem;
    public DeleteListItem deleteListItem;

    public BatteryBrandAdapter(Context context) {
        super(DIFF_CALLBACK);

        this.context =context;
    }

    public interface EditListItem{
        void editBrandDetails(int position);
    }
    public interface DeleteListItem{
        void deleteBrandDetails(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.battery_brand_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editListItem != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        editListItem.editBrandDetails(holder.getAdapterPosition());
                }
            }
        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteListItem != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        deleteListItem.deleteBrandDetails(holder.getAdapterPosition());

                }
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "item clicked !!!", Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

        final BatteryBrandPojo.BatteryBrandItem item = getItem(position);

        holder.brandName.setText(item.getBrand_name());

        if (item.getStatus()){
            holder.statusBcg.setBackgroundResource(R.color.greenBcg);
            holder.status.setText("Active");
        }else {
            holder.statusBcg.setBackgroundResource(R.color.redBcg);
            holder.status.setText("Inactive");
        }

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(item.getCreated_at());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newstr = new SimpleDateFormat("yyyy-MM-dd").format(date);

        holder.createDate.setText(newstr);

//        holder.editItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (editListItem != null){
//                    editListItem.editBrandDetails(holder.getAdapterPosition());
//                }
//            }
//        });
//
//        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (deleteListItem != null){
//                    deleteListItem.deleteBrandDetails(holder.getAdapterPosition());
//
//                }
//            }
//        });


    }

    private static DiffUtil.ItemCallback<BatteryBrandPojo.BatteryBrandItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BatteryBrandPojo.BatteryBrandItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull BatteryBrandPojo.BatteryBrandItem oldItem, @NonNull BatteryBrandPojo.BatteryBrandItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull BatteryBrandPojo.BatteryBrandItem oldItem, @NonNull BatteryBrandPojo.BatteryBrandItem newItem) {
                    return !(oldItem != newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView brandName, status, createDate;
        RelativeLayout statusBcg;
        ImageView editItem, deleteItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            brandName = itemView.findViewById(R.id.brandName);
            status = itemView.findViewById(R.id.status);
            createDate = itemView.findViewById(R.id.createDate);
            statusBcg = itemView.findViewById(R.id.statusBcg);
            editItem = itemView.findViewById(R.id.editItem);
            deleteItem = itemView.findViewById(R.id.deleteItem);
        }
    }

}
