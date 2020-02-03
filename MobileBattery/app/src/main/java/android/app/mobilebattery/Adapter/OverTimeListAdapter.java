package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.OverTimeLsitPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class OverTimeListAdapter extends ArrayAdapter {

    private int resource;
    private Context context;
    private ArrayList<OverTimeLsitPojo> arrayList;
    private LayoutInflater inflater;

    public Accept accept;

    public OverTimeListAdapter(Context context, int resource, ArrayList<OverTimeLsitPojo> arrayList){
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
        View view = inflater.inflate(R.layout.overtime_list_item, null);

        TextView agentName = view.findViewById(R.id.agentName);
        TextView jobId = view.findViewById(R.id.jobId);
        TextView jobIssue = view.findViewById(R.id.jobIssue);
        TextView isAccepted = view.findViewById(R.id.isAccepted);
        RelativeLayout statusBcg = view.findViewById(R.id.statusBcg);
//        ImageView acceptBtn = view.findViewById(R.id.acceptBtn);

        final OverTimeLsitPojo pojo = arrayList.get(position);
        agentName.setText(pojo.getAgent_name());
        jobId.setText(pojo.getJob_receipt_no());
        jobIssue.setText(pojo.getJob_issue());

        String AC = "Approved";
        String PD = "Pending";
        String NA = "Rejected";
        if (pojo.getAction().equals("Approve")){
            isAccepted.setText(AC);
            statusBcg.setBackgroundResource(R.color.greenBcg);
            isAccepted.setClickable(false);
        }else if (pojo.getAction().equals("Reject")){
            isAccepted.setText(NA);
            statusBcg.setBackgroundResource(R.color.redBcg);
            isAccepted.setClickable(false);
        }else {
            isAccepted.setText(PD);
            statusBcg.setBackgroundResource(R.color.yellowBcg);
            isAccepted.setClickable(true);
            isAccepted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (accept!=null){
                        accept.acceptOverTime(position, true);
                    }
                }
            });


        }



        return view;
    }
}
