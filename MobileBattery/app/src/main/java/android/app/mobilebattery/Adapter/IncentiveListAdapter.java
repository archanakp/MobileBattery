package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.IncentiveListPojo;
import android.app.mobilebattery.Pojo.OverTimeLsitPojo;
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

import java.util.ArrayList;

public class IncentiveListAdapter extends ArrayAdapter {

    private int resource;
    private Context context;
    private ArrayList<IncentiveListPojo> arrayList;
    private LayoutInflater inflater;


    public IncentiveListAdapter(Context context, int resource, ArrayList<IncentiveListPojo> arrayList){
        super(context, resource, arrayList);

        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface Accept{
        void acceptOverTime(int position, Boolean isAccepted);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.incentive_fragment_list_item, null);

        TextView agentName = view.findViewById(R.id.agentName);
        TextView leadId = view.findViewById(R.id.leadId);
        TextView incentiveAmount = view.findViewById(R.id.incentiveAmount);

        final IncentiveListPojo pojo = arrayList.get(position);
        agentName.setText(pojo.getAgent_name());
        leadId.setText(pojo.getLead_id());
        incentiveAmount.setText("$ "+pojo.getIncentive_amount());

        return view;
    }
}
