package android.app.mobilebattery.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.ProductRecyclerAdapter;
import android.app.mobilebattery.Helper.HideKeyBoard;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Model.ProductModel;
import android.app.mobilebattery.Pojo.BatteryBrandPojo;
import android.app.mobilebattery.Pojo.ImgeUploadPojo;
import android.app.mobilebattery.Pojo.ProductPojo;
import android.app.mobilebattery.Pojo.UploadProductDataPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
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


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class ProductFragment extends Fragment implements ProductRecyclerAdapter.Action {

    private Button addProductBtn, saveProductBtn;
    private EditText searchBySKUET;
    private ImageButton refreshFilter;
    private RecyclerView productRecyclerList;
    private TextView noResultFoundTV;
    private ScrollView addProductForm;
    private ImageView closeFormBtn, selectedBatteryImg;
    private EditText addBrandNameET, addProductSKUET, addSizeET, addBatteryImgET;
    private ListView brandNameLV;

    private TextView productName;
    private List<BatteryBrandPojo.BatteryBrandItem> brandItemList;

    private final int CODE_GALLERY_REQUEST = 999;
    private Bitmap bitmap;
    private File file;
    private Boolean selectImgClicked = false, formClick = false, listViewClicked;
    private int currentproductId, brandId;

    private ProductRecyclerAdapter recyclerAdapter;
    private ProductModel model;
    private PagedList<ProductPojo.ProductItem> productItemsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        
        init(view);
        methodListener();
        loadProductList();
        new HideKeyBoard().setupUI(addProductForm, getActivity());
        
        
        return view;
    }

    private void init(View view) {

        addProductBtn = view.findViewById(R.id.addProductBtn);
        saveProductBtn = view.findViewById(R.id.saveProductBtn);
        searchBySKUET = view.findViewById(R.id.searchBySKUET);
        refreshFilter = view.findViewById(R.id.refreshFilter);
        productRecyclerList = view.findViewById(R.id.productRecyclerList);
        noResultFoundTV = view.findViewById(R.id.noResultFoundTV);
        addProductForm = view.findViewById(R.id.addProductForm);
        closeFormBtn = view.findViewById(R.id.closeFormBtn);
        selectedBatteryImg = view.findViewById(R.id.selectedBatteryImg);
        addBrandNameET = view.findViewById(R.id.addBrandNameET);
        addProductSKUET = view.findViewById(R.id.addProductSKUET);
        addSizeET = view.findViewById(R.id.addSizeET);
        addBatteryImgET = view.findViewById(R.id.addBatteryImgET);
        brandNameLV = view.findViewById(R.id.brandNameLV);

    }

    private void methodListener() {

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProductForm.setVisibility(View.VISIBLE);
                selectedBatteryImg.setVisibility(View.GONE);
                formClick = false;
            }
        });
        closeFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });

        addBatteryImgET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImgClicked = true;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image!"), CODE_GALLERY_REQUEST);
                }
            }
        });

        saveProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addBrandNameET.getText().toString();
                String sku = addProductSKUET.getText().toString();
                String size = addSizeET.getText().toString();
                String img = addBatteryImgET.getText().toString();

                if (name.equals("") || sku.equals("") || size.equals("")){
                    Toast.makeText(getActivity(), "All fields required !", Toast.LENGTH_SHORT).show();
                }else if (img.equals("")){
                    Toast.makeText(getActivity(), "Choose Battery Image!", Toast.LENGTH_SHORT).show();
                }else {

                    Map<String, String> map = new HashMap<>();
                    map.put("battery_sku", sku);
                    map.put("brand_id", String.valueOf(brandId));
//                    map.put("brand_id", "3");
                    map.put("battery_size", size);
                    map.put("battery_image", img);

                    JSONObject object = new JSONObject();
                    try {
                        object.put("battery_sku", sku);
                        object.put("brand_id", String.valueOf(brandId));
                        object.put("battery_size", size);
                        object.put("battery_image", img);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Log.d("12345", map.toString());

                    if (selectImgClicked){
                        if (formClick)
                            editUploadImage(file, map);
                        else
                            uploadImage(file, map);

                    }else {
//                        uploadData(map, object);
                        uploadProductData(map);
                    }
                }
            }
        });


        addBrandNameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (addBrandNameET.hasFocus()) {
                    Log.d("12345", "yes focus");

                    if (count == 0)
                        brandNameLV.setVisibility(View.GONE);
                    else {
//                        searchBrand(s.toString().toLowerCase());
                        searchBrandName(s);
                        brandNameLV.setVisibility(View.VISIBLE);
                    }

                }else {
                    Log.d("12345", "no focus");
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchBySKUET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

//                if (addBrandNameET.hasFocus()) {
                    if (count != 0){

                        if (model != null)
                            if (model.liveDataSource.getValue() != null)
                                model.liveDataSource.getValue().invalidate();

                        model = new ProductModel("sku",s.toString().toLowerCase());

                        model.itemListData.observe(getActivity(), new Observer<PagedList<ProductPojo.ProductItem>>() {
                            @Override
                            public void onChanged(PagedList<ProductPojo.ProductItem> productItems) {
                                productItemsList = productItems;
                                recyclerAdapter.submitList(productItems);
                            }
                        });

                        productRecyclerList.setAdapter(recyclerAdapter);
                    }else {
                        loadProductList();
                    }

//                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        refreshFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBySKUET.setText("");
                searchBySKUET.clearFocus();
                loadProductList();
            }
        });



    }

    private void clearForm() {
        addProductForm.setVisibility(View.GONE);
        addBrandNameET.setText("");
        addBrandNameET.clearFocus();
        addProductSKUET.setText("");
        addProductSKUET.clearFocus();
        addSizeET.setText("");
        addSizeET.clearFocus();
        addBatteryImgET.setText("");
        addBatteryImgET.clearFocus();
        selectedBatteryImg.setVisibility(View.GONE);
        brandNameLV.setVisibility(View.GONE);
        selectImgClicked = false;
    }


    private void loadProductList() {

        if (getActivity() != null) {
            productRecyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));
            productRecyclerList.setItemAnimator(new DefaultItemAnimator());

            recyclerAdapter = new ProductRecyclerAdapter(getActivity() );
            recyclerAdapter.action = this;

            if (model != null)
                if (model.liveDataSource.getValue() != null)
                    model.liveDataSource.getValue().invalidate();


            model = new ProductModel();
            model.itemListData.observe(getActivity(), new Observer<PagedList<ProductPojo.ProductItem>>() {
                @Override
                public void onChanged(PagedList<ProductPojo.ProductItem> productItems) {
                    recyclerAdapter.submitList(productItems);
                    productItemsList = productItems;
                }
            });
            productRecyclerList.setAdapter(recyclerAdapter);
        }

    }

    @Override
    public void editBattery(int position) {
        formClick = true;

        ProductPojo.ProductItem item = productItemsList.get(position);

        if (item != null){

            addProductForm.setVisibility(View.VISIBLE);
            selectImgClicked = false;

            currentproductId = item.getId();
            brandId = item.getBrand_id();
            addBrandNameET.setText(item.getBrand_name());
            addProductSKUET.setText(item.getBattery_sku());
            addSizeET.setText(item.getBattery_size());
            addBatteryImgET.setText(item.getBattery_image().substring(item.getBattery_image().lastIndexOf('/')+1));

            Picasso.get()
                    .load(item.getBattery_image())
                    .placeholder(R.drawable.app_logo)
                    .into(selectedBatteryImg);
            selectedBatteryImg.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void deleteBattery(int position) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //TODO: action
            Uri filePath = data.getData();

            String path = getRealPathFromUri(getActivity(), filePath);
            String imageFileName = path.substring(path.lastIndexOf("/") + 1);
            addBatteryImgET.setText(imageFileName);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                selectedBatteryImg.setImageBitmap(bitmap);
                selectedBatteryImg.setVisibility(View.VISIBLE);

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

    private void uploadImage(File file, final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData("battery_image", file.getName(), requestBody);

//        Call<ImgeUploadPojo> call = uploadAPIs.uploadImage(part);
        NetworkClient.getInstance()
                .getApi()
                .uploadProductImage(part)
                .enqueue(new Callback<ImgeUploadPojo>() {
                    @Override
                    public void onResponse(Call<ImgeUploadPojo> call, retrofit2.Response<ImgeUploadPojo> response) {

                        dialog.cancel();
                        if (response.body().getMessage().equals("Battery Image Uploaded Successfully.")) {
                            uploadProductData(map);

//                            uploadData(map, object);
                        }else if (response.body().getMessage().equals("Battery image can not be empty.")){

                            Log.d("12345", "Battery image can not be empty.");

                        }else if (response.body().getMessage().equals("Image Upload Failed.")){

                            Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }else {
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

    private void editUploadImage(File file, final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part part = MultipartBody.Part.createFormData("battery_image", file.getName(), requestBody);

        NetworkClient.getInstance()
                    .getApi()
                    .uploadEditedProductImage(URL_Helper.PRODUCT_IMAGE_UPLOAD_URL+currentproductId, part)
                    .enqueue(new Callback<ImgeUploadPojo>() {
            @Override
            public void onResponse(Call<ImgeUploadPojo> call, retrofit2.Response<ImgeUploadPojo> response) {

                dialog.cancel();
                if (response.body().getMessage().equals("Battery Image Uploaded Successfully.")) {

                    uploadProductData(map);
//                    uploadData(map, object);
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

    private void uploadProductData(Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        Call<UploadProductDataPojo> call;

        String url;
        if (formClick) {
            url = URL_Helper.EDIT_PRODUCT_URL + currentproductId;
            call = NetworkClient.getInstance()
                    .getApi()
                    .uploadEditedProductData(url,map.get("battery_sku"),map.get("battery_image"),map.get("battery_size"),Integer.valueOf(map.get("brand_id")) );

        }else {
//            url = URL_Helper.ADD_PRODUCT_URL;
            call = NetworkClient.getInstance()
                    .getApi()
                    .uploadProductData(map.get("battery_sku"),map.get("battery_image"),map.get("battery_size"),Integer.valueOf(map.get("brand_id")) );

        }

        call.enqueue(new Callback<UploadProductDataPojo>() {
                    @Override
                    public void onResponse(Call<UploadProductDataPojo> call, retrofit2.Response<UploadProductDataPojo> response) {
                        dialog.cancel();
                        Log.d("12345", "message "+response.body());
                        if (response.body() != null) {
                            Log.d("12345", "message "+response.body().getMessage());
                            String msg = response.body().getMessage();
                            if (msg.equals("Product Added Successfully.")){
                                Toast.makeText(getActivity(), "Product Added Successfully.", Toast.LENGTH_SHORT).show();
                                clearForm();
                                loadProductList();
                            }else if (msg.equals("Product details updated successfully.")){
                                Toast.makeText(getActivity(), "Product details updated successfully.", Toast.LENGTH_SHORT).show();
                                clearForm();
                                loadProductList();
                            }else if (msg.equals("Technical Error.")){
                                Toast.makeText(getActivity(), "Technical Error.", Toast.LENGTH_SHORT).show();
                            }else if (msg.equals("Product with this sku already exists.")){
                                Toast.makeText(getActivity(), "Product with this sku already exists.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Error in upload data, Try again", Toast.LENGTH_SHORT).show();
                                Log.d("12345", msg);
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<UploadProductDataPojo> call, Throwable t) {
                        dialog.cancel();
                        Log.d("12345", "error "+t.getMessage());
                        Toast.makeText(getActivity(), "Error in Network", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void searchBrandName(CharSequence s) {

        final String text = s.toString().toLowerCase();
        brandItemList = new ArrayList<>();
        Log.d("12345", "search  " + s );

        String url = URL_Helper.BATTERY_BRAND_SEARCH_URL + text;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Boolean error = response.getBoolean("error");
                            if (error) {
                                Toast.makeText(getContext(), "no data found with this name", Toast.LENGTH_SHORT).show();
                            } else {

                                Log.d("12345", "response ");

                                brandNameLV.setVisibility(View.VISIBLE);
                                BatteryBrandPojo p = new BatteryBrandPojo();
                                JSONArray array = response.getJSONArray("data");
                                if (array.length() != 0) {
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        Log.d("12345", "array " + array.length());

                                        BatteryBrandPojo.BatteryBrandItem item = p.new BatteryBrandItem();

                                        item.setId(object.getInt("id"));
                                        item.setBrand_name(object.getString("brand_name"));
                                        item.setStatus(object.getBoolean("status"));
                                        item.setCreated_at(object.getString("created_at"));
                                        item.setUpdated_at(object.getString("updated_at"));

                                        brandItemList.add(item);
                                    }

                                    brandNameLV.setAdapter(new BrandListAdapter(getContext(), R.layout.simple_spinner_item, brandItemList));


                                } else {
                                    Toast.makeText(getContext(), "no data found", Toast.LENGTH_SHORT).show();
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12345", "error " + error.getMessage());
                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }


    private void searchBrand(String text){
        brandItemList = new ArrayList<>();
        NetworkClient.getInstance()
                .getApi()
                .searchBrandList(text)
                .enqueue(new Callback<BatteryBrandPojo>() {
                    @Override
                    public void onResponse(Call<BatteryBrandPojo> call, Response<BatteryBrandPojo> response) {
                        BatteryBrandPojo obj = new BatteryBrandPojo();
                        if (response.body() != null){
                            if (!response.body().getError()){
                                if (response.body().getData().size() != 0){
                                    for (int i = 0; i< response.body().getData().size();i++)
                                    {
                                        BatteryBrandPojo.BatteryBrandItem item = response.body().getData().get(i);

                                        BatteryBrandPojo.BatteryBrandItem pojo = obj.new BatteryBrandItem();

                                        pojo.setId(item.getId());
                                        pojo.setBrand_name(item.getBrand_name());
                                        pojo.setStatus(item.getStatus());
                                        pojo.setCreated_at(item.getCreated_at());
                                        pojo.setUpdated_at(item.getUpdated_at());

                                        brandItemList.add(pojo);
                                    }
//                                    brandItemList = response.body().getData();

                                    BrandListAdapter adapter = new BrandListAdapter(getActivity(), R.layout.simple_spinner_item, brandItemList);
                                    brandNameLV.setAdapter(adapter);
                                }

                            }else {
                                Toast.makeText(getActivity(), "No record found with this name", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<BatteryBrandPojo> call, Throwable t) {
                        Toast.makeText(getActivity(), "No record found with this name", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    class BrandListAdapter extends ArrayAdapter {

        private Context context;
        private List<BatteryBrandPojo.BatteryBrandItem> brandItemList;


        private LayoutInflater inflater;

        public BrandListAdapter(Context context, int resource, List<BatteryBrandPojo.BatteryBrandItem> brandItemList) {
            super(context, resource);
            this.brandItemList = brandItemList;

            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return brandItemList.size();
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = inflater.inflate(R.layout.simple_spinner_item, parent, false);

            productName = view.findViewById(R.id.technicianName);

            final BatteryBrandPojo.BatteryBrandItem item = brandItemList.get(position);
            if (item != null) {
                productName.setText(item.getBrand_name());

                productName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                    handle click here
                        listViewClicked = true;
                        Log.d("12345", "show  "+listViewClicked);

                        brandId = item.getId();
                        addBrandNameET.setText(item.getBrand_name());
                        brandNameLV.setVisibility(View.GONE);

                    }
                });

            }

            return view;
        }
    }




}
