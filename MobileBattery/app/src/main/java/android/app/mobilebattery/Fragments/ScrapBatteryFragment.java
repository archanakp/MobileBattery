package android.app.mobilebattery.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.NewBatteryRecyclerAdapter;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Model.NewBatteryModel;
import android.app.mobilebattery.Pojo.ImgeUploadPojo;
import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.Pojo.ProductPojo;
import android.app.mobilebattery.Pojo.SupplierListPojo;
import android.app.mobilebattery.Pojo.UserListPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

import static android.app.Activity.RESULT_OK;

public class ScrapBatteryFragment extends Fragment implements NewBatteryRecyclerAdapter.Action {


    private ListView newBatteryList;
    private EditText assignedQuantity;
    private EditText addQuantityEditText, addCostPriceEditText, addSalePriceEditText;
    private EditText searchByBrand, searchBySKUET, searchBySizeET;
    private TextView  noResultFoundTV, sourceTypeLabel;
    private ImageView closeFormBtn;
    private Button addNewBatteryBtn, addNewBatteryRecordBtn, submitStockToTechBtn;
    private ScrollView addBatteryForm;
    private Spinner sourceSpinner, techSpinner;
    private RelativeLayout relativeLayout;
    private Spinner addTechOrSupIdSpinner, addBatterySourceSpinner, addSalePriceTypeSpinner;
    private RelativeLayout form;
    private ImageButton refreshFilter;

    private EditText batteryName, addLowerRetailSalePriceEditText, addHigherRetailSalePriceEditText;

    private RecyclerView batteryRecyclerList;
    private Boolean listViewClicked = false;
    private int productId;

    private TextView btryAvailableQty, btrySize, btrySku, btryBrand;
    private TextView productName;
    private ListView productNameListView;

    private ArrayAdapter<String> batterySourceAdapter;
    private ArrayAdapter<String> salePriceSpinnerAdapter;
    private ArrayAdapter<String> searchSpinnerAdapter;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayList<String> batterySource;
    private ArrayList<String> supplierNameList;
    private ArrayList<SupplierListPojo.SupplierItem> supplierList;
    private ArrayList<UserListPojo> technicianList;
    private ArrayList<String> technicianNameList;
    private ArrayList<String> salePriceType;


    private ArrayList<ProductPojo.ProductItem> productItemList;

    private String btnClick = "add";
    private String batteryId = "";
    private int CODE_GALLERY_REQUEST = 999;


    private Boolean selectImgClicked = false;
    private File file;
    private String techId = "", allocateBatteryId, editImageUploadURL;
    private ArrayList<String> techNameList;
    private ArrayList<String> techIdList;
    private ArrayAdapter<String> dataAdapter;

    private NewBatteryRecyclerAdapter recyclerAdapter;
    private NewBatteryModel batteryModel;
    private PagedList<NewBatteryPojo.NewBatteryItem> newBatteryItemsList;

    private Bitmap bitmap;
    private static final int PICK_IMAGE = 1;

    public ScrapBatteryFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.scrap_battery_fragment, container, false);

        init(view);
        getTechList();
        methodListener();

        setAdapterToRecyclerView();

        fetchSupplierList();
        addSpinnerData();
        setupUI(relativeLayout);
        searchByBrand();
        searchBySKU();
