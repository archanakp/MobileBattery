package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.BatteryAllocatedListPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BatteryAllocatedListAddapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private ArrayList<BatteryAllocatedListPojo> arrayList;
    private LayoutInflater inflater;

    public BatteryAllocatedListAddapter (Context context, int resource, ArrayList<BatteryAllocatedListPojo> arrayList){
        super(context, resource, arrayList);

        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.allocated_stock_list_item, null);

        ImageView batteryImageView = view.findViewById(R.id.batteryImageView);
        TextView techNameTextView = view.findViewById(R.id.techNameTextView);
        TextView batteryBrandNameTextView = view.findViewById(R.id.batteryBrandNameTextView);
        TextView batterySkuTextView = view.findViewById(R.id.batterySkuTextView);
        TextView assignedQtyTextView = view.findViewById(R.id.assignedQtyTextView);
        TextView soldTextView = view.findViewById(R.id.soldTextView);
        TextView remainingQtyTextView = view.findViewById(R.id.remainingQtyTextView);

        BatteryAllocatedListPojo pojo = arrayList.get(position);

        techNameTextView.setText(pojo.getTechnician_name());
        batteryBrandNameTextView.setText(pojo.getBattery_brand());
        batterySkuTextView.setText(pojo.getBattery_sku());
        assignedQtyTextView.setText(pojo.getAssigned_quantity());
        soldTextView.setText(pojo.getSold_quantity());
        remainingQtyTextView.setText(pojo.getRemaining_quantity());

        Picasso.get()
                .load(pojo.getBattery_image())
                .placeholder(R.drawable.app_logo)
                .into(batteryImageView);


        return view;
    }
}
