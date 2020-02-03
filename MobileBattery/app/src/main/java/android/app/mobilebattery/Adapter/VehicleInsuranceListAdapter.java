package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.VehicleInsuraceListPojo;
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

public class VehicleInsuranceListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<VehicleInsuraceListPojo> arrayList;
    LayoutInflater inflater;


    public EditInsuranceDetails editDetails;
    public DeleteInsuranceDetails deleteDetails;

    public VehicleInsuranceListAdapter(Context context, int resource, ArrayList<VehicleInsuraceListPojo> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface EditInsuranceDetails {
        void editInsuranceDetails(int position);
    }
    public interface DeleteInsuranceDetails {
        void deleteInsuranceDetails(int position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = inflater.inflate(R.layout.vehicle_insurance_list_item, null);

        TextView insIdTV = view.findViewById(R.id.insIdTV);
        TextView insDateTV = view.findViewById(R.id.insDateTV);
        TextView renewalDateTV = view.findViewById(R.id.renewalDateTV);
        ImageView editBtn = view.findViewById(R.id.editBtn);
        ImageView deleteBtn = view.findViewById(R.id.deleteBtn);

        VehicleInsuraceListPojo pojo = arrayList.get(position);
        insIdTV.setText(pojo.getInsurance_id());
        insDateTV.setText(pojo.getInsurance_date());
        renewalDateTV.setText(pojo.getRenew_date());

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editDetails!=null)
                    editDetails.editInsuranceDetails(position);
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
                            deleteDetails.deleteInsuranceDetails(position);
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
