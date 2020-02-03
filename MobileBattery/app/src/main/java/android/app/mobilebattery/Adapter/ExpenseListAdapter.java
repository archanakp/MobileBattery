package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.ExpenseListPojo;
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

import java.util.ArrayList;
import java.util.List;

public class ExpenseListAdapter extends PagedListAdapter<ExpenseListPojo.ExpenseListItem, ExpenseListAdapter.ItemViewHolder> {

    Context context;
    public EditItemData editItemData;
    public DeleteItemData deleteItemData;

    public ExpenseListAdapter(Context context) {
        super(DIFF_CALLBACK);

        this.context = context;
    }

    public interface EditItemData{
        void editExpenseData(int position);
    }
    public interface DeleteItemData{
        void deleteExpenseData(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.expense_fragment_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.editItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editItemData != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        editItemData.editExpenseData(holder.getAdapterPosition());
                }
            }
        });

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deleteItemData != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        deleteItemData.deleteExpenseData(holder.getAdapterPosition());
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, final int position) {

        ExpenseListPojo.ExpenseListItem item = getItem(position);

        if (item != null){
            holder.purposeTV.setText(item.getPurpose());
            holder.amountTV.setText("$ "+item.getAmount().toString());
            holder.dateTV.setText(item.getDate());


        }

    }

    private static DiffUtil.ItemCallback<ExpenseListPojo.ExpenseListItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ExpenseListPojo.ExpenseListItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull ExpenseListPojo.ExpenseListItem oldItem, @NonNull ExpenseListPojo.ExpenseListItem newItem) {
                    return oldItem == newItem;
                }

                @Override
                public boolean areContentsTheSame(@NonNull ExpenseListPojo.ExpenseListItem oldItem, @NonNull ExpenseListPojo.ExpenseListItem newItem) {
                    return !(oldItem != newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder{

        TextView purposeTV, amountTV, dateTV;
        ImageView editItem, deleteItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTV = itemView.findViewById(R.id.dateTV);
            amountTV = itemView.findViewById(R.id.amountTV);
            purposeTV = itemView.findViewById(R.id.purposeTV);
            editItem = itemView.findViewById(R.id.editItem);
            deleteItem = itemView.findViewById(R.id.deleteItem);
        }
    }

}
