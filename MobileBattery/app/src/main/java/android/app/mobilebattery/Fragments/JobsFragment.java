package android.app.mobilebattery.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.JobsListAdapter;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.JobsListPojo;
import android.app.mobilebattery.R;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobsFragment extends Fragment implements JobsListAdapter.AssignJob,
        JobsListAdapter.EditDetails, JobsListAdapter.DeleteDetails{

    private ListView jobsList;
    private Button addNewJobBtn;
    private RelativeLayout relativeLayout;
    private EditText searchByLocationET, searchSourceOfCallET;
    private TextView noResultFoundTV;
    private Spinner customerNameSpinner;
    private ImageButton resetFilter;

//    add Jobs form views
    private ScrollView addJobsForm;
    private ImageView closeAddJobsFormIV;
    private RadioGroup btWarrantyGPR, pushSaleGPR;
    private RadioButton isBtWarranty, isPushSale;
    private Spinner customerSpinner, offeredPriceTypeSpinner;
    private EditText locationET, sourceOfCallET, issuesET, quickNoteET, offeredPrice, currentBtSKUET, currentBtBrandET, currentBtSizeET;
    private EditText newBtSKUET, newBtBrandET, newBtSizeET;
    private Button saveJobsBtn;

//    assign job layout views
    private Button assignJobsToTechBtn;
    private Spinner techSpinner;
    private RelativeLayout assignJobLayout;
    private TextView custNameAssJob, vehicleNameAssJob, issueAssJob, locationAssJob;


    private ArrayList<JobsListPojo> jobsListArrayList;
    private JobsListAdapter jobsListAdapter;
    private String currentJobID, techId, custId, custVehicleId, searchcustId;
    private String submitType, currentJobId;


    private ArrayList<String> custNameList;
    private ArrayList<String> custIdList;
    private ArrayList<String> techNameList;
    private ArrayList<String> techIdList;
    private ArrayList<String> offerPriceType = new ArrayList<>();
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> cusSpinnerAdapter;
    private ArrayAdapter<String> pTypeSpinnerAdapter;
    private RequestQueue queue;
    private Activity activity;

    public JobsFragment(){/* Required empty public constructor*/}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.jobs_fragments, container, false);

        init(view);
        if (getActivity()!= null)
            activity = getActivity();

        queue = Volley.newRequestQueue(activity);
        loadJobsListData();
        methodListener();
        setupUI(relativeLayout);
        getTechList();
        getCustomerList();
        searchByLocation();
        searchBySourceOfCall();
        searchByCustomerName();

        offerPriceType.add("Price Type");
        offerPriceType.add("Lower Retail");
        offerPriceType.add("Retail");
        offerPriceType.add("Higher Retail");

        pTypeSpinnerAdapter = new ArrayAdapter<>(activity, R.layout.simple_spinner_item, offerPriceType);

        pTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        offeredPriceTypeSpinner.setAdapter(pTypeSpinnerAdapter);

        return view;
    }

    private void init(View view) {

//        assign jobs views
        assignJobsToTechBtn = view.findViewById(R.id.assignJobsToTechBtn);
        techSpinner = view.findViewById(R.id.techSpinner);
        assignJobLayout = view.findViewById(R.id.assignJobLayout);
        custNameAssJob = view.findViewById(R.id.custNameAssJob);
        vehicleNameAssJob = view.findViewById(R.id.vehicleNameAssJob);
        issueAssJob = view.findViewById(R.id.issueAssJob);
        locationAssJob = view.findViewById(R.id.locationAssJob);



//        add Jobs form find
        addJobsForm = view.findViewById(R.id.addJobsForm);
        closeAddJobsFormIV = view.findViewById(R.id.closeAddJobsFormIV);
        customerSpinner = view.findViewById(R.id.customerSpinner);
        offeredPriceTypeSpinner = view.findViewById(R.id.offeredPriceTypeSpinner);
        locationET = view.findViewById(R.id.locationET);
        sourceOfCallET = view.findViewById(R.id.sourceOfCallET);
        issuesET = view.findViewById(R.id.issuesET);
        quickNoteET = view.findViewById(R.id.quickNoteET);
        offeredPrice = view.findViewById(R.id.offeredPrice);
        currentBtSKUET = view.findViewById(R.id.currentBtSKUET);
        currentBtBrandET = view.findViewById(R.id.currentBtBrandET);
        currentBtSizeET = view.findViewById(R.id.currentBtSizeET);
        newBtSKUET = view.findViewById(R.id.newBtSKUET);
        newBtBrandET = view.findViewById(R.id.newBtBrandET);
        newBtSizeET = view.findViewById(R.id.newBtSizeET);
        saveJobsBtn = view.findViewById(R.id.saveJobsBtn);
        btWarrantyGPR = view.findViewById(R.id.btWarrantyGPR);
        pushSaleGPR = view.findViewById(R.id.pushSaleGPR);
        isBtWarranty = view.findViewById(btWarrantyGPR.getCheckedRadioButtonId());
        isPushSale = view.findViewById(pushSaleGPR.getCheckedRadioButtonId());



        customerNameSpinner = view.findViewById(R.id.customerNameSpinner);
        resetFilter = view.findViewById(R.id.resetFilter);
        noResultFoundTV = view.findViewById(R.id.noResultFoundTV);
        searchSourceOfCallET = view.findViewById(R.id.searchSourceOfCallET);
        searchByLocationET = view.findViewById(R.id.searchByLocationET);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        jobsList = view.findViewById(R.id.jobsList);
        addNewJobBtn = view.findViewById(R.id.addNewJobBtn);
    }

    private void methodListener() {

        closeAddJobsFormIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationET.setText("");
                sourceOfCallET.setText("");
                issuesET.setText("");
                quickNoteET.setText("");
                offeredPrice.setText("");
                currentBtSKUET.setText("");
                currentBtBrandET.setText("");
                currentBtSizeET.setText("");
                newBtSKUET.setText("");
                newBtBrandET.setText("");
                newBtSizeET.setText("");
                addJobsForm.setVisibility(View.GONE);
            }
        });

        addNewJobBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitType = "add";
                locationET.setText("");
                sourceOfCallET.setText("");
                issuesET.setText("");
                quickNoteET.setText("");
                offeredPrice.setText("");
                currentBtSKUET.setText("");
                currentBtBrandET.setText("");
                currentBtSizeET.setText("");
                newBtSKUET.setText("");
                newBtBrandET.setText("");
                newBtSizeET.setText("");

                addJobsForm.setVisibility(View.VISIBLE);
                customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (custNameList.get(position).equals("Select Customer")){
                            Log.d("12345", "");
//                            Toast.makeText(getActivity(), "Select Technician", Toast.LENGTH_SHORT).show();
                        }else {
                            custId = custIdList.get(position);
                            Log.d("12345", "custId "+custId);
                            getCustomerVehicleId(custId);
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
//                        parent.getAdapter().
                    }
                });
            }
        });

        assignJobLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assignJobLayout.setVisibility(View.GONE);

            }
        });


        saveJobsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               submitJobsDataToServer(view);
            }
        });


        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList(jobsListArrayList);
                searchByLocationET.setText("");
                searchByLocationET.clearFocus();
                searchSourceOfCallET.setText("");
                searchSourceOfCallET.clearFocus();
                customerNameSpinner.setSelection(0);
                noResultFoundTV.setVisibility(View.GONE);
                jobsList.setVisibility(View.VISIBLE);
            }
        });


    }

    private void submitJobsDataToServer(View view) {

        String loc = locationET.getText().toString();
        String sOfCall = sourceOfCallET.getText().toString();
        String issue = issuesET.getText().toString();
        String qNote = quickNoteET.getText().toString();


        String ofrdPriceType = offeredPriceTypeSpinner.getSelectedItem().toString();
        String ofrdPrice = offeredPrice.getText().toString();
        String cbSKU = currentBtSKUET.getText().toString();
        String cbBrand = currentBtBrandET.getText().toString();
        String cbSize = currentBtSizeET.getText().toString();
        String nbSKU = newBtSKUET.getText().toString();
        String nbBrand = newBtBrandET.getText().toString();
        String nbSize = newBtSizeET.getText().toString();

        if (loc.equals("") || sOfCall.equals("") || issue.equals("") || qNote.equals("") ||
                ofrdPrice.equals("") || cbSKU.equals("") || cbBrand.equals("") || cbSize.equals("") || nbSKU.equals("") ||
                nbBrand.equals("") ||nbSize.equals("")){
            Snackbar.make(view, "All fields Required! ", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }else if (ofrdPriceType.equals("Price Type")){
            Snackbar.make(view, "Please select valid offered Price Type.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }/*else if (ofrdPriceType.equals("Price Type")){
            Snackbar.make(view, "Please select valid offered Price Type.", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }*/ else {

            String IsWar, IsPushSale;
            if (isBtWarranty.getText().toString().equals("Yes")){
                IsWar="1";
            }else {IsWar="0";}

            if (isBtWarranty.getText().toString().equals("Yes")){
                IsPushSale="1";
            }else {IsPushSale="0";}


            Map<String, String> map = new HashMap<>();
            map.put("vehicle_id", custVehicleId);
            map.put("customer_id", custId);
            map.put("location", loc);
            map.put("source_of_call", sOfCall);
            map.put("issues_faced", issue);
            map.put("quick_note", qNote);
            map.put("is_warranty", IsWar);
            map.put("push_sale", IsPushSale);
            map.put("price_offered_type", ofrdPriceType);
            map.put("price_offered", ofrdPrice);
            map.put("New[battery_sku]", nbSKU);
            map.put("New[battery_brand]", nbBrand);
            map.put("New[battery_size]", nbSize);
            map.put("Current[battery_sku]", cbSKU);
            map.put("Current[battery_brand]", cbBrand);
            map.put("Current[battery_size]", cbSize);

            Log.d("12345", map.toString());
            addNewJobs(map);
        }
    }

    private void addNewJobs(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String submit_URL;

        if (submitType.equals("add")){
            submit_URL = URL_Helper.ADD_JOBS_URL;
        }else {
//            submit_URL = URL_Helper.EDIT_JOBS_URL+"1";
            submit_URL = URL_Helper.EDIT_JOBS_URL+currentJobId;
        }

        StringRequest request = new StringRequest(Request.Method.POST, submit_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        Log.d("12345", response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Customer id, vehicle id, location, issue faces, offered price, type, warranty, push sale, new or current battery details cannot be empty.")){
                                Toast.makeText(activity, "All fields Required", Toast.LENGTH_SHORT).show();
//                                Snackbar.make(getView(),"All fields Required",Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }else if (message.equals("Job Added Successfully.")){
                                addJobsForm.setVisibility(View.GONE);
                                Snackbar.make(getView(),"Job Added Successfully.",Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }else if (message.equals("Job details updated successfully.")){
                                addJobsForm.setVisibility(View.GONE);
                                Snackbar.make(getView(),"Job details updated Successfully.",Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            }else if (message.equals("Job id is invalid.")){
                                Snackbar.make(getView(),"Job details updated Successfully.",Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
                        Log.d("12345", error.toString());

                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private void loadJobsListData() {
        jobsListArrayList = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.JOBS_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);

                                JobsListPojo pojo = new JobsListPojo();
                                pojo.setId(object.getString("id"));
                                pojo.setCustomer_id(object.getString("customer_id"));
                                pojo.setVehicle_id(object.getString("vehicle_id"));
                                pojo.setLocation(object.getString("location"));
                                pojo.setSource_of_call(object.getString("source_of_call"));
                                pojo.setIssues_faced(object.getString("issues_faced"));

                                pojo.setPrice_offered_type(object.getString("price_offered_type"));
                                pojo.setPrice_offered(object.getString("price_offered"));
                                pojo.setQuick_note(object.getString("quick_note"));
                                pojo.setJob_status(object.getString("job_status"));
                                pojo.setCancellation_reason(object.getString("cancellation_reason"));
                                pojo.setReceipt_no(object.getString("receipt_no"));
                                pojo.setCreated_at(object.getString("created_at"));
                                pojo.setUpdated_at(object.getString("updated_at"));

                                pojo.setCustomer_name(object.getString("customer_name"));
                                pojo.setVehicle_name(object.getString("vehicle_name"));
                                pojo.setVehicle_model(object.getString("vehicle_model"));
                                pojo.setReaschedule_reason(object.getString("reaschedule_reason"));
                                pojo.setAllocated_to(object.getString("allocated_to"));

                                pojo.setIs_reschedule(object.getBoolean("is_reschedule"));
                                pojo.setReschedule_accepted(object.getBoolean("reschedule_accepted"));
                                pojo.setIs_warranty(object.getBoolean("is_warranty"));
                                pojo.setPush_sale(object.getBoolean("push_sale"));
                                pojo.setPush_sale(object.getBoolean("push_sale"));
                                pojo.setCash_received(object.getBoolean("cash_received"));
                                pojo.setOvertime(object.getBoolean("overtime"));


                                jobsListArrayList.add(pojo);
                            }

                            showList(jobsListArrayList);
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
                        Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void showList(ArrayList<JobsListPojo> jobsListArrayList) {

        if (getActivity()!=null) {

            jobsListAdapter = new JobsListAdapter(getActivity(),R.layout.jobs_list_item, jobsListArrayList);
            jobsListAdapter.assignJob = this;
            jobsListAdapter.editDetails = this;
            jobsListAdapter.deleteDetails = this;
            jobsList.setAdapter(jobsListAdapter);
        }
    }

    @Override
    public void assignJob(int position) {

        assignJobLayout.setVisibility(View.VISIBLE);
        JobsListPojo pojo = jobsListArrayList.get(position);
        currentJobID = pojo.getId();

        custNameAssJob.setText(pojo.getCustomer_name());
        vehicleNameAssJob.setText(pojo.getVehicle_name());
        issueAssJob.setText(pojo.getIssues_faced());
        locationAssJob.setText(pojo.getLocation());

        techSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (techNameList.get(position).equals("Select Tech.")){
//                    Toast.makeText(getActivity(), "Select Technician", Toast.LENGTH_SHORT).show();
                }else {
                    techId = techIdList.get(position);}

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        assignJobsToTechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, String> map = new HashMap<>();
                map.put("job_id", currentJobID);
                map.put("technician_id", techId);

                submitAssignJob(map);

            }
        });


    }

    private void submitAssignJob(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ASSIGN_JOBS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Job assigned successfully.")){
                                Snackbar.make(getView(), "Job assigned successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                assignJobLayout.setVisibility(View.GONE);

                            }else if (message.equals("Job id, technician id cannot be empty.")){
                                Snackbar.make(getView(), "Job id, technician id cannot be empty.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
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
                        Log.d("12345", " error " + error.toString());
                        Snackbar.make(getView(), " Error in network", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };



        queue.add(request);

    }

    @Override
    public void editJobsDetails(int position) {

        JobsListPojo pojo = jobsListArrayList.get(position);

        submitType = "edit";
        currentJobId = pojo.getId();
        getJobsDetails(pojo.getId());

    }

    private void getJobsDetails(String id) {
//        jobDetailsArrayList = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.JOB_DETAILS_URL + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            String message = response.getString("message");
                            if (message.equals("Job id is invalid.")){

                            }else {
                                JSONArray array = response.getJSONArray("data");
                                JSONObject object = array.getJSONObject(0);
                                addJobsForm.setVisibility(View.VISIBLE);

                                String customerName = object.getString("customer_name");
                                Log.d("12345", customerName);
                                if (!customerName.equals("")){
                                    int spinnerPosition = cusSpinnerAdapter.getPosition(customerName);
                                    Log.d("12345", "position  "+spinnerPosition);
                                    customerSpinner.setSelection(spinnerPosition+1);

                                    custId = custIdList.get(spinnerPosition);
                                    Log.d("12345", "custId "+custId);
                                    getCustomerVehicleId(custId);
                                }

                                locationET.setText(object.getString("location"));
                                sourceOfCallET.setText(object.getString("source_of_call"));
                                issuesET.setText(object.getString("issues_faced"));
                                quickNoteET.setText(object.getString("quick_note"));

                                String offeredPriceType = object.getString("price_offered_type");
                                Log.d("12345", offeredPriceType);
                                if (!offeredPriceType.equals("")){
                                    int spinnerPosition = pTypeSpinnerAdapter.getPosition(offeredPriceType);
                                    offeredPriceTypeSpinner.setSelection(spinnerPosition);
                                }
//                                offeredPriceTypeSpinner.getSelectedItem().toString();
                                offeredPrice.setText(object.getString("price_offered"));

                                JSONArray cBatteryArray = object.getJSONArray("current_battery");
                                JSONObject object1 = cBatteryArray.getJSONObject(0);
                                currentBtSKUET.setText(object1.getString("battery_sku"));
                                currentBtBrandET.setText(object1.getString("battery_brand"));
                                currentBtSizeET.setText(object1.getString("battery_size"));

                                JSONArray nBatteryArray = object.getJSONArray("current_battery");
                                JSONObject object2 = nBatteryArray.getJSONObject(0);
                                newBtSKUET.setText(object2.getString("battery_sku"));
                                newBtBrandET.setText(object2.getString("battery_brand"));
                                newBtSizeET.setText(object2.getString("battery_size"));



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

                    }
                });

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    @Override
    public void deleteJobsDetails(int position) {

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }

    private void getTechList(){
        String URL = URL_Helper.USER_LIST_URL+"?type=Technician";
        techNameList = new ArrayList<>();
        techNameList.add("Select Tech.");
        techIdList = new ArrayList<>();
        techIdList.add("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");

                            Log.d("12345", "no of tech record   "+array.length());
                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);
                                techIdList.add(object.getString("id"));
                                techNameList.add(object.getString("name"));
                            }

                            if (getActivity() != null) {
                                spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, techNameList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                techSpinner.setAdapter(spinnerAdapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12345", error.toString());
                        Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();

                    }
                });

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void getCustomerList(){
//        String URL = URL_Helper.USER_LIST_URL+"?type=Technician";
        custNameList = new ArrayList<>();
        custNameList.add("Select Customer");
        custIdList = new ArrayList<>();
        custIdList.add("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.CUSTOMER_list_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("12345", response.toString());

                        try {
                            JSONArray array = response.getJSONArray("data");

                            Log.d("12345", "no of customer record   "+array.length());

                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);
                                custIdList.add(object.getString("id"));
                                custNameList.add(object.getString("name"));
                            }

                            cusSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, custNameList);
                            cusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            customerSpinner.setAdapter(cusSpinnerAdapter);
                            customerNameSpinner.setAdapter(cusSpinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12345", error.toString());
//                        Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();

                    }
                });

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void getCustomerVehicleId(String id){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.CUSTOMER_DETAILS_URL + "?id=" + id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            Log.d("12345", "message "+message);

                            if (message.equals("Customer id is invalid.")){
                                Log.d("12345", message);
                            }else {
                                JSONArray array = response.getJSONArray("data");
                                JSONObject object = array.getJSONObject(0);

                                JSONArray array1 = object.getJSONArray("vehicles");
                                JSONObject object1 = array1.getJSONObject(0);

                                custVehicleId = object1.getString("id");
                                Log.d("12345", custVehicleId);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void searchByLocation(){
        searchByLocationET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final ArrayList<JobsListPojo> user = new ArrayList<>();
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                ManageUser.this.listAdapter.getFilter().filter(s);
                for (int i=0; i<jobsListArrayList.size(); i++){
                    JobsListPojo listPojo= jobsListArrayList.get(i);
                    if (listPojo.getLocation().toLowerCase().contains(s.toString().toLowerCase())){
                        JobsListPojo pojo = new JobsListPojo();
                        pojo.setId(listPojo.getId());
                        pojo.setReschedule_accepted(listPojo.getReschedule_accepted());
                        pojo.setIs_reschedule(listPojo.getIs_reschedule());
                        pojo.setAllocated_to(listPojo.getAllocated_to());
                        pojo.setReaschedule_reason(listPojo.getReaschedule_reason());
                        pojo.setVehicle_model(listPojo.getVehicle_model());
                        pojo.setVehicle_name(listPojo.getVehicle_name());
                        pojo.setCustomer_name(listPojo.getCustomer_name());
                        pojo.setReceipt_no(listPojo.getReceipt_no());
                        pojo.setCancellation_reason(listPojo.getCancellation_reason());
                        pojo.setJob_status(listPojo.getJob_status());
                        pojo.setQuick_note(listPojo.getQuick_note());
                        pojo.setPrice_offered(listPojo.getPrice_offered());
                        pojo.setPrice_offered_type(listPojo.getPrice_offered_type());
                        pojo.setIssues_faced(listPojo.getIssues_faced());
                        pojo.setSource_of_call(listPojo.getSource_of_call());
                        pojo.setLocation(listPojo.getLocation());
                        pojo.setVehicle_id(listPojo.getVehicle_id());
                        pojo.setCustomer_id(listPojo.getCustomer_id());
                        pojo.setCash_received(listPojo.getCash_received());
                        pojo.setIs_warranty(listPojo.getIs_warranty());
                        pojo.setOvertime(listPojo.getOvertime());
                        pojo.setPush_sale(listPojo.getPush_sale());
                        pojo.setCreated_at(listPojo.getCreated_at());
                        pojo.setUpdated_at(listPojo.getUpdated_at());
                        user.add(pojo);

                    }
                }
                if (user.size()<1){
                    noResultFoundTV.setVisibility(View.VISIBLE);
                    jobsList.setVisibility(View.GONE);
                }else {
                    noResultFoundTV.setVisibility(View.GONE);
                    jobsList.setVisibility(View.VISIBLE);
                    showList(user);}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchBySourceOfCall(){
        searchSourceOfCallET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final ArrayList<JobsListPojo> user = new ArrayList<>();
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                ManageUser.this.listAdapter.getFilter().filter(s);
                for (int i=0; i<jobsListArrayList.size(); i++){
                    JobsListPojo listPojo= jobsListArrayList.get(i);
                    if (listPojo.getSource_of_call().toLowerCase().contains(s.toString().toLowerCase())){
                        JobsListPojo pojo = new JobsListPojo();
                        pojo.setId(listPojo.getId());
                        pojo.setReschedule_accepted(listPojo.getReschedule_accepted());
                        pojo.setIs_reschedule(listPojo.getIs_reschedule());
                        pojo.setAllocated_to(listPojo.getAllocated_to());
                        pojo.setReaschedule_reason(listPojo.getReaschedule_reason());
                        pojo.setVehicle_model(listPojo.getVehicle_model());
                        pojo.setVehicle_name(listPojo.getVehicle_name());
                        pojo.setCustomer_name(listPojo.getCustomer_name());
                        pojo.setReceipt_no(listPojo.getReceipt_no());
                        pojo.setCancellation_reason(listPojo.getCancellation_reason());
                        pojo.setJob_status(listPojo.getJob_status());
                        pojo.setQuick_note(listPojo.getQuick_note());
                        pojo.setPrice_offered(listPojo.getPrice_offered());
                        pojo.setPrice_offered_type(listPojo.getPrice_offered_type());
                        pojo.setIssues_faced(listPojo.getIssues_faced());
                        pojo.setSource_of_call(listPojo.getSource_of_call());
                        pojo.setLocation(listPojo.getLocation());
                        pojo.setVehicle_id(listPojo.getVehicle_id());
                        pojo.setCustomer_id(listPojo.getCustomer_id());
                        pojo.setCash_received(listPojo.getCash_received());
                        pojo.setIs_warranty(listPojo.getIs_warranty());
                        pojo.setOvertime(listPojo.getOvertime());
                        pojo.setPush_sale(listPojo.getPush_sale());
                        pojo.setCreated_at(listPojo.getCreated_at());
                        pojo.setUpdated_at(listPojo.getUpdated_at());
                        user.add(pojo);

                    }
                }
                if (user.size()<1){
                    noResultFoundTV.setVisibility(View.VISIBLE);
                    jobsList.setVisibility(View.GONE);
                }else {
                    noResultFoundTV.setVisibility(View.GONE);
                    jobsList.setVisibility(View.VISIBLE);
                    showList(user);}
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchByCustomerName(){

        customerNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cusName = custNameList.get(position);

                if (cusName.equals("Select Customer")){
//                    Toast.makeText(getActivity(), "Select Technician", Toast.LENGTH_SHORT).show();
                }else {
                    final ArrayList<JobsListPojo> user = new ArrayList<>();

                    for (int i=0; i<jobsListArrayList.size(); i++){
                        JobsListPojo listPojo= jobsListArrayList.get(i);
                        if (listPojo.getCustomer_name().toLowerCase().contains(cusName.toString().toLowerCase())){
                            JobsListPojo pojo = new JobsListPojo();
                            pojo.setId(listPojo.getId());
                            pojo.setReschedule_accepted(listPojo.getReschedule_accepted());
                            pojo.setIs_reschedule(listPojo.getIs_reschedule());
                            pojo.setAllocated_to(listPojo.getAllocated_to());
                            pojo.setReaschedule_reason(listPojo.getReaschedule_reason());
                            pojo.setVehicle_model(listPojo.getVehicle_model());
                            pojo.setVehicle_name(listPojo.getVehicle_name());
                            pojo.setCustomer_name(listPojo.getCustomer_name());
                            pojo.setReceipt_no(listPojo.getReceipt_no());
                            pojo.setCancellation_reason(listPojo.getCancellation_reason());
                            pojo.setJob_status(listPojo.getJob_status());
                            pojo.setQuick_note(listPojo.getQuick_note());
                            pojo.setPrice_offered(listPojo.getPrice_offered());
                            pojo.setPrice_offered_type(listPojo.getPrice_offered_type());
                            pojo.setIssues_faced(listPojo.getIssues_faced());
                            pojo.setSource_of_call(listPojo.getSource_of_call());
                            pojo.setLocation(listPojo.getLocation());
                            pojo.setVehicle_id(listPojo.getVehicle_id());
                            pojo.setCustomer_id(listPojo.getCustomer_id());
                            pojo.setCash_received(listPojo.getCash_received());
                            pojo.setIs_warranty(listPojo.getIs_warranty());
                            pojo.setOvertime(listPojo.getOvertime());
                            pojo.setPush_sale(listPojo.getPush_sale());
                            pojo.setCreated_at(listPojo.getCreated_at());
                            pojo.setUpdated_at(listPojo.getUpdated_at());
                            user.add(pojo);

                        }
                    }
                    if (user.size()<1){
                        noResultFoundTV.setVisibility(View.VISIBLE);
                        jobsList.setVisibility(View.GONE);
                    }else {
                        noResultFoundTV.setVisibility(View.GONE);
                        jobsList.setVisibility(View.VISIBLE);
                        showList(user);}
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}
