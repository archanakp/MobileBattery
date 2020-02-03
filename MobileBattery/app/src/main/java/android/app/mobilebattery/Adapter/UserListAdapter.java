package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.UserListPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends ArrayAdapter<UserListPojo> implements Filterable  {

    Context context;
    int resource;
    ArrayList<UserListPojo> arrayList;
    LayoutInflater inflater;

    public EditDetails editDetails;
    public DeleteDetails deleteDetails;

    public UserListAdapter(Context context, int resource, ArrayList<UserListPojo> arrayList) {
        super(context, resource, arrayList);
        this.context = context;
        this.resource = resource;
        this.arrayList = arrayList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public interface EditDetails{
        void editUserDetails(int position);
    }
    public interface DeleteDetails{
        void deleteUserDetails(int position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.user_list_item, null);

        TextView name = view.findViewById(R.id.userNameTextView);
        TextView email = view.findViewById(R.id.emailTextView);
        TextView contactNo = view.findViewById(R.id.contactTextView);
        ImageView editBtn = view.findViewById(R.id.editUserBtn);
        ImageView deleteBtn = view.findViewById(R.id.deleteUserBtn);
        CircleImageView userIcon = view.findViewById(R.id.userIcon);

        UserListPojo pojo = arrayList.get(position);

        name.setText(pojo.getName());
        email.setText(pojo.getEmail());
        contactNo.setText(pojo.getPhone());
        Picasso.get()
                .load(pojo.getProfile_image())
                .placeholder(R.drawable.person)
                .error(R.drawable.person)
                .into(userIcon);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editDetails!=null)
                    editDetails.editUserDetails(position);
//                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setTitle("Edit User Details Alert!");
//                builder.setMessage("Are you sure to edit this user details ?");
//                builder.setCancelable(false);
//                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        builder.setCancelable(true);
//                        if(editDetails!=null)
//                            editDetails.editUserDetails(position);
//
//                    }
//                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        builder.setCancelable(true);
//                    }
//                });
//                builder.show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete User Details Alert!");
                builder.setMessage("Are you sure to delete this user details ?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                        if(deleteDetails!=null)
                            deleteDetails.deleteUserDetails(position);
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

    Filter myFilter = new Filter() {

        @Override
        public FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<UserListPojo> tempList=new ArrayList<>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(constraint != null && arrayList!=null) {
                int length=arrayList.size();
                int i=0;
                while(i<length){
                    UserListPojo item=arrayList.get(i);
                    //do whatever you wanna do here
                    //adding result set output array

                    tempList.add(item);

                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            arrayList = (ArrayList<UserListPojo>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    @Override
    public Filter getFilter() {
        return myFilter;
    }
}
