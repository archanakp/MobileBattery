package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.OverTimeListAdapter;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.OverTimeLsitPojo;
import android.app.mobilebattery.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OverTimeFragment extends Fragment implements OverTimeListAdapter.Accept{

    private ListView overTimeList;
    private RelativeLayout actionLayout;
    private RadioGroup actionRadioGroup;
    private RadioButton acceptOrDeny;
    private Button saveActionBtn;

    private TextView agentName, jobID, jobIssue;

    private String requestId;
    private OverTimeListAdapter overTimeListAdapter;
    private ArrayList<OverTimeLsitPojo> overTimeLsitArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.overtime_fragment, container, false);
        
        init(view);
        loadOverTimeList();
        methodListener();
        
        return view;
    }

    private void init(View view) {

        jobIssue = view.findViewById(R.id.jobIssue);
        jobID = view.findViewById(R.id.jobID);
        agentName = view.findViewById(R.id.agentName);
        actionLayout = view.findViewById(R.id.actionLayout);
        actionRadioGroup = view.findViewById(R.id.actionRadioGroup);
        saveActionBtn = view.findViewById(R.id.saveActionBtn);
        overTimeList = view.findViewById(R.id.overTimeList);


    }

    private void methodListener() {
        actionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionLayout.setVisibility(View.GONE);
            }
        });

        saveActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map = new HashMap<>();
                acceptOrDeny = getView().findViewById(actionRadioGroup.getCheckedRadioButtonId());
                if (acceptOrDeny.getText().toString().equals("Approve")){
                    map.put("is_accepted","1");
                }else if (acceptOrDeny.getText().toString().equals("Deny")){
                    map.put("is_accepted","0");
                }

                acceptOrReject(map);
            }
        });
    }

    private void acceptOrReject(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_Helper.OVERTIME_APPROVE_URL+requestId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            actionLayout.setVisibility(View.GONE);

                            if (message.equals("Overtime request denied.")){
                                Snackbar.make(getView(), "Overtime request denied.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else if (message.equals("Overtime request approved.")){
                                Snackbar.make(getView(), "Overtime request approved.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else if (message.equals("Accepted status cannot be empty.")){
                                Snackbar.make(getView(), "Accepted status cannot be empty.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }

                            loadOverTimeList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Snackbar.make(getView(), "Network Error", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(stringRequest);

    }

    private void loadOverTimeList() {
        overTimeLsitArrayList = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        final JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_Helper.OVERTIME_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0; i<array.length(); i++){
                                JSONObject object = array.getJSONObject(i);

                                OverTimeLsitPojo pojo = new OverTimeLsitPojo();
                                pojo.setId(object.getString("id"));
                                pojo.setAgent_id(object.getString("agent_id"));
                                pojo.setJob_id(object.getString("job_id"));
                                pojo.setAction(object.getString("action"));
                                pojo.setAgent_name(object.getString("agent_name"));
                                pojo.setJob_issue(object.getString("job_issue"));
                                pojo.setJob_receipt_no(object.getString("job_receipt_no"));
                                pojo.setCreated_at(object.getString("created_at"));
                                pojo.setUpdated_at(object.getString("updated_at"));

                                overTimeLsitArrayList.add(pojo);

                            }

                            showList();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Snackbar.make(getView(), "Error in Network",Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                });
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(objectRequest);
    }

    private void showList() {
        if (getActivity()!=null) {

            overTimeListAdapter = new OverTimeListAdapter(getActivity(), R.layout.overtime_list_item, overTimeLsitArrayList);
            overTimeListAdapter.accept = this;
            overTimeList.setAdapter(overTimeListAdapter);
        }
    }

    @Override
    public void acceptOverTime(int position, Boolean isAccepted) {

        OverTimeLsitPojo pojo = overTimeLsitArrayList.get(position);
        requestId = pojo.getId();
        agentName.setText(pojo.getAgent_name());
        jobID.setText(pojo.getJob_id());
        jobIssue.setText(pojo.getJob_issue());

        actionLayout.setVisibility(View.VISIBLE);

    }
}
