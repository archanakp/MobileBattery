package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.VehiclePojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class VehicleAdapter extends PagedListAdapter<VehiclePojo.VehicleItem, VehicleAdapter.ItemViewHolder> {

    private Context context;

    public EditVehicle editVehicle;
    public DeleteVehicle deleteVehicle;
    public AssignVehicle assignVehicle;

    public VehicleAdapter(Context context) {
        super(DIFF_CALL_BACK);

        this.context = context;
    }


    public interface EditVehicle{
        void editVehicleDetails(int position);
    }
    public interface DeleteVehicle{
        void deleteVehicleDetails(int position);
    }
    public interface AssignVehicle{
        void assignVehicleDetails(int position);

        void clickedOnItem(int position);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.vehicle_list_item, parent, false);

        final ItemViewHolder holder = new ItemViewHolder(view);

        holder.assignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(assignVehicle!=null)
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        assignVehicle.assignVehicleDetails(holder.getAdapterPosition());

            }
        });

        holder.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editVehicle!=null)
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                        editVehicle.editVehicleDetails(holder.getAdapterPosition());

            }
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Vehicle Details Alert!");
                builder.setMessage("Are you sure to delete this Vehicle details ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                        if(deleteVehicle!=null) {
                            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION)
                                deleteVehicle.deleteVehicleDetails(holder.getAdapterPosition());
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                });
                builder.show();

            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (assignVehicle != null){
                    if (holder.getAdapterPosition() != RecyclerView.NO_POSITION){
                        assignVehicle.clickedOnItem(holder.getAdapterPosition());
                    }
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        VehiclePojo.VehicleItem item = getItem(position);

        if (item != null){

            holder.noPlateTextView.setText(item.number_plate);
            holder.regDateTextView.setText(item.registration_date);
            holder.techNameTextView.setText(item.technician_name);
            holder.kmReadingTextView.setText(item.km_completed+" KM");

//            holder.assignBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if(assignVehicle!=null)
//                        assignVehicle.assignVehicleDetails(position);
//
//                }
//            });
//
//            holder.editBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(editVehicle!=null)
//                        editVehicle.editVehicleDetails(position);
//
//                }
//            });
//
//            holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                    builder.setTitle("Delete Vehicle Details Alert!");
//                    builder.setMessage("Are you sure to delete this Vehicle details ?");
//                    builder.setCancelable(false);
//                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            builder.setCancelable(true);
//                            if(deleteVehicle!=null)
//                                deleteVehicle.deleteVehicleDetails(position);
//                        }
//                    });
//                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            builder.setCancelable(true);
//                        }
//                    });
//                    builder.show();
//
//                }
//            });

        }
    }

    private static DiffUtil.ItemCallback<VehiclePojo.VehicleItem> DIFF_CALL_BACK =
            new DiffUtil.ItemCallback<VehiclePojo.VehicleItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull VehiclePojo.VehicleItem oldItem, @NonNull VehiclePojo.VehicleItem newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull VehiclePojo.VehicleItem oldItem, @NonNull VehiclePojo.VehicleItem newItem) {
                    return !(oldItem != newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView noPlateTextView, regDateTextView, techNameTextView, kmReadingTextView;
        ImageView assignBtn, editBtn, deleteBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            noPlateTextView = itemView.findViewById(R.id.noPlateTextView);
            regDateTextView = itemView.findViewById(R.id.regDateTextView);
            techNameTextView = itemView.findViewById(R.id.techNameTextView);
            kmReadingTextView = itemView.findViewById(R.id.kmReadingTextView);
            assignBtn = itemView.findViewById(R.id.assignBtn);
            editBtn = itemView.findViewById(R.id.editBtn);
            deleteBtn = itemView.findViewById(R.id.deleteBtn);
        }
    }

}
