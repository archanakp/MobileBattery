package android.app.mobilebattery.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.ExpenseListAdapter;
import android.app.mobilebattery.DataSource.ExpenseDSFactory;
import android.app.mobilebattery.DataSource.ExpenseDataSource;
import android.app.mobilebattery.Helper.HelperClass;
import android.app.mobilebattery.Helper.HideKeyBoard;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Interface.ApiInterface;
import android.app.mobilebattery.Model.ExpenseViewModel;
import android.app.mobilebattery.Pojo.ExpenseListPojo;
import android.app.mobilebattery.Pojo.UploadExpensesImagePojo;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

public class ExpenseFragment extends Fragment implements ExpenseListAdapter.DeleteItemData,
                ExpenseListAdapter.EditItemData{

    private RelativeLayout relativeLayout, actionLayout;
    private Button addExpenseBtn;
    private EditText searchByDateET;
    private Spinner searchByUserIdSpinner;
    private RecyclerView expenseRecyclerList;

    private ImageView closeFormIV, receiptImageView;
    private Spinner userSpinner;
    private EditText amountET, purposeET, descriptionET, receiptImageET, dateET;
    private Button saveExpensesBtn;
    private ImageView refreshListIV;
    private ProgressBar progressBar;
    private TextView noResultFoundTV;

    private ExpenseViewModel viewModel;
    private ExpenseListAdapter expenseListAdapter;
    private LinearLayoutManager layoutManager;
    private PagedList<ExpenseListPojo.ExpenseListItem> expenseItemList;

    private Bitmap bitmap;
    private File file;
    private int CODE_GALLERY_REQUEST = 999;

    private static final int PAGE_START = 1;
    private boolean isLoading = false, isLastPage = false, selectImg = false;
    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START, currentExpenseId;
    private String userId, clickType;

    private ApiInterface apiInterface;



    private ArrayList<String> userNameList;
    private ArrayList<String> userIdList;
    private ArrayAdapter<String> spinnerAdapter;
    private Activity activity;

    public ExpenseFragment (){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.expense_fragment, container, false);

        init(view);

        if (getActivity() != null)
            activity = getActivity();

        methodListener();
        loadUserData();
        setAdapterToRecyclerView();
        new HideKeyBoard().setupUI(relativeLayout, activity);
        apiInterface = NetworkClient.getRetrofitClient(activity).create(ApiInterface.class);
//        expenseListAdapter = new BatteryBrandListAdapter(getActivity());


        return view;
    }

    private void init(View view) {

        noResultFoundTV = view.findViewById(R.id.noResultFoundTV);
        progressBar = view.findViewById(R.id.progressBar);
        actionLayout = view.findViewById(R.id.actionLayout);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        addExpenseBtn = view.findViewById(R.id.addExpenseBtn);
        searchByDateET = view.findViewById(R.id.searchByDateET);
        searchByUserIdSpinner = view.findViewById(R.id.searchByUserIdSpinner);
        expenseRecyclerList = view.findViewById(R.id.expenseRecyclerList);
        closeFormIV = view.findViewById(R.id.closeFormIV);
        userSpinner = view.findViewById(R.id.userSpinner);
        amountET = view.findViewById(R.id.amountET);
        purposeET = view.findViewById(R.id.purposeET);
        descriptionET = view.findViewById(R.id.descriptionET);
        receiptImageView = view.findViewById(R.id.receiptImageView);
        receiptImageET = view.findViewById(R.id.receiptImageET);
        dateET = view.findViewById(R.id.dateET);
        saveExpensesBtn = view.findViewById(R.id.saveExpensesBtn);
        refreshListIV = view.findViewById(R.id.refreshListIV);

    }

    private void methodListener() {

        refreshListIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchByDateET.setText("");
                searchByDateET.clearFocus();
                searchByUserIdSpinner.setSelection(0);
                setAdapterToRecyclerView();
            }
        });

        closeFormIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionLayout.setVisibility(View.GONE);
                amountET.setText("");
                purposeET.setText("");
                descriptionET.setText("");
                receiptImageET.setText("");
                dateET.setText("");
            }
        });

        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickType = "add";
                actionLayout.setVisibility(View.VISIBLE);
                userSpinner.setSelection(0);
                amountET.setText("");
                purposeET.setText("");
                descriptionET.setText("");
                receiptImageET.setText("");
                receiptImageView.setVisibility(View.GONE);
                dateET.setText("");
            }
        });

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d("123455", "position  "+position);

                if (userNameList.get(position).equals("Select User")){

                }else {
                    userId = userIdList.get(position);
                    Log.d("123455", userId);
                    searchByDate("user", userId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {


                if (userNameList.get(position).equals("Select User")){

                }else {
                    userId = userIdList.get(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        searchByUserIdSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                Log.d("123455", "position  "+position);

                if (userNameList.get(position).equals("Select User")){

                }else {
                    userId = userIdList.get(position);
                    Log.d("123455", userId);
                    searchByDate("user", userId);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        saveExpensesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = userSpinner.getSelectedItem().toString();
                String amount = amountET.getText().toString();
                String purpose = purposeET.getText().toString();
                String description = descriptionET.getText().toString();
                String receiptImageName = receiptImageET.getText().toString();
                String date = dateET.getText().toString();

                if (user.equals("") ||amount.equals("") || purpose.equals("") || description.equals("")){
                    Snackbar.make(view, "All Fields Required !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (receiptImageName.equals("") ){
                    Snackbar.make(view, "Upload Receipt Image !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (date.equals("")){
                    Snackbar.make(view, "Choose Date !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (!comapareDate(date)){
                    Snackbar.make(view, "Please select date before today !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else if (user.equals("Select User")){
                    Snackbar.make(view, "Select User !", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }else {

                    Map<String, String> map = new HashMap<>();
                    map.put("user_id", userId);
                    map.put("amount", amount);
                    map.put("purpose", purpose);
                    map.put("description", description);
                    map.put("receipt_image", receiptImageName);
                    map.put("date", date);

                    if (clickType.equals("add")) {

//                    addExpense(map);
                        uploadExpensesReceipt(file, map);
                    }else if (clickType.equals("edit")){

                        if (selectImg){
                            editUploadExpensesReceipt(file, map);
                        }else {
                            editExpense(map);
                        }

                    }

                }
            }
        });

        receiptImageET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImg = true;
                if (ActivityCompat.checkSelfPermission(activity,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Select Image !"), CODE_GALLERY_REQUEST );

                }
            }
        });

        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new HelperClass(activity).getDate(dateET);
            }
        });

        searchByDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {

                        String mm;
                        String dd;
                        if (month+1 < 10) mm = "0"+(month+1);
                        else mm = ""+month+1;

                        if (day < 10) dd = "0"+day;
                        else dd = ""+day;

                        String date = year+"-"+mm+"-"+dd;
                        searchByDateET.setText(date);
                        searchByDate("date",date);
                    }
                }, year, month, day);

                datePickerDialog.show();
            }
        });
    }

    private Boolean comapareDate(String selectedDate){

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Date d2 = null;
        Log.d("12345", selectedDate);
        try {
//            d2 = formatter.parse("2020-01-22");
            d2 = formatter.parse(selectedDate);

            Log.d("12345", "selectedDate   "+d2);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if(date.compareTo(d2) >= 0) {
//            System.out.println("Date 1 occurs after Date 2");
            return true;
        } else if(date.compareTo(d2) < 0) {
//            System.out.println("Date 1 occurs before Date 2");
            return false;
        } else {
//            System.out.println("Both dates are equal");
            return false;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Select Image!"), CODE_GALLERY_REQUEST);

            }else {
                Toast.makeText(activity, "You Don't have permission! ", Toast.LENGTH_SHORT).show();
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


            String path = getRealPathFromUri(activity, filePath);
            String imageFileName = path.substring(path.lastIndexOf("/")+1);
            receiptImageET.setText(imageFileName);
            try {
                InputStream inputStream = activity.getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                file = new File(getRealPathFromUri(activity, filePath));
                receiptImageView.setVisibility(View.VISIBLE);
                receiptImageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            Snackbar.make(getView(), "Select Another Image", Snackbar.LENGTH_SHORT)
                    .setAction("Action", null).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
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

    private void setAdapterToRecyclerView() {

        layoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        expenseRecyclerList.setLayoutManager(layoutManager);
        expenseRecyclerList.setItemAnimator(new DefaultItemAnimator());

        if (viewModel != null){
            if (viewModel.itemDataSource.getValue() != null)
                viewModel.itemDataSource.getValue().invalidate();
        }
//            viewModel = ViewModelProviders.of(getActivity()).get(ExpenseViewModel.class);
        viewModel = new ExpenseViewModel();

        expenseListAdapter = new ExpenseListAdapter(activity);
        expenseListAdapter.deleteItemData = this;
        expenseListAdapter.editItemData = this;

        viewModel.itemDataList.observe(getActivity(), new Observer<PagedList<ExpenseListPojo.ExpenseListItem>>() {
            @Override
            public void onChanged(PagedList<ExpenseListPojo.ExpenseListItem> expenseListItems) {
                expenseItemList = expenseListItems;
                expenseListAdapter.submitList(expenseListItems);
                progressBar.setVisibility(View.GONE);

            }
        });

        expenseRecyclerList.setAdapter(expenseListAdapter);

//        if (expenseListAdapter.getItemCount() == 0)
//            noResultFoundTV.setVisibility(View.VISIBLE);
//        else
//            noResultFoundTV.setVisibility(View.GONE);




    }
    
    private void addExpense(final Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_EXPENSE_URL,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();

                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");

                            Log.d("12345", "error  "+message);
                            actionLayout.setVisibility(View.GONE);

                            if (message.equals("User id, amount, purpose, description, receipt image and date cannot be empty.")){
                                Snackbar.make(getView(), "User id, amount, purpose, description, receipt image and date cannot be empty. ", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else if (message.equals("Technical Error.")){
                                Snackbar.make(getView(), "Technical Error. ", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else if (message.equals("Expense Added Successfully.")){
                                Snackbar.make(getView(), "Expense Added Successfully.", Snackbar.LENGTH_SHORT)
                                         .setAction("Action", null).show();

                                if (viewModel != null){
                                    if (viewModel.itemDataSource.getValue() != null)
                                    viewModel.itemDataSource.getValue().invalidate();
                                    setAdapterToRecyclerView();
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
                        dialog.cancel();
                        Snackbar.make(getView(), "Error in network, Please Try again! ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Log.d("12345", "error  " + error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(request);

    }

    private void editExpense(final Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(activity);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.EDIT_EXPENSE_URL+currentExpenseId,
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();

                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");

                            Log.d("12345", "error  "+message);
                            actionLayout.setVisibility(View.GONE);

                            if (message.equals("User id, amount, purpose, description, receipt image and date cannot be empty.")){
                                Snackbar.make(getView(), "User id, amount, purpose, description, receipt image and date cannot be empty. ", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else if (message.equals("Technical Error.")){
                                Snackbar.make(getView(), "Technical Error. ", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            } else if (message.equals("Expense Added Successfully.")){
                                Snackbar.make(getView(), "Expense Added Successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                if (viewModel != null){
                                    if (viewModel.itemDataSource.getValue() != null)
                                        viewModel.itemDataSource.getValue().invalidate();

                                    setAdapterToRecyclerView();
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
                        dialog.cancel();
                        Snackbar.make(getView(), "Error in network, Please Try again! ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                        Log.d("12345", "error  " + error.toString());

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(request);

    }

    private void uploadExpensesReceipt(File imageFile, final Map<String, String> map) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", imageFile.getName(), fileReqBody);

        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<UploadExpensesImagePojo> call = apiInterface.uploadExpensesImage(part/*, description*/);
        call.enqueue(new Callback<UploadExpensesImagePojo>() {

            @Override
            public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {

                dialog.cancel();
                Log.d("12345", "message  "+response.body().getMessage());
                if (response.body().getMessage().equals("Receipt Image Uploaded Successfully.")){
                    Snackbar.make(getView(), "Receipt Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    addExpense(map);

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

    private void editUploadExpensesReceipt(File imageFile, final Map<String, String> map) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("receipt_image", imageFile.getName(), fileReqBody);

        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        Call<UploadExpensesImagePojo> call = apiInterface.uploadExpensesEditedImage(currentExpenseId ,part/*, description*/);
        call.enqueue(new Callback<UploadExpensesImagePojo>() {

            @Override
            public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {

                dialog.cancel();
                Log.d("12345", "message  "+response.body().getMessage());
                if (response.body().getMessage().equals("Receipt Image Uploaded Successfully.")){
                    Snackbar.make(getView(), "Receipt Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    editExpense(map);

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

    private void loadUserData(){
        String URL = URL_Helper.USER_LIST_URL;
        userNameList = new ArrayList<>();
        userNameList.add("Select User");
        userIdList = new ArrayList<>();
        userIdList.add("");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);
                                userIdList.add(object.getString("id"));
                                userNameList.add(object.getString("name"));
                            }

                            spinnerAdapter = new ArrayAdapter<>(activity, R.layout.simple_spinner_item, userNameList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            userSpinner.setAdapter(spinnerAdapter);
                            searchByUserIdSpinner.setAdapter(spinnerAdapter);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("12345", e.toString());
                            Snackbar.make(getView(), "", Snackbar.LENGTH_SHORT)
                                    .setAction("Action", null).show();
                        }

                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(activity);
        queue.add(request);
    }

    @Override
    public void editExpenseData(int position) {

        ExpenseListPojo.ExpenseListItem item = expenseItemList.get(position);

        if (item != null) {
            clickType = "edit";
            String URL = "http://reports-mindsmetricks.com/MB/web/expenses/api/view?id=";
            final ProgressDialog dialog = new ProgressDialog(activity);
            dialog.setMessage("Loading...");
            dialog.show();

            currentExpenseId = item.getId();

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL_Helper.VIEW_EXPENSE_URL + item.getId(), null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.cancel();
                            try {
                                String message = response.getString("message");

                                if (message.equals("Expense id is invalid.")) {

                                } else {
                                    actionLayout.setVisibility(View.VISIBLE);
                                    JSONArray array = response.getJSONArray("data");

                                    JSONObject object = array.getJSONObject(0);

                                    amountET.setText(object.getString("amount"));
                                    purposeET.setText(object.getString("purpose"));
                                    descriptionET.setText(object.getString("description"));
                                    receiptImageET.setText(object.getString("receipt_image").substring(object.getString("receipt_image").lastIndexOf("/") + 1));
                                    dateET.setText(object.getString("date"));

                                    receiptImageView.setVisibility(View.VISIBLE);
                                    Picasso.get()
                                            .load(object.getString("receipt_image"))
                                            .placeholder(R.drawable.app_logo)
                                            .into(receiptImageView);

                                    int p = spinnerAdapter.getPosition(object.getString("user"));

                                    userSpinner.setSelection(p);

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.cancel();


                        }
                    });
            RequestQueue queue = Volley.newRequestQueue(activity);
            queue.add(request);

        }


    }

    @Override
    public void deleteExpenseData(int position) {

    }


    private void searchByDate(String type, String date){

        if (getActivity() != null) {

            if (viewModel != null) {
                if (viewModel.itemDataSource.getValue() != null)
                    viewModel.itemDataSource.getValue().invalidate();
            }

            viewModel = new ExpenseViewModel(type, date);

            viewModel.itemDataList.observe(getActivity(), new Observer<PagedList<ExpenseListPojo.ExpenseListItem>>() {
                @Override
                public void onChanged(PagedList<ExpenseListPojo.ExpenseListItem> expenseListItems) {
                    expenseItemList = expenseListItems;
                    expenseListAdapter.submitList(expenseListItems);


                }
            });

            expenseRecyclerList.setAdapter(expenseListAdapter);

        }

//        if (expenseListAdapter.getItemCount() == 0)
//            noResultFoundTV.setVisibility(View.VISIBLE);
//        else
//            noResultFoundTV.setVisibility(View.GONE);

    }
}
