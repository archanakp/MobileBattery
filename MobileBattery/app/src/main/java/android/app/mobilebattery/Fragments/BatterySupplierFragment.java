package android.app.mobilebattery.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.SupplierAdapter;
import android.app.mobilebattery.Helper.HideKeyBoard;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Model.SupplierViewModel;
import android.app.mobilebattery.Pojo.SupplierListPojo;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BatterySupplierFragment extends Fragment implements SupplierAdapter.Action{

    private RecyclerView supplierList;
    private EditText supplierNameEditText, supplierEmailEditText, phoneNoEditText, supplierCompanyEditText, supplierAddressEditText;
    private ImageView closeNewSupplierFormBtn;
    private Button submitSupplierDetails, addNewSupplierBtn;
    private ScrollView newSupplierForm;
    private RelativeLayout relativeLayout;
    private EditText searchByNameET, searchByEmailET, searchByPhoneET;
    private TextView noUserFoundTV;
    private ImageButton resetFilter;

    private ArrayList<SupplierListPojo> arrayList;
    private SupplierAdapter adapter;
    private SupplierViewModel viewModel;
    private PagedList<SupplierListPojo.SupplierItem> supplierItemList;


    private String btnClick = null;
    private String editId = "1";

    public BatterySupplierFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.battery_supplier_fragment, container, false);
        
        init(view);
        methodListener();
        showSupplierList();

        new HideKeyBoard().setupUI(relativeLayout,getActivity());
        searchByName();
        searchByEmail();
        searchByPhoneNo();

        return view;
    }

    private void init(View view) {

        resetFilter = view.findViewById(R.id.resetFilter);
        noUserFoundTV = view.findViewById(R.id.noUserFoundTV);
        searchByNameET = view.findViewById(R.id.searchByNameET);
        searchByEmailET = view.findViewById(R.id.searchByEmailET);
        searchByPhoneET = view.findViewById(R.id.searchByPhoneET);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        supplierList = view.findViewById(R.id.supplierList);
        supplierAddressEditText = view.findViewById(R.id.supplierAddressEditText);
        supplierCompanyEditText = view.findViewById(R.id.supplierCompanyEditText);
        phoneNoEditText = view.findViewById(R.id.phoneNoEditText);
        supplierEmailEditText = view.findViewById(R.id.supplierEmailEditText);
        supplierNameEditText = view.findViewById(R.id.supplierNameEditText);
        closeNewSupplierFormBtn = view.findViewById(R.id.closeNewSupplierFormBtn);
        newSupplierForm = view.findViewById(R.id.newSupplierForm);
        submitSupplierDetails = view.findViewById(R.id.submitSupplierDetails);
        addNewSupplierBtn = view.findViewById(R.id.addNewSupplierBtn);
    }

    private void methodListener() {

        addNewSupplierBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                supplierList.setVisibility(View.GONE);
                newSupplierForm.setVisibility(View.VISIBLE);
                clearAllEditText();
                btnClick = "add";
            }
        });
        closeNewSupplierFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                supplierList.setVisibility(View.VISIBLE);
                newSupplierForm.setVisibility(View.GONE);
            }
        });
        submitSupplierDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEnteredSupplierDetails();
            }
        });

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByNameET.setText("");
                searchByNameET.clearFocus();
                searchByEmailET.setText("");
                searchByEmailET.clearFocus();
                searchByPhoneET.setText("");
                searchByPhoneET.clearFocus();
                noUserFoundTV.setVisibility(View.GONE);
                supplierList.setVisibility(View.VISIBLE);
            }
        });



    }

    private void getEnteredSupplierDetails() {

        String name = supplierNameEditText.getText().toString();
        String email = supplierEmailEditText.getText().toString();
        String phoneNo = phoneNoEditText.getText().toString();
        String company = supplierCompanyEditText.getText().toString();
        String address = supplierAddressEditText.getText().toString();

        if (name.equals("") || email.equals("") || phoneNo.equals("") || company.equals("") || address.equals("")){
            Toast.makeText(getContext(), "All fields required !", Toast.LENGTH_SHORT).show();
        }else if (!email.matches(URL_Helper.emailPattern)){
            supplierEmailEditText.setError("Invalid Email");
        }else if (phoneNo.length()<9){
            phoneNoEditText.setError("Invalid phone No");
        }else {
            Map<String, String> map = new HashMap<>();
            map.put("supplier_email", email);
            map.put("supplier_name", name);
            map.put("supplier_phone", phoneNo);
            map.put("supplier_company", company);
            map.put("supplier_address", address);

            if (btnClick.equals("add"))
            {addSupplierData(map);}
            else if (btnClick.equals("edit"))
            {editSupplierData(map);}

        }

    }

    private void addSupplierData(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String data = "?id="+editId;
        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_SUPPLIER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            Log.d("12345", "response " + response);
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Supplier Added Successfully.")) {
                                Toast.makeText(getActivity(), "Supplier Added Successfully.", Toast.LENGTH_SHORT).show();
                                showSupplierList();
                                newSupplierForm.setVisibility(View.GONE);
                                supplierList.setVisibility(View.VISIBLE);
                            }else if (message.equals("Supplier email already exists.")) {
                                Toast.makeText(getActivity(), "Supplier email already exists.", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("Supplier phone already exists.")) {
                                Toast.makeText(getActivity(), "Supplier phone already exists.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Supplier name, email, phone, company or address cannot be empty.", Toast.LENGTH_SHORT).show();
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
                        Log.d("12345", "add error " + error.toString());
                        Toast.makeText(getActivity(), "error in network ", Toast.LENGTH_SHORT).show();

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

    private void editSupplierData(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.EDIT_SUPPLIER_URL+editId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            Log.d("12345", "response " + response);
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Supplier details updated successfully.")) {
                                Toast.makeText(getActivity(), "Supplier details updated successfully.", Toast.LENGTH_SHORT).show();
                                newSupplierForm.setVisibility(View.GONE);
                                supplierList.setVisibility(View.VISIBLE);
                                showSupplierList();

                            }else if (message.equals("Battery email or phone already exists.")) {
                                Toast.makeText(getActivity(), "Battery email or phone already exists.", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("Supplier type is invalid.")) {
                                Toast.makeText(getActivity(), "Supplier type is invalid.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Supplier name, email, phone, company or address cannot be empty.", Toast.LENGTH_SHORT).show();
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
                        Log.d("12345", "login error " + error.toString());
                        Toast.makeText(getActivity(), "error in network ", Toast.LENGTH_SHORT).show();

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

    private void showSupplierList() {

        if (getActivity()!=null) {

            supplierList.setLayoutManager(new LinearLayoutManager(getActivity()));
            supplierList.setItemAnimator(new DefaultItemAnimator());


            adapter = new SupplierAdapter(getActivity());
            adapter.action = this;

            if (viewModel != null)
                if (viewModel.liveDataSource.getValue() != null)
                    viewModel.liveDataSource.getValue().invalidate();


            viewModel = new SupplierViewModel();

            viewModel.liveDataList.observe(getActivity(), new Observer<PagedList<SupplierListPojo.SupplierItem>>() {
                @Override
                public void onChanged(PagedList<SupplierListPojo.SupplierItem> supplierItems) {
                    supplierItemList = supplierItems;
                    adapter.submitList(supplierItems);
                }
            });
            supplierList.setAdapter(adapter);
//            supplierList.setAdapter(adapter);
        }
    }


    private void clearAllEditText(){
        supplierNameEditText.setText("");
        supplierEmailEditText.setText("");
        phoneNoEditText.setText("");
        supplierAddressEditText.setText("");
        supplierCompanyEditText.setText("");

        submitSupplierDetails.setText("ADD SUPPLIER");
    }


    private void searchByName(){
        searchByNameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getActivity(), s.toString().toLowerCase(), Toast.LENGTH_SHORT).show();

                if (getActivity() != null){

                    if (viewModel != null)
                        if (viewModel.liveDataSource.getValue() != null)
                            viewModel.liveDataSource.getValue().invalidate();

                    if (s.toString().length() != 0)
                        viewModel = new SupplierViewModel("name", s.toString().toLowerCase());
                    else
                        viewModel = new SupplierViewModel();

                    viewModel.liveDataList.observe(getActivity(), new Observer<PagedList<SupplierListPojo.SupplierItem>>() {
                        @Override
                        public void onChanged(PagedList<SupplierListPojo.SupplierItem> supplierItems) {
                            supplierItemList = supplierItems;
                            adapter.submitList(supplierItems);
                        }
                    });
                    supplierList.setAdapter(adapter);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchByEmail(){
        searchByEmailET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getActivity(), s.toString().toLowerCase(), Toast.LENGTH_SHORT).show();

                if (getActivity() != null){

                    if (viewModel != null)
                        if (viewModel.liveDataSource.getValue() != null)
                            viewModel.liveDataSource.getValue().invalidate();

                    if (s.length() != 0)
                        viewModel = new SupplierViewModel("email", s.toString().toLowerCase());
                    else
                        viewModel = new SupplierViewModel();

                    viewModel.liveDataList.observe(getActivity(), new Observer<PagedList<SupplierListPojo.SupplierItem>>() {
                        @Override
                        public void onChanged(PagedList<SupplierListPojo.SupplierItem> supplierItems) {
                            supplierItemList = supplierItems;
                            adapter.submitList(supplierItems);
                        }
                    });
                    supplierList.setAdapter(adapter);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchByPhoneNo(){
        searchByPhoneET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Toast.makeText(getActivity(), s.toString().toLowerCase(), Toast.LENGTH_SHORT).show();

                if (getActivity() != null){

                    if (viewModel != null)
                        if (viewModel.liveDataSource.getValue() != null)
                            viewModel.liveDataSource.getValue().invalidate();

                    if (s.length() != 0)
                        viewModel = new SupplierViewModel("phone", s.toString().toLowerCase());
                    else
                        viewModel = new SupplierViewModel();

                    viewModel.liveDataList.observe(getActivity(), new Observer<PagedList<SupplierListPojo.SupplierItem>>() {
                        @Override
                        public void onChanged(PagedList<SupplierListPojo.SupplierItem> supplierItems) {
                            supplierItemList = supplierItems;
                            adapter.submitList(supplierItems);
                        }
                    });
                    supplierList.setAdapter(adapter);

                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void editSupplierDetails(int position) {

        SupplierListPojo.SupplierItem pojo = supplierItemList.get(position);
        if (pojo != null) {
            supplierNameEditText.setText(pojo.getSupplier_name());
            supplierEmailEditText.setText(pojo.getSupplier_email());
            phoneNoEditText.setText(pojo.getSupplier_phone());
            supplierAddressEditText.setText(pojo.getSupplier_address());
            supplierCompanyEditText.setText(pojo.getSupplier_company());
            submitSupplierDetails.setText("EDIT SUPPLIER");

            newSupplierForm.setVisibility(View.VISIBLE);
//            supplierList.setVisibility(View.GONE);
            btnClick = "edit";
            editId = String.valueOf(pojo.getId());
        }

    }

    @Override
    public void deleteSupplierDetails(int position) {

    }
}
