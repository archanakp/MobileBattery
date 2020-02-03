package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.IncentiveListPojo;
import android.app.mobilebattery.Pojo.InvoiceListPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class InvoiceListAdapter extends ArrayAdapter {

    private int resource;
    private Context context;
    private ArrayList<InvoiceListPojo> arrayList;
    private LayoutInflater inflater;


    public InvoiceListAdapter(Context context, int resource, ArrayList<InvoiceListPojo> arrayList){
        super(context, resource, arrayList);

        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.invoice_fragment_list_item, null);

        TextView customerName  = view.findViewById(R.id.customerName);
        TextView job = view.findViewById(R.id.jobName);
        TextView date = view.findViewById(R.id.date);
        TextView jobId = view.findViewById(R.id.jobId);
        TextView invoiceAmount = view.findViewById(R.id.invoiceAmount);
        TextView status = view.findViewById(R.id.status);
        RelativeLayout statusBcg = view.findViewById(R.id.statusBcg);

        final InvoiceListPojo pojo = arrayList.get(position);
        customerName.setText(pojo.getCustomer());
        job.setText(pojo.getJob());
        jobId.setText(pojo.getJob_receipt_no());
        date.setText(pojo.getInvoice_date());
        invoiceAmount.setText("$ "+pojo.getAmount());

        if (pojo.getStatus().equals("Pending"))
        {
            status.setText(pojo.getStatus());
            statusBcg.setBackgroundResource(R.color.redBcg);
//            status.setTextColor(Color.RED);
        }else {
            status.setText(pojo.getStatus());
            statusBcg.setBackgroundResource(R.color.greenBcg);
//            status.setTextColor(Color.GREEN);

        }


        return view;
    }
}
