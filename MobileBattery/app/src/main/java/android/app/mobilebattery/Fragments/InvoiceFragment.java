package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.InvoiceListAdapter;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.InvoiceListPojo;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class InvoiceFragment extends Fragment {


    private ListView invoiceList;
    private Spinner customerIDSpinner;
    private TextView noResultFoundTextView;
    private EditText searchByNameET;
    private ImageButton refreshFilter;

    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> customerNameList;
    private ArrayList<String> customerIdList;

    private ArrayList<InvoiceListPojo> invoiceListArrayList;
    private InvoiceListAdapter incentiveListAdapter;

    private String cusId;

    public InvoiceFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.invoice_fragment, container, false);

        init(view);
        getCustomerList();
        methodListener();
        loadDefaultInvoiceList();
        searchByName();

        return view;
    }

    private void init(View view) {

        refreshFilter = view.findViewById(R.id.refreshFilter);
        searchByNameET = view.findViewById(R.id.searchByNameET);
        noResultFoundTextView = view.findViewById(R.id.noResultFoundTextView);
        invoiceList = view.findViewById(R.id.invoiceList);
        customerIDSpinner = view.findViewById(R.id.customerIDSpinner);
    }

    private void methodListener() {
        customerIDSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (customerNameList.get(position).equals("Customer Name")){
//                    Snackbar.make(getView(), "Select A Customer Name", Snackbar.LENGTH_SHORT)
//                            .setAction("Action", null).show();
                }else {
                    cusId = customerIdList.get(position);
                    Log.d("12345", cusId);
                    loadInvoiceList();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        refreshFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadDefaultInvoiceList();
                customerIDSpinner.setSelection(0);
            }
        });
    }

    private void loadDefaultInvoiceList() {
        invoiceListArrayList =new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_Helper.INVOICE_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", array.length()+"  "+array.toString());
                            if (array.length() == 0){
                                invoiceList.setVisibility(View.GONE);
                                noResultFoundTextView.setVisibility(View.VISIBLE);
                            }else {
                                noResultFoundTextView.setVisibility(View.GONE);
                                invoiceList.setVisibility(View.VISIBLE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    InvoiceListPojo pojo = new InvoiceListPojo();
                                    pojo.setId(object.getString("id"));
                                    pojo.setCustomer_id(object.getString("customer_id"));
                                    pojo.setCustomer(object.getString("customer"));
                                    pojo.setJob_id(object.getString("job_id"));
                                    pojo.setJob(object.getString("job"));
                                    pojo.setAmount(object.getString("amount"));
                                    pojo.setStatus(object.getString("status"));
                                    pojo.setInvoice_date(object.getString("invoice_date"));
                                    pojo.setJob_receipt_no(object.getString("job_receipt_no"));
                                    pojo.setCreated_at(object.getString("created_at"));
                                    pojo.setUpdated_at(object.getString("updated_at"));

                                    invoiceListArrayList.add(pojo);
                                }

                                showIncentiveList(invoiceListArrayList);
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

    private void loadInvoiceList() {
        final ArrayList<InvoiceListPojo> arrayList=new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        Log.d("12345", "URL "+URL_Helper.INVOICE_LIST_URL + cusId);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL_Helper.INVOICE_LIST_URL+"?id="+ cusId, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", array.length()+"  "+array.toString());
                            if (array.length() == 0){
                                invoiceList.setVisibility(View.GONE);
                                noResultFoundTextView.setVisibility(View.VISIBLE);
                            }else {
                                noResultFoundTextView.setVisibility(View.GONE);
                                invoiceList.setVisibility(View.VISIBLE);
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject object = array.getJSONObject(i);

                                    InvoiceListPojo pojo = new InvoiceListPojo();
                                    pojo.setId(object.getString("id"));
                                    pojo.setCustomer_id(object.getString("customer_id"));
                                    pojo.setCustomer(object.getString("customer"));
                                    pojo.setJob_id(object.getString("job_id"));
                                    pojo.setJob(object.getString("job"));
                                    pojo.setAmount(object.getString("amount"));
                                    pojo.setStatus(object.getString("status"));
                                    pojo.setInvoice_date(object.getString("invoice_date"));
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

    private void showIncentiveList(ArrayList<InvoiceListPojo> arrayList) {
        if (getActivity()!=null) {

            incentiveListAdapter = new InvoiceListAdapter(getActivity(), R.layout.incentive_fragment_list_item, arrayList);
            invoiceList.setAdapter(incentiveListAdapter);
        }
    }

    private void getCustomerList(){
        String URL = URL_Helper.CUSTOMER_list_URL;
        customerNameList = new ArrayList<>();
        customerNameList.add("Customer Name");
        customerIdList = new ArrayList<>();
        customerIdList.add("");

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);
                                customerIdList.add(object.getString("id"));
                                customerNameList.add(object.getString("name"));
                            }

                            if (getActivity()!= null)
                                spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, customerNameList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            customerIDSpinner.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12345", error.toString());
                        dialog.cancel();
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void searchByName(){

        searchByNameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final ArrayList<InvoiceListPojo> user = new ArrayList<>();
                for (int i=0; i<invoiceListArrayList.size(); i++){
                    InvoiceListPojo listPojo= invoiceListArrayList.get(i);
                    if (listPojo.getCustomer().toLowerCase().contains(s.toString().toLowerCase())){
                        InvoiceListPojo pojo = new InvoiceListPojo();
                        pojo.setId(listPojo.getId());


                        pojo.setCustomer_id(listPojo.getCustomer_id());
                        pojo.setCustomer(listPojo.getCustomer());
                        pojo.setJob_id(listPojo.getJob_id());
                        pojo.setJob(listPojo.getJob());
                        pojo.setAmount(listPojo.getAmount());
                        pojo.setStatus(listPojo.getStatus());
                        pojo.setInvoice_date(listPojo.getInvoice_date());

                        pojo.setCreated_at(listPojo.getCreated_at());
                        pojo.setUpdated_at(listPojo.getUpdated_at());
                        user.add(pojo);

                    }
                }
                if (user.size()<1){
                    noResultFoundTextView.setVisibility(View.VISIBLE);
                    invoiceList.setVisibility(View.GONE);
                }else {
                    noResultFoundTextView.setVisibility(View.GONE);
                    invoiceList.setVisibility(View.VISIBLE);
                    showIncentiveList(user);}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

}
