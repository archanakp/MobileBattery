package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.VehicleServiceListPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

public class VehicleServiceListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<VehicleServiceListPojo> arrayList;
    LayoutInflater inflater;

    public EditServiceDetails editDetails;
    public DeleteServiceDetails deleteDetails;

    public VehicleServiceListAdapter(Context context, int resource, ArrayList<VehicleServiceListPojo> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public interface EditServiceDetails {
        void editServiceDetails(int position);
    }
    public interface DeleteServiceDetails{
        void deleteServiceDetails(int position);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.vehicle_service_list_item, null);

        TextView idTextView = view.findViewById(R.id.idTextView);
        TextView serviceIdTextView = view.findViewById(R.id.serviceIdTextView);
        TextView kmCompletedTextView = view.findViewById(R.id.kmCompletedTextView);
        ImageView editBtn = view.findViewById(R.id.editBtn);
        ImageView deleteBtn = view.findViewById(R.id.deleteBtn);

        VehicleServiceListPojo pojo = arrayList.get(position);
        idTextView.setText(pojo.getService_id());
        serviceIdTextView.setText(pojo.getService_date());
        kmCompletedTextView.setText(pojo.getNext_service_date());

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editDetails!=null)
                    editDetails.editServiceDetails(position);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete Insurance History Details Alert!");
                builder.setMessage("Are you sure to delete this Insurance History details ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                        if(deleteDetails!=null)
                            deleteDetails.deleteServiceDetails(position);
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


        return view;
    }
}
