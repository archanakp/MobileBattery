package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.CustomerPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.media.Image;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAdapter extends PagedListAdapter<CustomerPojo.CustomerData, CustomerAdapter.ItemViewHolder> {

    private Context context;

    public Action action;

    public CustomerAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    public interface Action{
        void editCustomerDetails(int position);


        void deleteCustomerDetails(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.customer_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.editCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.editCustomerDetails(holder.getAdapterPosition());
                }
            }
        });

        holder.deleteCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (action != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        action.deleteCustomerDetails(holder.getAdapterPosition());
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        CustomerPojo.CustomerData item = getItem(position);

        if (item != null){
            Picasso.get()
                    .load(item.profile_image)
                    .placeholder(R.drawable.person)
                    .into(holder.profileImage);

            holder.customerUserNameTextView.setText(item.name);
            holder.customerEmailTextView.setText(item.email);
            holder.customerPhoneTextView.setText(item.phone.toString());

        }

    }

    private static DiffUtil.ItemCallback<CustomerPojo.CustomerData> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CustomerPojo.CustomerData>() {
                @Override
                public boolean areItemsTheSame(@NonNull CustomerPojo.CustomerData oldItem, @NonNull CustomerPojo.CustomerData newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull CustomerPojo.CustomerData oldItem, @NonNull CustomerPojo.CustomerData newItem) {
                    return oldItem.email.equals(newItem.email);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;
        TextView customerUserNameTextView, customerEmailTextView, customerPhoneTextView;
        ImageView editCustomerBtn, deleteCustomerBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.customerImageIcon);
            customerUserNameTextView = itemView.findViewById(R.id.customerUserNameTextView);
            customerEmailTextView = itemView.findViewById(R.id.customerEmailTextView);
            customerPhoneTextView = itemView.findViewById(R.id.customerPhoneTextView);
            editCustomerBtn = itemView.findViewById(R.id.editCustomerBtn);
            deleteCustomerBtn = itemView.findViewById(R.id.deleteCustomerBtn);

        }
    }

}
