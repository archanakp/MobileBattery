package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.SupplierListPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class SupplierAdapter extends PagedListAdapter<SupplierListPojo.SupplierItem, SupplierAdapter.ItemViewHolder> {


    private Context context;

    public Action action;

    public SupplierAdapter(Context context) {
        super(DIFF_CLLBACK);
        this.context = context;
    }

    public interface Action{

        void editSupplierDetails(int position);

        void deleteSupplierDetails(int position);

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.supplier_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.editSupplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.editSupplierDetails(holder.getAdapterPosition());
                }
            }
        });

        holder.deleteSupplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.deleteSupplierDetails(holder.getAdapterPosition());
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        SupplierListPojo.SupplierItem item = getItem(position);
        if (item != null) {
            holder.supplierNameTextView.setText(item.supplier_name);
            holder.supplierEmailTextView.setText(item.supplier_email);
            holder.supplierPhoneTextView.setText(item.supplier_phone);
        }
    }

    private static DiffUtil.ItemCallback<SupplierListPojo.SupplierItem> DIFF_CLLBACK =
            new DiffUtil.ItemCallback<SupplierListPojo.SupplierItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull SupplierListPojo.SupplierItem oldItem, @NonNull SupplierListPojo.SupplierItem newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull SupplierListPojo.SupplierItem oldItem, @NonNull SupplierListPojo.SupplierItem newItem) {
                    return !(oldItem != newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView supplierNameTextView, supplierEmailTextView, supplierPhoneTextView;
        ImageView editSupplierBtn, deleteSupplierBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            supplierNameTextView = itemView.findViewById(R.id.supplierNameTextView);
            supplierEmailTextView = itemView.findViewById(R.id.supplierEmailTextView);
            supplierPhoneTextView = itemView.findViewById(R.id.supplierPhoneTextView);
            editSupplierBtn = itemView.findViewById(R.id.editSupplierBtn);
            deleteSupplierBtn = itemView.findViewById(R.id.deleteSupplierBtn);
        }
    }

}