//        searchBySize();
        searchBySource();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        batteryName.clearFocus();
    }

    private void setAdapterToRecyclerView() {

        if (getActivity() != null) {

            batteryRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
            batteryRecyclerList.setItemAnimator(new DefaultItemAnimator());

            recyclerAdapter = new NewBatteryRecyclerAdapter(getActivity());
            recyclerAdapter.action = this;

            if (batteryModel != null) {
                if (batteryModel.liveDataSource.getValue() != null) {
                    batteryModel.liveDataSource.getValue().invalidate();
                }
            }

            batteryModel = new NewBatteryModel("Scrap");
            batteryModel.itemListData.observe(getActivity(), new Observer<PagedList<NewBatteryPojo.NewBatteryItem>>() {
                @Override
                public void onChanged(PagedList<NewBatteryPojo.NewBatteryItem> newBatteryItems) {
                    newBatteryItemsList = newBatteryItems;
                    recyclerAdapter.submitList(newBatteryItemsList);

                }
            });

            batteryRecyclerList.setAdapter(recyclerAdapter);

        }


    }

    private void init(View view) {


        productNameListView = view.findViewById(R.id.productNameListView);
        batteryName = view.findViewById(R.id.batteryName);
        addHigherRetailSalePriceEditText = view.findViewById(R.id.addHigherRetailSalePriceEditText);
        addLowerRetailSalePriceEditText = view.findViewById(R.id.addLowerRetailSalePriceEditText);

        batteryRecyclerList = view.findViewById(R.id.batteryRecyclerList);
        refreshFilter = view.findViewById(R.id.refreshFilter);
        btryBrand = view.findViewById(R.id.btryBrand);
        btrySku = view.findViewById(R.id.btrySku);
        btrySize = view.findViewById(R.id.btrySize);
        btryAvailableQty = view.findViewById(R.id.btryAvailableQty);

        submitStockToTechBtn = view.findViewById(R.id.submitStockToTechBtn);
        form = view.findViewById(R.id.form);
        techSpinner = view.findViewById(R.id.techSpinner);
        assignedQuantity = view.findViewById(R.id.assignedQuantity);
        sourceTypeLabel = view.findViewById(R.id.sourceTypeLabel);
        noResultFoundTV = view.findViewById(R.id.noResultFoundTV);
        searchBySizeET = view.findViewById(R.id.searchBySizeET);
        searchBySKUET = view.findViewById(R.id.searchBySKUET);
        searchByBrand = view.findViewById(R.id.searchByBrand);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        sourceSpinner = view.findViewById(R.id.sourceSpinner);
        addBatteryForm = view.findViewById(R.id.addBatteryForm);
        addSalePriceTypeSpinner = view.findViewById(R.id.addSalePriceTypeSpinner);
        addBatterySourceSpinner = view.findViewById(R.id.addBatterySourceSpinner);
        addNewBatteryRecordBtn = view.findViewById(R.id.addNewBatteryRecordBtn);
        addNewBatteryBtn = view.findViewById(R.id.addNewBatteryBtn);
        closeFormBtn = view.findViewById(R.id.closeFormBtn);
        addTechOrSupIdSpinner = view.findViewById(R.id.addTechOrSupIdSpinner);
        addSalePriceEditText = view.findViewById(R.id.addSalePriceEditText);
        addCostPriceEditText = view.findViewById(R.id.addCostPriceEditText);
        addQuantityEditText = view.findViewById(R.id.addQuantityEditText);
        newBatteryList = view.findViewById(R.id.newBatteryList);

        batteryName.setFocusableInTouchMode(true);
    }

    private void methodListener() {

        refreshFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByBrand.setText("");
                searchByBrand.setFocusable(false);
                searchBySKUET.setText("");
                searchBySKUET.setFocusable(false);
                sourceSpinner.setSelection(0);
//                showList(arrayList);

            }
        });

        form.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form.setVisibility(View.GONE);
            }
        });

        submitStockToTechBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEnteredData();
            }
        });

        techSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (techNameList.get(position).equals("Select Tech.")) {
//                    Snackbar.make(getView(), "Select Tech. Name", Snackbar.LENGTH_SHORT)
//                            .setAction("Action", null).show();
                } else {
                    techId = techIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addNewBatteryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBatteryForm.setVisibility(View.VISIBLE);
                newBatteryList.setVisibility(View.GONE);
                btnClick = "add";
                listViewClicked = false;

                addBatterySourceSpinner.setSelection(0);
                addTechOrSupIdSpinner.setSelection(0);
                addSalePriceEditText.setText("");
                addCostPriceEditText.setText("");
                addQuantityEditText.setText("");
                addLowerRetailSalePriceEditText.setText("");
                addHigherRetailSalePriceEditText.setText("");
                batteryName.setText("");
                addSalePriceTypeSpinner.setSelection(0);

            }
        });
        closeFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBatteryForm.setVisibility(View.GONE);
                newBatteryList.setVisibility(View.VISIBLE);
                productNameListView.setVisibility(View.GONE);
            }
        });

        addNewBatteryRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBatteryRecord();
            }
        });
