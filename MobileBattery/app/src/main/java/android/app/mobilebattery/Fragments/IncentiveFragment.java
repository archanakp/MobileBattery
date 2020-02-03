package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.IncentiveListAdapter;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.IncentiveListPojo;
import android.app.mobilebattery.Pojo.UploadProductDataPojo;
import android.app.mobilebattery.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class IncentiveFragment extends Fragment {


    private ListView incentiveList;
    private Spinner customerCareSpinner;
    private TextView noResultFoundTextView;
    private Button addIncentive, saveBtn;
    private EditText searchByNameEt;
    private ImageButton resetFilter;

    private ImageView closeFormIV;
    private ScrollView newIncentiveForm;
    private Spinner userSpinner;
    private EditText amountET, receiptNoET;


    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> customerCareNameList;
    private ArrayList<String> customerCareIdList;

    private ArrayList<IncentiveListPojo> incentiveListArrayList;
    private IncentiveListAdapter incentiveListAdapter;

    private String cCareId, userId;

    public IncentiveFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.incentive_fragment, container, false);

        init(view);
        getCustomerCareList();
        methodListener();
        loadDefaultIncentiveList();
        searchByName();

        return view;
    }

    private void init(View view) {

        saveBtn = view.findViewById(R.id.saveBtn);
        closeFormIV = view.findViewById(R.id.closeFormIV);
        newIncentiveForm = view.findViewById(R.id.newIncentiveForm);
        userSpinner = view.findViewById(R.id.userSpinner);
        amountET = view.findViewById(R.id.amountET);
        receiptNoET = view.findViewById(R.id.receiptNoET);

        resetFilter = view.findViewById(R.id.resetFilter);
        searchByNameEt = view.findViewById(R.id.searchByNameEt);
        addIncentive = view.findViewById(R.id.addIncentive);
        noResultFoundTextView = view.findViewById(R.id.noResultFoundTextView);
        incentiveList = view.findViewById(R.id.incentiveList);
        customerCareSpinner = view.findViewById(R.id.customerCareSpinner);
    }

    private void methodListener() {

        addIncentive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIncentiveForm.setVisibility(View.VISIBLE);
                amountET.setText("");
                receiptNoET.setText("");
                userSpinner.setSelection(0);
            }
        });

        closeFormIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIncentiveForm.setVisibility(View.GONE);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String user = userSpinner.getSelectedItem().toString();
                String amt = amountET.getText().toString();
                String reciept_no = receiptNoET.getText().toString();

                if (user.equals("Users") || amt.equals("")){
                    Toast.makeText(getActivity(), "All Fields Required ", Toast.LENGTH_SHORT).show();
                }else {

                    Map<String, String> map = new HashMap<>();
                    map.put("agent_id", userId);
                    map.put("incentive_amount", amt);
                    map.put("lead_id", reciept_no);

                    submitIncentiveData(map);

                }
            }
        });

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (customerCareNameList.get(position).equals("Users")){
//                    Snackbar.make(getView(), "Select A Customer Care", Snackbar.LENGTH_SHORT)
//                            .setAction("Action", null).show();
                }else {
                    userId = customerCareIdList.get(position);

                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        customerCareSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (customerCareNameList.get(position).equals("Users")){
//                    Snackbar.make(getView(), "Select A Customer Care", Snackbar.LENGTH_SHORT)
//                            .setAction("Action", null).show();
                }else {
                    cCareId = customerCareIdList.get(position);
                    Log.d("12345", cCareId);
                    loadIncentiveList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIncentiveList(incentiveListArrayList);
                searchByNameEt.setText("");
                searchByNameEt.clearFocus();
                customerCareSpinner.setSelection(0);
                noResultFoundTextView.setVisibility(View.GONE);
                incentiveList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void submitIncentiveData(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        Log.d("12345", "map  "+map);

        NetworkClient.getInstance()
                .getApi()
                .submitIncentiveData(map.get("agent_id"),map.get("incentive_amount"),map.get("lead_id"))
                .enqueue(new Callback<UploadProductDataPojo>() {
                    @Override
                    public void onResponse(Call<UploadProductDataPojo> call, retrofit2.Response<UploadProductDataPojo> response) {
                        dialog.cancel();
                        if (response.body() != null){

                            String msg = response.body().getMessage();


                            if (msg.equals("Incentive Added Successfully.")) {
                                Toast.makeText(getActivity(), "Incentive Added Successfully.", Toast.LENGTH_SHORT).show();
                                newIncentiveForm.setVisibility(View.GONE);
                                loadDefaultIncentiveList();
                            }
                            else if (msg.equals("Incentive amount or agent id cannot be empty."))
                                Toast.makeText(getActivity(), "Incentive amount or agent id cannot be empty.", Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getContext(), "Network Error", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<UploadProductDataPojo> call, Throwable t) {
                        dialog.cancel();


                        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();

                    }
                });

//
//        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.POST, URL_Helper.ADD_INCENTIVE_URL, null,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        dialog.cancel();
//                        try {
//                            String msg = response.getString("message");
//
//                            Log.d("12345", "msg  "+msg+"  response  "+response);
//
//                            if (msg.equals("Incentive Added Successfully.")) {
//                                Toast.makeText(getActivity(), "Incentive Added Successfully.", Toast.LENGTH_SHORT).show();
//                                newIncentiveForm.setVisibility(View.GONE);
//                                loadDefaultIncentiveList();
//
//                            }
//                            else if (msg.equals("Incentive amount or agent id cannot be empty."))
//                                Toast.makeText(getActivity(), "Incentive amount or agent id cannot be empty.", Toast.LENGTH_SHORT).show();
//                            else
//                                Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//
//                        dialog.cancel();
//                        Toast.makeText(getActivity(), "Network Error", Toast.LENGTH_SHORT).show();
//
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                return map;
//            }
//        };
//        RequestQueue queue = Volley.newRequestQueue(getActivity());
//        queue.add(objectRequest);

    }

    private void loadDefaultIncentiveList() {
        incentiveListArrayList=new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        Log.d("12345", "URL "+URL_Helper.INCENTIVE_LIST_URL);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_Helper.INCENTIVE_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", array.length()+"  "+array.toString());
                            if (array.length() == 0){
                                incentiveList.setVisibility(View.GONE);
                                noResultFoundTextView.setVisibility(View.VISIBLE);
                            }else {
                                noResultFoundTextView.setVisibility(View.GONE);
                                incentiveList.setVisibility(View.VISIBLE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    IncentiveListPojo pojo = new IncentiveListPojo();
                                    pojo.setId(object.getString("id"));
                                    pojo.setAgent_id(object.getString("agent_id"));
                                    pojo.setLead_id(object.getString("lead_id"));
                                    pojo.setIncentive_amount(object.getString("incentive_amount"));
                                    pojo.setAgent_name(object.getString("agent_name"));
                                    pojo.setLead(object.getString("lead"));
                                    pojo.setLead_receipt_no(object.getString("lead_receipt_no"));
                                    pojo.setCreated_at(object.getString("created_at"));
                                    pojo.setUpdated_at(object.getString("updated_at"));

                                    incentiveListArrayList.add(pojo);
                                }

                                showIncentiveList(incentiveListArrayList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Snackbar.make(getView(), "Select A Customer Care", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(objectRequest);
    }

    private void loadIncentiveList() {
        final ArrayList<IncentiveListPojo> arrayList=new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        Log.d("12345", "URL "+URL_Helper.INCENTIVE_LIST_URL + cCareId);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_Helper.INCENTIVE_LIST_URL+"?id="+ cCareId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", "search by name  "+array.length()+"  "+array.toString());
                            if (array.length() == 0){
                                incentiveList.setVisibility(View.GONE);
                                noResultFoundTextView.setVisibility(View.VISIBLE);
                            }else {
                                noResultFoundTextView.setVisibility(View.GONE);
                                incentiveList.setVisibility(View.VISIBLE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    IncentiveListPojo pojo = new IncentiveListPojo();
                                    pojo.setId(object.getString("id"));
                                    pojo.setAgent_id(object.getString("agent_id"));
                                    pojo.setLead_id(object.getString("lead_id"));
                                    pojo.setIncentive_amount(object.getString("incentive_amount"));
                                    pojo.setAgent_name(object.getString("agent_name"));
                                    pojo.setLead(object.getString("lead"));
                                    pojo.setLead_receipt_no(object.getString("lead_receipt_no"));
                                    pojo.setCreated_at(object.getString("created_at"));
                                    pojo.setUpdated_at(object.getString("updated_at"));

                                    arrayList.add(pojo);
                                }

                                showIncentiveList(arrayList);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Snackbar.make(getView(), "Select A Customer Care", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(objectRequest);
    }

    private void showIncentiveList(ArrayList<IncentiveListPojo> incentiveListArrayList) {
        if (getActivity()!=null) {

            incentiveListAdapter = new IncentiveListAdapter(getActivity(), R.layout.incentive_fragment_list_item, incentiveListArrayList);
            incentiveList.setAdapter(incentiveListAdapter);
        }
    }

    private void getCustomerCareList(){
        String URL = URL_Helper.USER_LIST_URL+"?type=Customer Care Agent";
        customerCareNameList = new ArrayList<>();
//        customerCareNameList.add("Customer Care");
        customerCareNameList.add("Users");
        customerCareIdList= new ArrayList<>();
        customerCareIdList.add("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.USER_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);
                                customerCareIdList.add(object.getString("id"));
                                customerCareNameList.add(object.getString("name"));
                            }

                            spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, customerCareNameList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            customerCareSpinner.setAdapter(spinnerAdapter);
                            userSpinner.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12345", error.toString());
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void searchByName(){
        searchByNameEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final ArrayList<IncentiveListPojo> user = new ArrayList<>();
                for (int i=0; i<incentiveListArrayList.size(); i++){
                    IncentiveListPojo listPojo= incentiveListArrayList.get(i);
                    if (listPojo.getAgent_name().toLowerCase().contains(s.toString().toLowerCase())){
                        IncentiveListPojo pojo = new IncentiveListPojo();
                        pojo.setId(listPojo.getId());
                        pojo.setLead_id(listPojo.getLead_id());
                        pojo.setAgent_id(listPojo.getLead_id());
                        pojo.setLead(listPojo.getLead());
                        pojo.setAgent_name(listPojo.getAgent_name());
                        pojo.setIncentive_amount(listPojo.getIncentive_amount());
                        pojo.setCreated_at(listPojo.getCreated_at());
                        pojo.setUpdated_at(listPojo.getUpdated_at());
                        user.add(pojo);

                    }
                }
                if (user.size()<1){
                    noResultFoundTextView.setVisibility(View.VISIBLE);
                    incentiveList.setVisibility(View.GONE);
                }else {
                    noResultFoundTextView.setVisibility(View.GONE);
                    incentiveList.setVisibility(View.VISIBLE);
                    showIncentiveList(user);}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
