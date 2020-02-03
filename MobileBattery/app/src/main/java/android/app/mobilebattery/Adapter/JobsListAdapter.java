package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.JobsListPojo;
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

public class JobsListAdapter extends ArrayAdapter {

    Context context;
    int resource;
    ArrayList<JobsListPojo> jobsArrayList;
    LayoutInflater inflater;

    public AssignJob assignJob;
    public EditDetails editDetails;
    public DeleteDetails deleteDetails;


    public JobsListAdapter(Context context, int resource, ArrayList<JobsListPojo> jobsArrayList){
        super(context, resource, jobsArrayList);

        this.context = context;
        this.resource = resource;
        this.jobsArrayList = jobsArrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface AssignJob{
        void assignJob(int position);
    }
    public interface EditDetails{
        void editJobsDetails(int position);
    }
    public interface DeleteDetails{
        void deleteJobsDetails(int position);
    }

    @Override
    public int getCount() {
        return jobsArrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.jobs_list_item, null);

        TextView customerNameTV = view.findViewById(R.id.customerNameTV);
        TextView locationTV = view.findViewById(R.id.locationTV);
        TextView sourceOfCallTV = view.findViewById(R.id.sourceOfCallTV);
        TextView issueTV = view.findViewById(R.id.issueTV);
        final ImageView assignJobBtn = view.findViewById(R.id.assignJobBtn);
        ImageView editJobBtn = view.findViewById(R.id.editJobBtn);
        ImageView deleteJobBtn = view.findViewById(R.id.deleteJobBtn);

        JobsListPojo pojo = jobsArrayList.get(position);

        customerNameTV.setText(pojo.getCustomer_name());
        locationTV.setText(pojo.getLocation());
        sourceOfCallTV.setText(pojo.getSource_of_call());
        issueTV.setText(pojo.getIssues_faced());

        assignJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (assignJob!=null)
                    assignJob.assignJob(position);

            }
        });

        editJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editDetails!=null)
                    editDetails.editJobsDetails(position);

            }
        });

        deleteJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete User Details Alert!");
                builder.setMessage("Are you sure to delete this user details ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                        if(deleteDetails!=null)
                            deleteDetails.deleteJobsDetails(position);
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
