package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.BatteryAllocatedListAddapter;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.BatteryAllocatedListPojo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.app.mobilebattery.R;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InventoryAllocation extends Fragment  {


    private ListView allocatedBatteryList;

    private String techID = "12";
    private ArrayList<BatteryAllocatedListPojo> arrayList;
    private BatteryAllocatedListAddapter allocatedListAdapter;
    private String allocatedBatteryId;
    private Spinner techNameSpinner;
    private ImageButton resetFilter;

    private String techId;
    private ArrayList<String> techNameList;
    private ArrayList<String> techIdList;
    private ArrayAdapter<String> dataAdapter;

    public InventoryAllocation() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.inventory_allocation_fragment, container, false);

        init(view);
        methodListener();
        loadAllocatedDefaultStockList();
        getTechList();


        return view;
    }

    private void init(View view) {
        techNameSpinner = view.findViewById(R.id.techNameSpinner);
        allocatedBatteryList = view.findViewById(R.id.allocatedBatteryList);
        resetFilter = view.findViewById(R.id.resetFilter);
    }

    private void methodListener() {

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                techNameSpinner.setSelection(0);
                showList(arrayList);
            }
        });

        techNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (techNameList.get(position).equals("Select Tech.")) {
//                    Toast.makeText(getActivity(), "Select Technician", Toast.LENGTH_SHORT).show();
                } else {
                    techID = techIdList.get(position);
                    Log.d("12345", techID);
                    loadAllocatedStockList(techID);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadAllocatedDefaultStockList() {

        arrayList = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String url_data = "?id=" + techID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.GET_ALLOCATED_STOCK_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", "array " + array.toString());
                            Log.d("12345", "array length " + array.length());

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                BatteryAllocatedListPojo pojo = new BatteryAllocatedListPojo();
                                pojo.setId(object.getString("id"));
                                pojo.setBattery_id(object.getString("stock_id"));
                                pojo.setTechnician_id(object.getString("technician_id"));
                                pojo.setAssigned_quantity(object.getString("assigned_quantity"));
                                pojo.setSold_quantity(object.getString("sold_quantity"));
                                pojo.setRemaining_quantity(object.getString("remaining_quantity"));
                                pojo.setCreated_at(object.getString("created_at"));
                                pojo.setUpdated_at(object.getString("updated_at"));
                                pojo.setTechnician_name(object.getString("technician_name"));
                                pojo.setBattery_sku(object.getString("battery_sku"));
                                pojo.setBattery_brand(object.getString("battery_brand"));
                                pojo.setBattery_size(object.getString("battery_size"));
                                pojo.setBattery_image(object.getString("battery_image"));
                                pojo.setCost_price(object.getString("cost_price"));

                                arrayList.add(pojo);
                            }

                            showList(arrayList);
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

    private void loadAllocatedStockList(String techID) {

        final ArrayList<BatteryAllocatedListPojo> list = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String url_data = "?id=" + techID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.GET_ALLOCATED_STOCK_URL+ url_data, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", "array " + array.toString());
                            Log.d("12345", "array length " + array.length());

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                BatteryAllocatedListPojo pojo = new BatteryAllocatedListPojo();
                                pojo.setId(object.getString("id"));
                                pojo.setBattery_id(object.getString("stock_id"));
                                pojo.setTechnician_id(object.getString("technician_id"));
                                pojo.setAssigned_quantity(object.getString("assigned_quantity"));
                                pojo.setSold_quantity(object.getString("sold_quantity"));
                                pojo.setRemaining_quantity(object.getString("remaining_quantity"));
                                pojo.setCreated_at(object.getString("created_at"));
                                pojo.setUpdated_at(object.getString("updated_at"));
                                pojo.setTechnician_name(object.getString("technician_name"));
                                pojo.setBattery_sku(object.getString("battery_sku"));
                                pojo.setBattery_brand(object.getString("battery_brand"));
                                pojo.setBattery_size(object.getString("battery_size"));
                                pojo.setBattery_image(object.getString("battery_image"));
                                pojo.setCost_price(object.getString("cost_price"));

                                list.add(pojo);
                            }

                            showList(list);
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

    private void showList(ArrayList<BatteryAllocatedListPojo> arrayList) {
        if (getActivity()!=null) {

            allocatedListAdapter = new BatteryAllocatedListAddapter(getActivity(), R.layout.allocated_stock_list_item, arrayList);
            allocatedBatteryList.setAdapter(allocatedListAdapter);
        }
    }



    private void getTechList() {
        String URL = URL_Helper.USER_LIST_URL + "?type=Technician";
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

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                techIdList.add(object.getString("id"));
                                techNameList.add(object.getString("username"));
                            }

                            dataAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, techNameList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            techNameSpinner.setAdapter(dataAdapter);

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

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

}
