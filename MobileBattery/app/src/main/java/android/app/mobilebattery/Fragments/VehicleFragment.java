package android.app.mobilebattery.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.VehicleAdapter;
import android.app.mobilebattery.Adapter.VehicleInsuranceListAdapter;
import android.app.mobilebattery.Adapter.VehicleServiceListAdapter;
import android.app.mobilebattery.Helper.HelperClass;
import android.app.mobilebattery.Helper.HideKeyBoard;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.RecyclerViewListener;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Interface.ApiInterface;
import android.app.mobilebattery.Model.VehicleViewModel;
import android.app.mobilebattery.Pojo.UploadExpensesImagePojo;
import android.app.mobilebattery.Pojo.VehicleInsuraceListPojo;
import android.app.mobilebattery.Pojo.VehiclePojo;
import android.app.mobilebattery.Pojo.VehicleServiceListPojo;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

import static android.app.Activity.RESULT_OK;

public class VehicleFragment extends Fragment implements VehicleAdapter.EditVehicle, VehicleAdapter.DeleteVehicle,
        VehicleAdapter.AssignVehicle, VehicleInsuranceListAdapter.EditInsuranceDetails,
        VehicleInsuranceListAdapter.DeleteInsuranceDetails, VehicleServiceListAdapter.EditServiceDetails,
        VehicleServiceListAdapter.DeleteServiceDetails{


    private EditText numberPlateEditText, registrationDateEditText, renewalDateEditText, kmReadingEditText;
    private EditText insuranceIdEditT, insuranceDateEditT, renewDateEditT, receiptIdEditT;
    private TextView receiptImageText, selectImage;
    private Button addNewVehicleBtn, addVehicle, assignVehicle, saveVehicleInsurance;
    private ImageView closeVehicleInsurance, closeVehicleFormBtn;
    private ScrollView newVehicleForm, addVehicleInsuranceForm;
    private RelativeLayout assignVehicleForm;
    private Spinner techNameSpinner;

    private RecyclerView vehicleRecyclerView;

//    view Of assign Vehicle To Technician

    private TextView asgnToTechRegNo, asgnToTechRegDate, asgnToTechRenDate, asgnToTechKM;

//    views of vehicle details
    private RelativeLayout vehicleFullDetailsLayout, insuranceListLayout, serviceListLayout;
    private ImageView closeVehicleFullDetailsForm, closeVehicleServiceForm;
    private Button addVehicleInsuranceBtn, addVehicleServiceBtn;
    private TextView techDetailName, vehicleDetailNoPlate, vehicleDetailKMReading, vehicleDetailRegDate, vehicleDetailRenDate;
    private TextView showInsuranceHistoryList, showServiceHistoryList;
    private ListView insuranceHistoryListView, serviceHistoryListView;
    private ArrayList<VehicleInsuraceListPojo> insuranceArrayList;
    private ArrayList<VehicleServiceListPojo> serviceArrayList;
    private VehicleInsuranceListAdapter insuranceListAdapter;
    private VehicleServiceListAdapter serviceListAdapter;
    private LinearLayout mainLayout;

//    views of vehicle service form
    private ScrollView addVehicleServiceForm;
    private EditText serviceIdEditT, kmReadingEditT, serviceDateEditT, nextServiceDateEditT, serviceReceiptIdEditT, nextServiceKMEditT;
    private TextView serviceReceiptImageText, selectServiceReceiptImage;
    private Button saveVehicleService;
    private RelativeLayout relativeLayout;

    private String btnClick = "add";
    private int editId = 1;
    private String str, currentVehicleID, currentVehicleInsID, currentVehicleSrvcID;

    private String saveInsBtnClick, saveSrvcBtnClick;


    private ArrayList<String> techNameList;
    private ArrayList<String> techIdList;
    private ArrayAdapter<String> dataAdapter;

    private RequestQueue queue;

    private Boolean clickedInsImg = false, clickedServiseImg = false;
    private final int CODE_GALLERY_REQUEST = 999;
    private TextView currentImageET;
    private File file;

    private ApiInterface apiInterface;
    private PagedList<VehiclePojo.VehicleItem> vehicleItemsList;

    private Bitmap bitmap;
    private static final int PICK_IMAGE = 1;



    private VehicleViewModel viewModel;
    private VehicleAdapter adapter;


    public VehicleFragment(){/* Required empty public constructor*/}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vehicle_fragment_layout, container, false);

        init(view);
        queue = Volley.newRequestQueue(getActivity());
        methodListener();

        apiInterface = NetworkClient.getRetrofitClient(getActivity()).create(ApiInterface.class);

        loadRecyclerViewList();

        getTechList();
        new HideKeyBoard().setupUI(relativeLayout, getActivity());

        return view;
    }

    private void init(View view) {

        mainLayout = view.findViewById(R.id.mainLinearLayout);
        vehicleRecyclerView = view.findViewById(R.id.vehicleRecyclerView);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        saveVehicleService = view.findViewById(R.id.saveVehicleService);
        selectServiceReceiptImage = view.findViewById(R.id.selectServiceReceiptImage);
        serviceReceiptImageText = view.findViewById(R.id.serviceReceiptImageText);
        nextServiceKMEditT = view.findViewById(R.id.nextServiceKMEditT);
        serviceReceiptIdEditT = view.findViewById(R.id.serviceReceiptIdEditT);
        nextServiceDateEditT = view.findViewById(R.id.nextServiceDateEditT);
        serviceDateEditT = view.findViewById(R.id.serviceDateEditT);
        kmReadingEditT = view.findViewById(R.id.kmReadingEditT);
        serviceIdEditT = view.findViewById(R.id.serviceIdEditT);

        closeVehicleServiceForm = view.findViewById(R.id.closeVehicleServiceForm);
        addVehicleServiceForm = view.findViewById(R.id.addVehicleServiceForm);
        serviceListLayout = view.findViewById(R.id.serviceListLayout);
        insuranceListLayout = view.findViewById(R.id.insuranceListLayout);
        vehicleFullDetailsLayout = view.findViewById(R.id.vehicleFullDetailsLayout);
        closeVehicleFullDetailsForm = view.findViewById(R.id.closeVehicleFullDetailsForm);
        addVehicleServiceBtn = view.findViewById(R.id.addVehicleServiceBtn);
        techDetailName = view.findViewById(R.id.techDetailName);
        vehicleDetailNoPlate = view.findViewById(R.id.vehicleDetailNoPlate);
        vehicleDetailKMReading = view.findViewById(R.id.vehicleDetailKMReading);
        vehicleDetailRegDate = view.findViewById(R.id.vehicleDetailRegDate);
        vehicleDetailRenDate = view.findViewById(R.id.vehicleDetailRenDate);
        showInsuranceHistoryList = view.findViewById(R.id.showInsuranceHistoryList);
        showServiceHistoryList = view.findViewById(R.id.showServiceHistoryList);
        insuranceHistoryListView = view.findViewById(R.id.insuranceHistoryListView);
        serviceHistoryListView = view.findViewById(R.id.serviceHistoryListView);


        techNameSpinner = view.findViewById(R.id.techNameSpinner);
        selectImage = view.findViewById(R.id.selectImage);
        receiptImageText = view.findViewById(R.id.receiptImageText);
        addVehicleInsuranceBtn = view.findViewById(R.id.addVehicleInsuranceBtn);
        receiptIdEditT = view.findViewById(R.id.receiptIdEditT);
        renewDateEditT = view.findViewById(R.id.renewDateEditT);
        insuranceDateEditT = view.findViewById(R.id.insuranceDateEditT);
        insuranceIdEditT = view.findViewById(R.id.insuranceIdEditT);
        saveVehicleInsurance = view.findViewById(R.id.addVehicleInsurance);
        addVehicleInsuranceForm = view.findViewById(R.id.addVehicleInsuranceForm);
        closeVehicleInsurance = view.findViewById(R.id.closeVehicleInsurance);
        assignVehicle = view.findViewById(R.id.assignVehicle);
        assignVehicleForm = view.findViewById(R.id.assignVehicleForm);
        numberPlateEditText = view.findViewById(R.id.numberPlateEditText);
        registrationDateEditText = view.findViewById(R.id.registrationDateEditText);
        renewalDateEditText = view.findViewById(R.id.renewalDateEditText);
        kmReadingEditText = view.findViewById(R.id.kmReadingEditText);
        addNewVehicleBtn = view.findViewById(R.id.addNewVehicleBtn);
        closeVehicleFormBtn = view.findViewById(R.id.closeVehicleFormBtn);
        addVehicle = view.findViewById(R.id.addVehicle);
        newVehicleForm = view.findViewById(R.id.newVehicleForm);

        asgnToTechKM = view.findViewById(R.id.asgnToTechKM);
        asgnToTechRenDate = view.findViewById(R.id.asgnToTechRenDate);
        asgnToTechRegDate = view.findViewById(R.id.asgnToTechRegDate);
        asgnToTechRegNo = view.findViewById(R.id.asgnToTechRegNo);

    }

    private void methodListener() {

        registrationDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDate(registrationDateEditText);
                new HelperClass(getActivity()).getDate(registrationDateEditText);
            }
        });
        renewalDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDate(renewalDateEditText);
                new HelperClass(getActivity()).getDate(renewalDateEditText);
            }
        });

        addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchEnteredData();
            }
        });

        addNewVehicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setVisibility(View.GONE);
                newVehicleForm.setVisibility(View.VISIBLE);
                btnClick = "add";
                clearAllFields();
            }
        });
        closeVehicleFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainLayout.setVisibility(View.VISIBLE);
                newVehicleForm.setVisibility(View.GONE);
            }
        });

        assignVehicleForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignVehicleForm.setVisibility(View.GONE);
            }
        });

        addVehicleInsuranceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInsBtnClick = "add";
                insuranceIdEditT.setText("");
                insuranceDateEditT.setText("");
                renewDateEditT.setText("");
                receiptIdEditT.setText("");
                receiptImageText.setText("");
                addVehicleInsuranceForm.setVisibility(View.VISIBLE);
            }
        });

        closeVehicleInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVehicleInsuranceForm.setVisibility(View.GONE);
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedInsImg = true;
                currentImageET = receiptImageText;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Insurance Receipt !"), CODE_GALLERY_REQUEST );

                }
            }
        });

        selectServiceReceiptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedServiseImg = true;
                currentImageET = serviceReceiptImageText;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Service Receipt !"), CODE_GALLERY_REQUEST );

                }
            }
        });

        insuranceDateEditT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDate(insuranceDateEditT);
                new HelperClass(getActivity()).getDate(insuranceDateEditT);
            }
        });
        renewDateEditT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDate(renewDateEditT);
                new HelperClass(getActivity()).getDate(renewDateEditT);
            }
        });

        saveVehicleInsurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String insuranceId = insuranceIdEditT.getText().toString();
                String insuranceDate = insuranceDateEditT.getText().toString();
                String renewDate = renewDateEditT.getText().toString();
                String receiptId = receiptIdEditT.getText().toString();
                String receiptImage = receiptImageText.getText().toString();

                if (insuranceId.equals("")||insuranceDate.equals("")||renewDate.equals("")||receiptId.equals("")){
                    Toast.makeText(getActivity(), "All fields required !", Toast.LENGTH_SHORT).show();
                }else if (receiptImage.equals("")){
                    Toast.makeText(getActivity(), "Select ", Toast.LENGTH_SHORT).show();
                }else {


                    Map<String, String> map = new HashMap<>();
                    map.put("vehicle_id", currentVehicleID);
                    map.put("insurance_id", insuranceId);
                    map.put("insurance_date", insuranceDate);
                    map.put("renew_date", renewDate);
                    map.put("receipt_id", receiptId);
                    map.put("receipt_image", receiptImage);

                    if (clickedInsImg){
                        if (saveInsBtnClick.equals("add"))
                            uploadVehicleInsuranceReceipt(file, map);
                        else if (saveInsBtnClick.equals("edit"))
                            editVehicleInsuranceReceipt(file, map, Integer.valueOf(currentVehicleInsID));
                    }else
                        addVehicleInsuranceOnServer(map);
                }


            }
        });




        serviceDateEditT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getDate(serviceDateEditT);
                new HelperClass(getActivity()).getDate(serviceDateEditT);
            }
        });

        nextServiceDateEditT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HelperClass(getActivity()).getDate(nextServiceDateEditT);