//
//        selectImage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                selectImgClicked = true;
//                if (ActivityCompat.checkSelfPermission(getActivity(),
//                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(
//                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//                            CODE_GALLERY_REQUEST);
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");
//                    startActivityForResult(Intent.createChooser(intent, "Select Image!"), CODE_GALLERY_REQUEST);
//                }
//
//            }
//        });

        batteryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewClicked = false;

                Log.d("12345", "show  "+listViewClicked);
            }
        });

        batteryName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (count == 0){

                }else {
                    if (listViewClicked){
                        Log.d("12345", "show  "+listViewClicked);
                    }else {
                        searchProductName(s);
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                productNameListView.setVisibility(View.GONE);
            }
        });


    }

    private void searchProductName(CharSequence s) {

        final String text = s.toString().toLowerCase();
        productItemList = new ArrayList<>();
        Log.d("12345", "search  " + s );

        String url = URL_Helper.PRODUCT_SEARCH_URL + "?filter=" + text;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean error = response.getBoolean("error");
                            if (error) {
                                Toast.makeText(getContext(), "no data found with this name", Toast.LENGTH_SHORT).show();
                            } else {

                                Log.d("12345", "response ");

                                productNameListView.setVisibility(View.VISIBLE);
                                ProductPojo p = new ProductPojo();
                                JSONArray array = response.getJSONArray("data");
                                if (array.length() != 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        Log.d("12345", "array " + array.length());

                                        ProductPojo.ProductItem item = p.new ProductItem();

                                        item.setId(object.getInt("id"));
                                        item.setBrand_id(object.getInt("brand_id"));
                                        item.setBattery_sku(object.getString("battery_sku"));
                                        item.setBrand_name(object.getString("brand_name"));
                                        item.setBattery_image(object.getString("battery_image"));
                                        item.setBattery_size(object.getString("battery_size"));
                                        item.setCreated_at(object.getString("created_at"));
                                        item.setUpdated_at(object.getString("updated_at"));

                                        productItemList.add(item);
                                    }

                                    productNameListView.setAdapter(new ProductListAdapter(getContext(), R.layout.simple_spinner_item, productItemList));


                                } else {
                                    Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
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
                        Log.d("12345", "error " + error.getMessage());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //TODO: action
            Uri filePath = data.getData();

            String path = getRealPathFromUri(getActivity(), filePath);
            String imageFileName = path.substring(path.lastIndexOf("/") + 1);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);


                file = new File(getRealPathFromUri(getActivity(), filePath));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            Snackbar.make(getView(), "Select Another Image", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image!"), CODE_GALLERY_REQUEST);

            } else {
                Toast.makeText(getActivity(), "You Don't have permission! ", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getEnteredData() {


        final String qty = assignedQuantity.getText().toString();

        if (techId.equals("") || qty.equals("")) {
            Snackbar.make(getView(), "All Fields required !", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        } else {

            final ProgressDialog dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading...");
            dialog.show();

            StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ALLOCATE_STOCK_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.cancel();
                            try {
                                JSONObject object = new JSONObject(response);
                                String message = object.getString("message");
                                if (message.equals("Stock allocated successfully.")) {
                                    Snackbar.make(getView(), "Stock allocated Successfully", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                    form.setVisibility(View.GONE);
                                    newBatteryList.setVisibility(View.VISIBLE);
                                } else if (message.equals("Technical Error.")) {
                                    Snackbar.make(getView(), "Technical Error.", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else if (message.equals("Stock is not enough.")) {
                                    Snackbar.make(getView(), "Stock is not enough.", Snackbar.LENGTH_SHORT)
                                            .setAction("Action", null).show();
                                } else {
                                    Snackbar.make(getView(), "Stock id, technician id or quantity can not be empty.", Snackbar.LENGTH_SHORT)
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

                            Snackbar.make(getView(), "error in network", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();

                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String, String> map = new HashMap<>();
                    map.put("stock_id", allocateBatteryId);
                    map.put("technician_id", techId);
//                    map.put("technician_id", "12");
                    map.put("assigned_quantity", qty);
                    Log.d("12345", "map  "+map);
                    return map;
                }
            };

            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }
    }

    private void addBatteryRecord() {

        String l_r_price = addLowerRetailSalePriceEditText.getText().toString();
        String h_r_price = addHigherRetailSalePriceEditText.getText().toString();

        String productName = batteryName.getText().toString();

        String bSource = addBatterySourceSpinner.getSelectedItem().toString();
        String r_s_Price = addSalePriceEditText.getText().toString();
        String bSPriceType = addSalePriceTypeSpinner.getSelectedItem().toString();
        String bCostPrice = addCostPriceEditText.getText().toString();
        String bQuantity = addQuantityEditText.getText().toString();
        String bTechOrSupId = addTechOrSupIdSpinner.getSelectedItem().toString();

        if (bQuantity.equals("") || bCostPrice.equals("") || r_s_Price.equals("") || l_r_price.equals("") || h_r_price.equals("")) {
            Toast.makeText(getActivity(), "All Fields Required !", Toast.LENGTH_SHORT).show();
        } else if (productName.equals("Battery")) {
            Toast.makeText(getActivity(), "Please Select Battery !", Toast.LENGTH_SHORT).show();
        } else if (bSource.equals("Battery Source")) {
            Toast.makeText(getActivity(), "Please Select Battery Source !", Toast.LENGTH_SHORT).show();
        } else if (bSource.equals("Technician") && bTechOrSupId.equals("Technician")) {
            Toast.makeText(getActivity(), "Please Select Technician !", Toast.LENGTH_SHORT).show();
        } else if (bSource.equals("Supplier") && bTechOrSupId.equals("Supplier")) {
            Toast.makeText(getActivity(), "Please Select Supplier !", Toast.LENGTH_SHORT).show();
        } else if (bSPriceType.equals("Sale Price Type")) {
            Toast.makeText(getActivity(), "Please Select Sale Price Type !", Toast.LENGTH_SHORT).show();
        } else {


            Map<String, String> map = new HashMap<>();
            map.put("product_id", String.valueOf(productId));
            map.put("sale_price_type", bSPriceType);
            map.put("cost_price", bCostPrice);
            map.put("quantity", bQuantity);
            map.put("battery_type", "Scrap");
            map.put("battery_source", bSource);
            map.put("lower_retail_sale_price", l_r_price);
            map.put("retail_sale_price", r_s_Price);
            map.put("higher_retail_sale_price", h_r_price);

            if (bSource.equals("Technician")) {
                int p = spinnerAdapter.getPosition(bTechOrSupId);
                UserListPojo pojo = technicianList.get(p + 1);
                String id = pojo.getId();
                map.put("technician_id", id);
            } else if (bSource.equals("Supplier")) {
                int p = spinnerAdapter.getPosition(bTechOrSupId);
                SupplierListPojo.SupplierItem pojo = supplierList.get(p + 1);
                String id = String.valueOf(pojo.getId());
                map.put("supplier_id", id);
            }

            if (btnClick.equals("add")) {
//                uploadImageToServer(file, map);
                submitNewRecord(map);

            } else if (btnClick.equals("edit")) {
                editNewBatteryRecord(map);
//                if (selectImgClicked)
//                    editUploadImageToServer(file, map);
//                else
            }

        }
    }

    private void submitNewRecord(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_BATTERY_RECORD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Stock Added Successfully.")) {
                                Toast.makeText(getActivity(), "Stock Added Successfully", Toast.LENGTH_SHORT).show();
                                addBatteryForm.setVisibility(View.GONE);
                                newBatteryList.setVisibility(View.VISIBLE);
//                                fetchBatteryList();
                                setAdapterToRecyclerView();

                            } else if (message.equals("Battery type is invalid.")) {
                                Toast.makeText(getActivity(), "Battery type is invalid.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Sale price type can not be empty.")) {
                                Toast.makeText(getActivity(), "Sale price type can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Battery type can not be empty.")) {
                                Toast.makeText(getActivity(), "Battery type can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Technician id can not be empty.")) {
                                Toast.makeText(getActivity(), "Technician id can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Supplier id can not be empty.")) {
                                Toast.makeText(getActivity(), "Supplier id can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Battery Sku already exists.")) {
                                Toast.makeText(getActivity(), "Battery Sku already exists.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Technical Error.")) {
                                Toast.makeText(getActivity(), "Battery Sku already exists.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something wrong.", Toast.LENGTH_SHORT).show();
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

    private void editNewBatteryRecord(final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.EDIT_BATTERY_RECORD_URL + batteryId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Stock Updated Successfully.")) {
                                Toast.makeText(getActivity(), "Stock Updated Successfully.", Toast.LENGTH_SHORT).show();
                                addBatteryForm.setVisibility(View.GONE);
                                newBatteryList.setVisibility(View.VISIBLE);
//                                fetchBatteryList();
                                setAdapterToRecyclerView();

                            } else if (message.equals("Battery type is invalid.")) {
                                Toast.makeText(getActivity(), "Battery type is invalid.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Sale price type can not be empty.")) {
                                Toast.makeText(getActivity(), "Sale price type can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Battery type can not be empty.")) {
                                Toast.makeText(getActivity(), "Battery type can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Technician id can not be empty.")) {
                                Toast.makeText(getActivity(), "Technician id can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Supplier id can not be empty.")) {
                                Toast.makeText(getActivity(), "Supplier id can not be empty.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("Battery id is invalid.")) {
                                Toast.makeText(getActivity(), "Something wrong.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Something wrong.", Toast.LENGTH_SHORT).show();
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

    private void addSpinnerData() {

        batterySource = new ArrayList<>();
        batterySource.add("Battery Source");
        batterySource.add("Supplier");
        batterySource.add("Technician");
        batterySourceAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, batterySource);
        batterySourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addBatterySourceSpinner.setAdapter(batterySourceAdapter);

        searchSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, batterySource);
        searchSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sourceSpinner.setAdapter(searchSpinnerAdapter);


        salePriceType = new ArrayList<>();
        salePriceType.add("Sale Price Type");
        salePriceType.add("Lower Retail");
        salePriceType.add("Retail");
        salePriceType.add("Higher Retail");
        salePriceSpinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, salePriceType);
        salePriceSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addSalePriceTypeSpinner.setAdapter(salePriceSpinnerAdapter);

        addBatterySourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (batterySourceAdapter.getItem(i).equals("Supplier")) {
                    spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, supplierNameList);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    addTechOrSupIdSpinner.setAdapter(spinnerAdapter);
                    sourceTypeLabel.setText("Supplier");


                } else if (batterySourceAdapter.getItem(i).equals("Technician")) {
                    spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, technicianNameList);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    addTechOrSupIdSpinner.setAdapter(spinnerAdapter);
                    sourceTypeLabel.setText("Technician");

                } else if (batterySourceAdapter.getItem(i).equals("Battery Source")) {
                    spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, technicianNameList);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    addTechOrSupIdSpinner.setAdapter(spinnerAdapter);
                    sourceTypeLabel.setText("Technician");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
//                spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, technicianNameList);
//                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                addTechOrSupIdSpinner.setAdapter(spinnerAdapter);
            }
        });

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
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }

    private void searchByBrand() {
        searchByBrand.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (batteryModel !=null )
                    if (batteryModel.liveDataSource.getValue() != null)
                        batteryModel.liveDataSource.getValue().invalidate();

                if (getActivity() != null){
                    if (count == 0)
                        batteryModel = new NewBatteryModel("Scrap");
                    else
                        batteryModel = new NewBatteryModel("Scrap", "brand", s.toString().toLowerCase());

                    batteryModel.itemListData.observe(getActivity(), new Observer<PagedList<NewBatteryPojo.NewBatteryItem>>() {
                        @Override
                        public void onChanged(PagedList<NewBatteryPojo.NewBatteryItem> newBatteryItems) {
                            recyclerAdapter.submitList(newBatteryItems);
                            newBatteryItemsList = newBatteryItems;
                        }
                    });

                    batteryRecyclerList.setAdapter(recyclerAdapter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchBySKU() {
        searchBySKUET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (batteryModel !=null )
                    if (batteryModel.liveDataSource.getValue() != null)
                        batteryModel.liveDataSource.getValue().invalidate();

                if (getActivity() != null){
                    if (count == 0)
                        batteryModel = new NewBatteryModel("Scrap");
                    else
                        batteryModel = new NewBatteryModel("Scrap", "sku", s.toString().toLowerCase());

                    batteryModel.itemListData.observe(getActivity(), new Observer<PagedList<NewBatteryPojo.NewBatteryItem>>() {
                        @Override
                        public void onChanged(PagedList<NewBatteryPojo.NewBatteryItem> newBatteryItems) {
                            recyclerAdapter.submitList(newBatteryItems);
                            newBatteryItemsList = newBatteryItems;
                        }
                    });

                    batteryRecyclerList.setAdapter(recyclerAdapter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchBySource() {
        sourceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = batterySource.get(position);

//                if (name.equals("Battery Source")) {
////                    Toast.makeText(getActivity(), "Please select battery source!", Toast.LENGTH_SHORT).show();
//                } else if (name.equals("supplier")){
//
//                }

                if (getActivity() != null){
                    if (name.equals("Supplier")) {
                        if (batteryModel != null)
                            if (batteryModel.liveDataSource.getValue() != null)
                                batteryModel.liveDataSource.getValue().invalidate();

                        batteryModel = new NewBatteryModel("Scrap", "source", "Supplier");
                    }else if (name.equals("Technician")) {
                        if (batteryModel !=null )
                            if (batteryModel.liveDataSource.getValue() != null)
                                batteryModel.liveDataSource.getValue().invalidate();
                        batteryModel = new NewBatteryModel("Scrap", "source", "Technician");
                    }else {
                        if (batteryModel !=null )
                            if (batteryModel.liveDataSource.getValue() != null)
                                batteryModel.liveDataSource.getValue().invalidate();

                        batteryModel = new NewBatteryModel("Scrap");
                    }

                    batteryModel.itemListData.observe(getActivity(), new Observer<PagedList<NewBatteryPojo.NewBatteryItem>>() {
                        @Override
                        public void onChanged(PagedList<NewBatteryPojo.NewBatteryItem> newBatteryItems) {
                            recyclerAdapter.submitList(newBatteryItems);
                            newBatteryItemsList = newBatteryItems;
                        }
                    });

                    batteryRecyclerList.setAdapter(recyclerAdapter);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void fetchSupplierList() {

        supplierList = new ArrayList<>();
        technicianList = new ArrayList<>();
        supplierNameList = new ArrayList<>();
        supplierNameList.add("Supplier");
        technicianNameList = new ArrayList<>();
        technicianNameList.add("Technician");

        String URL = URL_Helper.USER_LIST_URL + "?type=Technician";

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.SUPPLIER_LIST_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                SupplierListPojo obj = new SupplierListPojo();
                                SupplierListPojo.SupplierItem pojo = obj.new SupplierItem();
                                pojo.setId(object.getInt("id"));
                                pojo.setSupplier_name(object.getString("supplier_name"));
                                pojo.setSupplier_email(object.getString("supplier_email"));
                                pojo.setSupplier_phone(object.getString("supplier_phone"));
                                pojo.setSupplier_company(object.getString("supplier_company"));
                                pojo.setSupplier_address(object.getString("supplier_address"));
                                pojo.setCreated_at(object.getString("created_at"));
                                pojo.setUpdated_at(object.getString("updated_at"));
                                supplierNameList.add(object.getString("supplier_name"));
                                supplierList.add(pojo);
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
                        Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);

                                UserListPojo pojo = new UserListPojo();
                                pojo.setId(object.getString("id"));
                                pojo.setUsername(object.getString("username"));
                                pojo.setEmail(object.getString("email"));
                                pojo.setPhone(object.getString("phone"));
                                pojo.setFirstname(object.getString("firstname"));
                                pojo.setLastname(object.getString("lastname"));
                                pojo.setProfile_image(object.getString("profile_image"));
                                pojo.setUsertype(object.getString("usertype"));
                                pojo.setCreated_at(object.getString("created_at"));
                                pojo.setUpdated_at(object.getString("updated_at"));
                                pojo.setName(object.getString("name"));

                                technicianNameList.add(object.getString("name"));
                                technicianList.add(pojo);
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
                        Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();

                    }
                });

        queue.add(objectRequest);

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
                            techSpinner.setAdapter(dataAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "error in network", Toast.LENGTH_SHORT).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    @Override
    public void editBattery(int position) {
        final NewBatteryPojo.NewBatteryItem pojo = newBatteryItemsList.get(position);
        if (pojo != null) {
            batteryId = String.valueOf(pojo.getId());
            listViewClicked = true;
            Log.d("12345", "show  "+listViewClicked);

            productId = pojo.getProduct_id();
            addBatteryForm.setVisibility(View.VISIBLE);
            newBatteryList.setVisibility(View.GONE);
            btnClick = "edit";

            final String bSource = pojo.getBattery_source();


            int spinnerPosition = batterySourceAdapter.getPosition(bSource);
            addBatterySourceSpinner.setSelection(spinnerPosition);

            if (bSource.equals("Supplier")) {
                spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, supplierNameList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addTechOrSupIdSpinner.setAdapter(spinnerAdapter);

                sourceTypeLabel.setText("Supplier");

                int p = spinnerAdapter.getPosition(pojo.getSupplier_name());
//                                    int p = spinnerAdapter.getPosition("Fritz Wold");
                addTechOrSupIdSpinner.setSelection(p);
            } else {
                spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, technicianNameList);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addTechOrSupIdSpinner.setAdapter(spinnerAdapter);
                sourceTypeLabel.setText("Technician");

                int p = spinnerAdapter.getPosition(pojo.getTechnician_name());
                addTechOrSupIdSpinner.setSelection(p);
            }

            batteryName.setText(pojo.getBrand_name());
            addLowerRetailSalePriceEditText.setText(String.valueOf(pojo.getLower_retail_sale_price()));
            addHigherRetailSalePriceEditText.setText(String.valueOf(pojo.getHigher_retail_sale_price()));
            addSalePriceEditText.setText(String.valueOf(pojo.getRetail_sale_price()));
            addCostPriceEditText.setText(String.valueOf(pojo.getCost_price()));
            addQuantityEditText.setText(String.valueOf(pojo.getQuantity()));

            int p = salePriceSpinnerAdapter.getPosition(pojo.getSale_price_type());
            addSalePriceTypeSpinner.setSelection(p);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (bSource.equals("Supplier")) {

                        int position = spinnerAdapter.getPosition(pojo.getSupplier_name());
                        addTechOrSupIdSpinner.setSelection(position);
                    } else {

                        int position = spinnerAdapter.getPosition(pojo.getTechnician_name());
                        addTechOrSupIdSpinner.setSelection(position);
                    }

                }
            }, 200);

        }
    }

    @Override
    public void deleteBattery(int position) {

    }

    @Override
    public void assignBattery(int position) {

        form.setVisibility(View.VISIBLE);

        NewBatteryPojo.NewBatteryItem pojo = newBatteryItemsList.get(position);
        if (pojo != null) {
            allocateBatteryId = String.valueOf(pojo.getId());

            btryAvailableQty.setText(String.valueOf(pojo.getQuantity()));
            btrySize.setText(pojo.getBattery_size());
            btrySku.setText(pojo.getBattery_sku());
            btryBrand.setText(pojo.getBrand_name());
        }

    }

    private interface UploadAPIs {
        @Multipart
        @POST("stock/api/upload-battery-image")
        Call<ImgeUploadPojo> uploadImage(@Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);


        @Multipart
        @POST
        Call<ImgeUploadPojo> uploadEditedImage(@Url String url, @Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);

    }

    private void uploadImageToServer(File imageFile, final Map<String, String> map) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);


        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("battery_image", imageFile.getName(), requestBody);

        Call<ImgeUploadPojo> call = uploadAPIs.uploadImage(part);
        call.enqueue(new Callback<ImgeUploadPojo>() {
            @Override
            public void onResponse(Call<ImgeUploadPojo> call, retrofit2.Response<ImgeUploadPojo> response) {

                dialog.cancel();
                if (response.body().getMessage().equals("Battery Image Uploaded Successfully.")) {

//                    submitNewRecord(map);
                } else {
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<ImgeUploadPojo> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }

    private void editUploadImageToServer(File imageFile, final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();
        Retrofit retrofit = NetworkClient.getRetrofitClient(getContext());
        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("battery_image", imageFile.getName(), requestBody);

        Call<ImgeUploadPojo> call = uploadAPIs.uploadEditedImage(editImageUploadURL, part);
        call.enqueue(new Callback<ImgeUploadPojo>() {
            @Override
            public void onResponse(Call<ImgeUploadPojo> call, retrofit2.Response<ImgeUploadPojo> response) {

                dialog.cancel();
                if (response.body().getMessage().equals("Battery Image Uploaded Successfully.")) {

                    editNewBatteryRecord(map);
                } else {
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void onFailure(Call<ImgeUploadPojo> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }


    class ProductListAdapter extends ArrayAdapter {

        private Context context;
        private ArrayList<ProductPojo.ProductItem> arrayList;

        private LayoutInflater inflater;

        public ProductListAdapter(Context context, int resource, ArrayList<ProductPojo.ProductItem> arrayList) {
            super(context, resource, arrayList);

            this.arrayList = arrayList;
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = inflater.inflate(R.layout.simple_spinner_item, parent, false);

            productName = view.findViewById(R.id.technicianName);

            final ProductPojo.ProductItem item = productItemList.get(position);
            if (item != null) {
                productName.setText(item.getBrand_name() + " , " + item.getBattery_sku());

                productName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    handle click here
                        listViewClicked = true;
                        Log.d("12345", "show  "+listViewClicked);

                        productId = item.getId();
                        batteryName.setText(item.getBrand_name());
                        productNameListView.setVisibility(View.GONE);

                    }
                });

            }

            return view;
        }
    }




}
