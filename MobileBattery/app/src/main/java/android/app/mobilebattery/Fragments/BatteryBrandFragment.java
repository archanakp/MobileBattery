package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.BatteryBrandAdapter;
import android.app.mobilebattery.Helper.HideKeyBoard;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Interface.ApiInterface;
import android.app.mobilebattery.Model.BatteryBrandModel;
import android.app.mobilebattery.Pojo.BatteryBrandPojo;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BatteryBrandFragment extends Fragment implements BatteryBrandAdapter.EditListItem,
        BatteryBrandAdapter.DeleteListItem{


    private Button addBatteryBrandBtn, saveBatteryBrandBtn;
    private RelativeLayout relativeLayout, actionLayout;
    private EditText brandNameET;
    private RadioGroup actionRadioGroup;
    private RadioButton activeOrInactive, active, inactive;
    private RecyclerView brandRecyclerList;
    private Spinner searchByStsSpinner;
    private EditText searchByBrandET;
    private TextView noResultFoundTV;
    private ImageView refreshListIV, closeForm;
    private ProgressBar progressBar;

    private ArrayList<String> statusSpinnerList;
    private ArrayAdapter<String> statusSpinnerAdapter;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    private int clickedItemId;
    private String click;

    private ApiInterface apiInterface;

    private BatteryBrandAdapter adapter;
    private BatteryBrandModel model;
    private PagedList<BatteryBrandPojo.BatteryBrandItem> batteryBrandList ;


    public BatteryBrandFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  =  inflater.inflate(R.layout.battery_brand_fragment, container, false);

        init(view);
        methodListener();
        setAdapterToRecyclerView();

        new HideKeyBoard().setupUI(relativeLayout, getActivity());
        apiInterface = NetworkClient.getRetrofitClient(getActivity()).create(ApiInterface.class);
//        brandListAdapter = new BatteryBrandListAdapter(getActivity());

        setSpinnerAdapter(getActivity());

        searchByBatteryBrand();
        searchByStatus();

        return view;
    }

    private void init(View view) {

        closeForm = view.findViewById(R.id.closeForm);
        progressBar = view.findViewById(R.id.progressBar);
        refreshListIV = view.findViewById(R.id.refreshListIV);
        noResultFoundTV = view.findViewById(R.id.noResultFoundTV);
        searchByStsSpinner = view.findViewById(R.id.searchByStsSpinner);
        searchByBrandET = view.findViewById(R.id.searchByBrandET);
        brandRecyclerList = view.findViewById(R.id.brandRecyclerList);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        addBatteryBrandBtn = view.findViewById(R.id.addBatteryBrandBtn);
        saveBatteryBrandBtn = view.findViewById(R.id.saveBatteryBrandBtn);
        actionLayout = view.findViewById(R.id.actionLayout);
        brandNameET = view.findViewById(R.id.brandNameET);
        actionRadioGroup = view.findViewById(R.id.actionRadioGroup);
        active = view.findViewById(R.id.active);
        inactive = view.findViewById(R.id.inactive);
    }

    private void methodListener() {

        refreshListIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (model != null){
                    model.liveDataSource.getValue().invalidate();
                }
                searchByBrandET.setText("");
                searchByBrandET.clearFocus();
                searchByStsSpinner.setSelection(0);
                setAdapterToRecyclerView();
            }
        });

        addBatteryBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionLayout.setVisibility(View.VISIBLE);
                brandNameET.setText("");
                click = "add";
            }
        });

        closeForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionLayout.setVisibility(View.GONE);
                brandNameET.setText("");
            }
        });

        saveBatteryBrandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String brandName = brandNameET.getText().toString();


                if (brandName.equals("")){
                    if (getActivity() != null){
                        Snackbar.make(view, "Please Enter Brand Name", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                    brandNameET.setError("Enter Brand Name");
                }else {

                    Map<String, String> map = new HashMap<>();
                    map.put("brand_name",brandName);
//                    activeOrInactive = view.findViewById(actionRadioGroup.getCheckedRadioButtonId());
//                    if (activeOrInactive.getText().toString().equals("Active")){
                    if (active.isChecked()){
                        map.put("status","1");
                    }else if (inactive.isChecked()){
                        map.put("status","0");
                    }
                    Log.d("12345", "map   "+map.toString());

                    if (click.equals("add"))
                        addBatteryBrand(map);
                    else
                        editBatteryBrand(map);
                }
            }
        });
    }

    private void setSpinnerAdapter(FragmentActivity activity) {


        statusSpinnerList = new ArrayList<>();
        statusSpinnerList.add("Status");
        statusSpinnerList.add("Active");
        statusSpinnerList.add("Inactive");

        statusSpinnerAdapter = new ArrayAdapter<>(activity,
                R.layout.simple_spinner_item, statusSpinnerList);
        statusSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchByStsSpinner.setAdapter(statusSpinnerAdapter);
    }

    private void setAdapterToRecyclerView() {

        if (getActivity() != null) {

            progressBar.setVisibility(View.VISIBLE);

            brandRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
            brandRecyclerList.setItemAnimator(new DefaultItemAnimator());


            adapter = new BatteryBrandAdapter(getActivity());
            adapter.deleteListItem = this;
            adapter.editListItem = this;

            if (model != null){
                if (model.liveDataSource.getValue() != null)
                    model.liveDataSource.getValue().invalidate();
            }

            model = new BatteryBrandModel();

            model.itemListData.observe(getActivity(), new Observer<PagedList<BatteryBrandPojo.BatteryBrandItem>>() {
                @Override
                public void onChanged(PagedList<BatteryBrandPojo.BatteryBrandItem> batteryBrandItems) {
                    batteryBrandList = batteryBrandItems;
                    adapter.submitList(batteryBrandItems);
                    progressBar.setVisibility(View.GONE);
                }
            });

            brandRecyclerList.setAdapter(adapter);

//            if (batteryBrandList.size() == 0) {
//                noResultFoundTV.setVisibility(View.GONE);
//                brandRecyclerList.setVisibility(View.VISIBLE);
//            }else {
//                noResultFoundTV.setVisibility(View.VISIBLE);
//                brandRecyclerList.setVisibility(View.GONE);
//            }

        }
    }

    private void addBatteryBrand(final Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        if (getActivity()!= null) {
            StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_BATTERY_BRAND_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.cancel();


                            try {
                                JSONObject object = new JSONObject(response);
                                String message = object.getString("message");

                                Log.d("12345", "error  "+message);
                                actionLayout.setVisibility(View.GONE);
                                brandNameET.setText("");

                                if (message.equals("Brand name and status cannot be empty.")){
                                    Snackbar.make(getView(), "Brand name and status cannot be empty. ", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else if (message.equals("Technical Error.")){
                                    Snackbar.make(getView(), "Technical Error. ", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else if (message.equals("Brand Added Successfully.")){
                                    Snackbar.make(getView(), "Brand Added Successfully.", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();


                                    if (model != null) {
                                        model.liveDataSource.getValue().invalidate();
                                        setAdapterToRecyclerView();
                                    }
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
                            Snackbar.make(getView(), "BError in network, Please Try again! ", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            Log.d("12345", "error  "+error.toString());

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void editBatteryBrand(final Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        if (getActivity()!= null) {
            StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.EDIT_BATTERY_BRAND_URL+clickedItemId,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.cancel();

                            try {
                                JSONObject object = new JSONObject(response);
                                String message = object.getString("message");

                                Log.d("12345", "message  "+message);
                                actionLayout.setVisibility(View.GONE);
                                brandNameET.setText("");

                                if (message.equals("Brand name and status cannot be empty.")){
                                    Snackbar.make(getView(), "Brand name and status cannot be empty. ", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else if (message.equals("Technical Error.")){
                                    Snackbar.make(getView(), "Technical Error. ", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else if (message.equals("Brand details updated successfully.")){
                                    Snackbar.make(getView(), "Brand details updated successfully.", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();

                                    if (model != null) {
                                        model.liveDataSource.getValue().invalidate();
                                        setAdapterToRecyclerView();
                                    }
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
                            Snackbar.make(getView(), "BError in network, Please Try again! ", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                            Log.d("12345", "error  "+error.toString());

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }


    @Override
    public void editBrandDetails(int position) {

        BatteryBrandPojo.BatteryBrandItem item = batteryBrandList.get(position);
        if (item != null) {
            actionLayout.setVisibility(View.VISIBLE);
            click = "edit";
            clickedItemId = item.getId();
//        BatteryBrandPojo.BatteryBrandItem data = new BatteryBrandListAdapter(getActivity()).getBrandList().get(position);
            brandNameET.setText(item.getBrand_name());
            if (item.getStatus()) {
                actionRadioGroup.check(R.id.active);
            } else {
                actionRadioGroup.check(R.id.inactive);
            }
        }
    }

    @Override
    public void deleteBrandDetails(int position) {
        Log.d("123445", "position  "+position);
    }

    private void searchByBatteryBrand(){
        searchByBrandET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                progressBar.setVisibility(View.VISIBLE);

                if (model != null){
                    if (model.liveDataSource.getValue() != null)
                        model.liveDataSource.getValue().invalidate();
                }

                if (getActivity() != null) {
                    if (s.length() == 0)
                        model = new BatteryBrandModel();
                    else
                        model = new BatteryBrandModel("brand",s.toString().toLowerCase() ,0);

                    model.itemListData.observe(getActivity(), new Observer<PagedList<BatteryBrandPojo.BatteryBrandItem>>() {
                        @Override
                        public void onChanged(PagedList<BatteryBrandPojo.BatteryBrandItem> batteryBrandItems) {
                            batteryBrandList = batteryBrandItems;
                            adapter.submitList(batteryBrandItems);
                            progressBar.setVisibility(View.GONE);

                        }
                    });
                    brandRecyclerList.setAdapter(adapter);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void searchByStatus(){

        searchByStsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String name = statusSpinnerList.get(position);
//                progressBar.setVisibility(View.VISIBLE);

                if (model != null){
                    if (model.liveDataSource.getValue() != null)
                        model.liveDataSource.getValue().invalidate();
                }

                if (name.equals("Active")){
                    model = new BatteryBrandModel("status",null ,1);

                }else if (name.equals("Inactive")){
                    model = new BatteryBrandModel("status",null ,0);
                }else {
                    model = new BatteryBrandModel();
                }

                if (getActivity() != null) {

                    model.itemListData.observe(getActivity(), new Observer<PagedList<BatteryBrandPojo.BatteryBrandItem>>() {
                        @Override
                        public void onChanged(PagedList<BatteryBrandPojo.BatteryBrandItem> batteryBrandItems) {
                            batteryBrandList = batteryBrandItems;
                            adapter.submitList(batteryBrandItems);



                        }
                    });

                    brandRecyclerList.setAdapter(adapter);

//                    if (batteryBrandList.size() == 0){
//                        noResultFoundTV.setVisibility(View.VISIBLE);
//                        brandRecyclerList.setVisibility(View.GONE);
//                        progressBar.setVisibility(View.GONE);
//                    }else {
//                        noResultFoundTV.setVisibility(View.GONE);
//                        brandRecyclerList.setVisibility(View.VISIBLE);
//                        progressBar.setVisibility(View.GONE);
//                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



}