//                getDate(nextServiceDateEditT);
            }
        });

        saveVehicleService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_id = serviceIdEditT.getText().toString();
                String km = kmReadingEditT.getText().toString();
                String s_date = serviceDateEditT.getText().toString();
                String n_s_date = nextServiceDateEditT.getText().toString();
                String s_r_id = serviceReceiptIdEditT.getText().toString();
                String n_s_km = nextServiceKMEditT.getText().toString();
                String imageName = serviceReceiptImageText.getText().toString();

                if (s_id.equals("")||km.equals("")||s_date.equals("")||n_s_date.equals("")||s_r_id.equals("")||n_s_km.equals("")){
                    Toast.makeText(getActivity(), "All fields required !", Toast.LENGTH_SHORT).show();
                }else if (imageName.equals("")){
                    Toast.makeText(getActivity(), "Select Service Receipt ", Toast.LENGTH_SHORT).show();
                }else {

                    Map<String, String> map = new HashMap<>();
                    map.put("vehicle_id", currentVehicleID);
                    map.put("service_id", s_id);
                    map.put("km_completed", km);
                    map.put("service_date", s_date);
                    map.put("receipt_id", s_r_id);
                    map.put("receipt_image", imageName);
                    map.put("next_service_date", n_s_date);
                    map.put("next_service_km", n_s_km);

                    if (clickedServiseImg){
                        if (saveSrvcBtnClick.equals("add"))
                            if (file == null){
                                Toast.makeText(getActivity(), "Select Service Receipt Image", Toast.LENGTH_SHORT).show();
                            }else
                                uploadVehicleServiceReceipt(file, map);
                        else if (saveSrvcBtnClick.equals("edit")) {
                            if (file == null){
                                Toast.makeText(getActivity(), "Select Service Receipt Image", Toast.LENGTH_SHORT).show();
                            }else
                                editVehicleServiceReceipt(file, map, Integer.valueOf(currentVehicleSrvcID));

                        }
                    }else
                        addVehicleServiceOnServer(map);




                }
            }
        });

        showInsuranceHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insuranceListLayout.setVisibility(View.VISIBLE);
                showInsuranceHistoryList.setTextColor(getResources().getColor(R.color.fontHeadingColor));
                serviceListLayout.setVisibility(View.GONE);
                showServiceHistoryList.setTextColor(getResources().getColor(R.color.fontContentColor));

            }
        });
        showServiceHistoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceListLayout.setVisibility(View.VISIBLE);
                showServiceHistoryList.setTextColor(getResources().getColor(R.color.fontHeadingColor));
                insuranceListLayout.setVisibility(View.GONE);
                showInsuranceHistoryList.setTextColor(getResources().getColor(R.color.fontContentColor));
            }
        });

        closeVehicleFullDetailsForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vehicleFullDetailsLayout.setVisibility(View.GONE);
                mainLayout.setVisibility(View.VISIBLE);
            }
        });

        addVehicleServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSrvcBtnClick = "add";
                serviceIdEditT.setText("");
                kmReadingEditT.setText("");
                serviceDateEditT.setText("");
                nextServiceDateEditT.setText("");
                serviceReceiptIdEditT.setText("");
                nextServiceKMEditT.setText("");
                serviceReceiptImageText.setText("");
                addVehicleServiceForm.setVisibility(View.VISIBLE);
            }
        });
        closeVehicleServiceForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addVehicleServiceForm.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image!"), CODE_GALLERY_REQUEST);

            }else {
                Toast.makeText(getActivity(), "You Don't have permission! ", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //TODO: action
            Uri filePath = data.getData();

            String path = getRealPathFromUri(getActivity(), filePath);
            String imageFileName = path.substring(path.lastIndexOf("/")+1);
            currentImageET.setText(imageFileName);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
//                userImage.setImageBitmap(bitmap);
//                userImage.setVisibility(View.VISIBLE);

                file = new File(getRealPathFromUri(getActivity(), filePath));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            Snackbar.make(getView(), "Select Another Image", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private String getRealPathFromUri(Context context, Uri contentUri) {

        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = 0;
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

            }
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private void loadRecyclerViewList() {

        if (getActivity() != null){


            vehicleRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            vehicleRecyclerView.setHasFixedSize(true);

            if (viewModel != null){
                viewModel.liveDataSource.getValue().invalidate();
            }

            viewModel = ViewModelProviders.of(getActivity()).get(VehicleViewModel.class);

            adapter = new VehicleAdapter(getActivity());
            adapter.assignVehicle = this;
            adapter.editVehicle = this;
            adapter.deleteVehicle = this;

            viewModel.itemPagedList.observe(getActivity(), new Observer<PagedList<VehiclePojo.VehicleItem>>() {
                @Override
                public void onChanged(PagedList<VehiclePojo.VehicleItem> vehicleItems) {
                    vehicleItemsList = vehicleItems;
                    adapter.submitList(vehicleItems);
                }
            });

            vehicleRecyclerView.setAdapter(adapter);

        }

    }

    private void addVehicleInsuranceOnServer(final Map<String, String> map) {

        Log.d("12345", "map "+map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_VEHICLE_INSURANCE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Insurance history added successfully.")){
                                Toast.makeText(getActivity(), "Insurance history added successfully.", Toast.LENGTH_SHORT).show();
                                addVehicleInsuranceForm.setVisibility(View.GONE);

                            }else if (message.equals("Vehicle id, insurance id, date, renew date, receipt id or image cannot be empty.")){
                                Toast.makeText(getActivity(), "Vehicle id, insurance id, date, renew date, receipt id or image cannot be empty.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "error in network ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    private void addVehicleServiceOnServer(final Map<String, String> map) {

        Log.d("12345", "map "+map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_VEHICLE_SERVICE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            Log.d("12345", message);
                            if (message.equals("Service history added successfully.")){
                                Toast.makeText(getActivity(), "Service history added successfully.", Toast.LENGTH_SHORT).show();
                                addVehicleServiceForm.setVisibility(View.GONE);

                            }else if (message.equals("Vehicle id, service id, completed kms, next service km, service, next service date, receipt id or image cannot be empty.")){
                                Toast.makeText(getActivity(), "All Fields required !", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "error in network ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    private void assignVehicleToTech(final int position, final String v_id, final String t_id) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ASSIGN_VEHICLE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Vehicle assigned successfully.")){
                                Toast.makeText(getActivity(), "Vehicle assigned successfully.  ", Toast.LENGTH_SHORT).show();
                                assignVehicleForm.setVisibility(View.GONE);

                                adapter.notifyItemChanged(position);

                                loadRecyclerViewList();

                            }else if (message.equals("Vehicle id, technician id cannot be empty.")){
                                Toast.makeText(getActivity(), "Vehicle id, technician id cannot be empty.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getActivity(), "error in network ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("vehicle_id", v_id);
                map.put("assigned_technician", t_id);
                return map;
            }
        };

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }

    private void fetchEnteredData() {
        final String numberPlate = numberPlateEditText.getText().toString();
        final String registrationDate = registrationDateEditText.getText().toString();
        final String renewalDate = renewalDateEditText.getText().toString();
        final String kmReading = kmReadingEditText.getText().toString();

        if (numberPlate.equals("") || registrationDate.equals("") || renewalDate.equals("") || kmReading.equals("")){
            Snackbar.make(getView(), "please fill all the fields", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }else {

            Map<String, String> map = new HashMap<>();
            map.put("number_plate",numberPlate);
            map.put("registration_date",registrationDate);
            map.put("renewal_date",renewalDate);
            map.put("km_completed",kmReading);

            if (btnClick.equals("add")){
                submitDataToServer(map);
            }else if (btnClick.equals("edit")){
                submitEditedDataToServer(map);
            }else {}


        }
    }

    private void submitDataToServer(final Map<String, String> map) {

        Log.d("12345","map "+ map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_VEHICLE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Vehicle Added Successfully.")){
                                Snackbar.make(getView(), "Vehicle Added Successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                mainLayout.setVisibility(View.VISIBLE);
                                newVehicleForm.setVisibility(View.GONE);
                                loadRecyclerViewList();

                            }else if (message.equals("Vehicle with this registration number already exists.")){
                                Snackbar.make(getView(), "Vehicle with this registration number already exists.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else {
                                Snackbar.make(getView(), "Email already exists.", Snackbar.LENGTH_SHORT)
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
                        Log.d("12345", "login error " + error.toString());
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void submitEditedDataToServer(final Map<String, String> map) {

        Log.d("12345","map "+ map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();


        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.EDIT_VEHICLE_URL+editId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Vehicle details updated successfully.")){

                                Snackbar.make(getView(), "Vehicle details updated successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                mainLayout.setVisibility(View.VISIBLE);
                                newVehicleForm.setVisibility(View.GONE);
                                loadRecyclerViewList();

                            }else if (message.equals("Vehicle with this registration number already exists.")){
                                Snackbar.make(getView(), "Vehicle with this registration number already exists.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else if (message.equals("Vehicle id is invalid.")){
                                Snackbar.make(getView(), "Vehicle id is invalid.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else {
                                Snackbar.make(getView(), "Vehicle already exists.", Snackbar.LENGTH_SHORT)
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
                        Log.d("12345", "login error " + error.toString());
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }


    private void clearAllFields(){

        numberPlateEditText.setText("");
        registrationDateEditText.setText("");
        renewalDateEditText.setText("");
        kmReadingEditText.setText("");
        addVehicle.setText("ADD VEHICLE");

    }

    @Override
    public void editVehicleDetails(int position) {
        VehiclePojo.VehicleItem pojo = vehicleItemsList.get(position);

        if (pojo != null) {
            editId = pojo.id;
            numberPlateEditText.setText(pojo.getNumber_plate());
            registrationDateEditText.setText(pojo.getRegistration_date());
            renewalDateEditText.setText(pojo.getRenewal_date());
            kmReadingEditText.setText(pojo.getKm_completed());
            addVehicle.setText("EDIT VEHICLE DETAILS");

            mainLayout.setVisibility(View.GONE);
            newVehicleForm.setVisibility(View.VISIBLE);
            btnClick = "edit";
        }

    }

    @Override
    public void deleteVehicleDetails(int position) {

    }

    @Override
    public void assignVehicleDetails(final int position) {
        assignVehicleForm.setVisibility(View.VISIBLE);
        VehiclePojo.VehicleItem pojo = vehicleItemsList.get(position);

        if (pojo != null) {
            final String vehicleId = String.valueOf(pojo.getId());

            asgnToTechRegNo.setText(pojo.getNumber_plate());
            asgnToTechRegDate.setText(pojo.getRegistration_date());
            asgnToTechRenDate.setText(pojo.getRenewal_date());
            asgnToTechKM.setText(pojo.getKm_completed());

            techNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (techNameList.get(position).equals("Select Tech.")) {
                        Snackbar.make(getView(), "Select Technician ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    } else {
                        str = techIdList.get(position);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            assignVehicle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    assignVehicleToTech(position, vehicleId, str);
                }
            });

        }

    }

    @Override
    public void clickedOnItem(int position) {

        VehiclePojo.VehicleItem item = vehicleItemsList.get(position);

        if (item != null){
            Log.d("12345", "battery id "+item.getId());
            getVehicleDetails(String.valueOf(item.id));
            currentVehicleID = String.valueOf(item.getId());
            vehicleFullDetailsLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void editInsuranceDetails(int position) {

        saveInsBtnClick = "edit";
        clickedInsImg = false;  
        addVehicleInsuranceForm.setVisibility(View.VISIBLE);
        VehicleInsuraceListPojo pojo = insuranceArrayList.get(position);

        currentVehicleInsID = pojo.getId();

        insuranceIdEditT.setText(pojo.getInsurance_id());
        insuranceDateEditT.setText(pojo.getInsurance_date());
        renewDateEditT.setText(pojo.getRenew_date());
        receiptIdEditT.setText(pojo.getReceipt_id());
        receiptImageText.setText(pojo.getReceipt_image().substring(pojo.getReceipt_image().lastIndexOf("/")+1 ));

    }

    @Override
    public void deleteInsuranceDetails(int position) {

    }

    @Override
    public void editServiceDetails(int position) {

        saveSrvcBtnClick = "edit";
        clickedServiseImg = false;
        VehicleServiceListPojo pojo = serviceArrayList.get(position);
        addVehicleServiceForm.setVisibility(View.VISIBLE);

        currentVehicleSrvcID = pojo.getId();

        serviceIdEditT.setText(pojo.getService_id());
        kmReadingEditT.setText(pojo.getKm_completed());
        serviceDateEditT.setText(pojo.getService_date());
        nextServiceDateEditT.setText(pojo.getNext_service_date());
        serviceReceiptIdEditT.setText(pojo.getReceipt_id());
        nextServiceKMEditT.setText(pojo.getNext_service_km());
        serviceReceiptImageText.setText(pojo.getReceipt_image().substring(pojo.getReceipt_image().lastIndexOf("/")+1));

    }

    @Override
    public void deleteServiceDetails(int position) {

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

                            for (int i=0; i<array.length(); i++ ){
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
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                });

//        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);


    }

    private void getVehicleDetails(String id){
        String URL = URL_Helper.VEHICLE_DETAILS_URL+"?id="+id;

        Log.d("12345", URL);

        insuranceArrayList = new ArrayList<>();
        serviceArrayList = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            String message = response.getString("message");
                            if (message.equals("Vehicle id is invalid.")){
                                Snackbar.make(getView(), "Vehicle id is invalid. ", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else {
                                JSONArray array = response.getJSONArray("data");

                                JSONObject object = array.getJSONObject(0);
                                techDetailName.setText(object.getString("technician_name"));
                                vehicleDetailNoPlate.setText(object.getString("number_plate"));
                                vehicleDetailKMReading.setText(object.getString("km_completed"));
                                vehicleDetailRegDate.setText(object.getString("registration_date"));
                                vehicleDetailRenDate.setText(object.getString("renewal_date"));
                                JSONArray insuranceArray = object.getJSONArray("insurance_histories");
                                for (int i=0; i<insuranceArray.length(); i++){
                                    JSONObject object1 = insuranceArray.getJSONObject(i);

                                    VehicleInsuraceListPojo pojo = new VehicleInsuraceListPojo();
                                    pojo.setId(object1.getString("id"));
                                    pojo.setVehicle_id(object1.getString("vehicle_id"));
                                    pojo.setInsurance_id(object1.getString("insurance_id"));
                                    pojo.setInsurance_date(object1.getString("insurance_date"));
                                    pojo.setRenew_date(object1.getString("renew_date"));
                                    pojo.setReceipt_id(object1.getString("receipt_id"));
                                    pojo.setReceipt_image(object1.getString("receipt_image"));
                                    pojo.setCreated_at(object1.getString("created_at"));
                                    pojo.setUpdated_at(object1.getString("updated_at"));


                                    insuranceArrayList.add(pojo);
                                }
                                JSONArray serviceArray = object.getJSONArray("service_histories");
                                for (int i=0; i<serviceArray.length(); i++){
                                    JSONObject object1 = serviceArray.getJSONObject(i);

                                    VehicleServiceListPojo pojo = new VehicleServiceListPojo();
                                    pojo.setId(object1.getString("id"));
                                    pojo.setVehicle_id(object1.getString("vehicle_id"));
                                    pojo.setService_id(object1.getString("service_id"));
                                    pojo.setKm_completed(object1.getString("km_completed"));
                                    pojo.setService_date(object1.getString("service_date"));
                                    pojo.setNext_service_date(object1.getString("next_service_date"));
                                    pojo.setReceipt_id(object1.getString("receipt_id"));
                                    pojo.setReceipt_image(object1.getString("receipt_image"));
                                    pojo.setCreated_at(object1.getString("created_at"));
                                    pojo.setUpdated_at(object1.getString("updated_at"));

                                    serviceArrayList.add(pojo);
                                }

                                setAdapterToList();


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
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                });


        queue.add(request);
    }

    private void setAdapterToList() {
        insuranceListAdapter = new VehicleInsuranceListAdapter(getActivity(),
                R.layout.vehicle_insurance_list_item, insuranceArrayList);
        insuranceListAdapter.editDetails = this;
        insuranceListAdapter.deleteDetails = this;
        insuranceHistoryListView.setAdapter(insuranceListAdapter);

        serviceListAdapter = new VehicleServiceListAdapter(getActivity(), R.layout.vehicle_insurance_list_item, serviceArrayList);
        serviceListAdapter.editDetails = this;
        serviceListAdapter.deleteDetails = this;
        serviceHistoryListView.setAdapter(serviceListAdapter);
    }


    private void uploadVehicleInsuranceReceipt(File file, final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", file.getName(),requestBody);

        Call<UploadExpensesImagePojo> call = apiInterface.uploadVehicleInsReceipt(part);
        call.enqueue(new Callback<UploadExpensesImagePojo>() {
            @Override
            public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {

                dialog.cancel();
                Log.d("12345", "message  "+response.body().getMessage());
                if (response.body().getMessage().equals("Receipt Image Uploaded Successfully.")){
                    Snackbar.make(getView(), "Receipt Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    addVehicleInsuranceOnServer(map);

                }else if (response.body().getMessage().equals("Image Upload Failed.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Image Could not saved.")){
                    Snackbar.make(getView(), "Image Could not saved.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Receipt image can not be empty.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d("12345", "Receipt image cannot be empty.");
                }

            }

            @Override
            public void onFailure(Call<UploadExpensesImagePojo> call, Throwable t) {
                dialog.cancel();
                Log.d("12345", "retrofit2 Error  "+t.getMessage());
                Snackbar.make(getView(), "Error in network, Try again", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }

    private void editVehicleInsuranceReceipt(File file, final Map<String, String> map, int id) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", file.getName(),requestBody);

        Call<UploadExpensesImagePojo> call = apiInterface.editVehicleInsReceipt( id ,part);
        call.enqueue(new Callback<UploadExpensesImagePojo>() {
            @Override
            public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {

                dialog.cancel();
                Log.d("12345", "message  "+response.body().getMessage());
                if (response.body().getMessage().equals("Receipt Image Uploaded Successfully.")){
                    Snackbar.make(getView(), "Receipt Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    addVehicleInsuranceOnServer(map);

                }else if (response.body().getMessage().equals("Image Upload Failed.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Image Could not saved.")){
                    Snackbar.make(getView(), "Image Could not saved.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Receipt image can not be empty.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d("12345", "Receipt image cannot be empty.");
                }

            }

            @Override
            public void onFailure(Call<UploadExpensesImagePojo> call, Throwable t) {
                dialog.cancel();
                Log.d("12345", "retrofit2 Error  "+t.getMessage());
                Snackbar.make(getView(), "Error in network, Try again", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }


    private void uploadVehicleServiceReceipt(File file, final Map<String, String> map) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", file.getName(),requestBody);

        Call<UploadExpensesImagePojo> call = apiInterface.uploadVehicleSrvcReceipt(part);
        call.enqueue(new Callback<UploadExpensesImagePojo>() {
            @Override
            public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {

                dialog.cancel();
                Log.d("12345", "message  "+response.body().getMessage());
                if (response.body().getMessage().equals("Receipt Image Uploaded Successfully.")){
                    Snackbar.make(getView(), "Receipt Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    addVehicleServiceOnServer(map);

                }else if (response.body().getMessage().equals("Image Upload Failed.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Image Could not saved.")){
                    Snackbar.make(getView(), "Image Could not saved.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Receipt image can not be empty.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d("12345", "Receipt image cannot be empty.");
                }

            }

            @Override
            public void onFailure(Call<UploadExpensesImagePojo> call, Throwable t) {
                dialog.cancel();
                Log.d("12345", "retrofit2 Error  "+t.getMessage());
                Snackbar.make(getView(), "Error in network, Try again", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }

    private void editVehicleServiceReceipt(File file, final Map<String, String> map, int id) {

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();


        RequestBody requestBody = RequestBody.create(MediaType.parse("image/"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", file.getName(),requestBody);

        Call<UploadExpensesImagePojo> call = apiInterface.editVehicleSrvcReceipt( id ,part);
        call.enqueue(new Callback<UploadExpensesImagePojo>() {
            @Override
            public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {

                dialog.cancel();
                Log.d("12345", "message  "+response.body().getMessage());
                if (response.body().getMessage().equals("Receipt Image Uploaded Successfully.")){
                    Snackbar.make(getView(), "Receipt Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    addVehicleServiceOnServer(map);

                }else if (response.body().getMessage().equals("Image Upload Failed.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Image Could not saved.")){
                    Snackbar.make(getView(), "Image Could not saved.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (response.body().getMessage().equals("Receipt image can not be empty.")){
                    Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    Log.d("12345", "Receipt image cannot be empty.");
                }

            }

            @Override
            public void onFailure(Call<UploadExpensesImagePojo> call, Throwable t) {
                dialog.cancel();
                Log.d("12345", "retrofit2 Error  "+t.getMessage());
                Snackbar.make(getView(), "Error in network, Try again", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

            }
        });

    }
}
