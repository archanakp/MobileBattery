package android.app.mobilebattery.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.CustomerAdapter;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Model.CustomerViewModel;
import android.app.mobilebattery.Pojo.CustomerPojo;
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
import android.widget.ScrollView;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;

public class CustomerFragment extends Fragment implements CustomerAdapter.Action{


    private EditText cFirstNameEditText, cLastNameEditText, cUserNameEditText, cEmailIdEditText, cPhoneNoEditText, cPasswordEditText, cConPassEditText;
    private ListView customerListView;
    private Button registerCustomer, addNewCustomerBtn;
    private ImageView closeCustomerFormBtn;
    private ScrollView newCustomerForm;
    private ImageButton resetFilter;
    private TextView noUserFoundTV, conPass, pass;
    private EditText searchByNameET, searchByEmailET, searchByPhoneET, cProfileImageEditText;
    private RecyclerView customerRecyclerView;


    private CustomerViewModel viewModel;
    private CustomerAdapter adapter;
    private PagedList<CustomerPojo.CustomerData> customerList;



    private String btnClick = null;
    private String editId = "1";

    private Boolean imageClicked = false;
    private Bitmap bitmap;
    private File file;
    private int currentCustomerId = 0;

    private final int CODE_GALLERY_REQUEST = 999;

    public CustomerFragment(){/* Required empty public constructor*/}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.customer_fragment, container, false);

        init(view);
        methodListener();
        setCustomerList();
//        loadCustomerList();
        setupUI(newCustomerForm);
        searchByName();
        searchByEmail();
        searchByPhoneNo();



        return view;
    }

    private void setCustomerList() {

        if (getActivity() != null) {
            customerRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            customerRecyclerView.setHasFixedSize(true);
            if (viewModel != null){
                if (viewModel.liveDataSource.getValue() != null)
                    viewModel.liveDataSource.getValue().invalidate();
            }

            viewModel = new CustomerViewModel();
            adapter = new CustomerAdapter(getActivity());
            adapter.action = this;

            viewModel.itemPagedList.observe(getActivity(), new Observer<PagedList<CustomerPojo.CustomerData>>() {
                @Override
                public void onChanged(PagedList<CustomerPojo.CustomerData> customerData) {
                    adapter.submitList(customerData);
                    customerList = customerData;
                }
            });

            customerRecyclerView.setAdapter(adapter);

        }
    }

    private void init(View view) {

        customerRecyclerView = view.findViewById(R.id.customerRecyclerView);
        noUserFoundTV = view.findViewById(R.id.noUserFoundTV);
        resetFilter = view.findViewById(R.id.resetFilter);
        searchByNameET = view.findViewById(R.id.searchByNameET);
        searchByEmailET = view.findViewById(R.id.searchByEmailET);
        searchByPhoneET = view.findViewById(R.id.searchByPhoneET);
        conPass = view.findViewById(R.id.conPass);
        pass = view.findViewById(R.id.pass);

        cProfileImageEditText = view.findViewById(R.id.cProfileImageEditText);
        cFirstNameEditText = view.findViewById(R.id.cFirstNameEditText);
        cLastNameEditText = view.findViewById(R.id.cLastNameEditText);
        cUserNameEditText = view.findViewById(R.id.cUserNameEditText);
        cEmailIdEditText = view.findViewById(R.id.cEmailIdEditText);
        cPhoneNoEditText = view.findViewById(R.id.cPhoneNoEditText);
        cPasswordEditText = view.findViewById(R.id.cPasswordEditText);
        cConPassEditText = view.findViewById(R.id.cConPassEditText);
//        customerListView = view.findViewById(R.id.customerListView);
        registerCustomer = view.findViewById(R.id.registerCustomer);
        addNewCustomerBtn = view.findViewById(R.id.addNewCustomerBtn);
        closeCustomerFormBtn = view.findViewById(R.id.closeCustomerFormBtn);
        newCustomerForm = view.findViewById(R.id.newCustomerForm);


    }

    private void methodListener() {

        registerCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchEnteredData();
            }
        });

        cProfileImageEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imageClicked = true;
                if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST);
                }else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, "Choose Image.."), CODE_GALLERY_REQUEST);

                }
            }
        });

        addNewCustomerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCustomerForm.setVisibility(View.VISIBLE);
//                customerListView.setVisibility(View.GONE);
                btnClick = "add";
                clearAllFields();
                cPasswordEditText.setVisibility(View.VISIBLE);
                cConPassEditText.setVisibility(View.VISIBLE);
                conPass.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
            }
        });
        closeCustomerFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newCustomerForm.setVisibility(View.GONE);

//                customerListView.setVisibility(View.VISIBLE);
            }
        });

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showList(arrayList);
                searchByNameET.setText("");
                searchByNameET.clearFocus();
                searchByEmailET.setText("");
                searchByEmailET.clearFocus();
                searchByPhoneET.setText("");
                searchByPhoneET.clearFocus();
                noUserFoundTV.setVisibility(View.GONE);
//                customerListView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODE_GALLERY_REQUEST){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Image.."), CODE_GALLERY_REQUEST);
            }else {
                Toast.makeText(getActivity(), "You doesn't have to access gallery! ", Toast.LENGTH_SHORT).show();
            }
            return;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            String path = getRealPathFromUri(getActivity(), uri);
            String imageFileName = path.substring(path.lastIndexOf("/")+1);
            cProfileImageEditText.setText(imageFileName);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(uri);
                bitmap = BitmapFactory.decodeStream(inputStream);
//                userImage.setImageBitmap(bitmap);
//                userImage.setVisibility(View.VISIBLE);

                file = new File(getRealPathFromUri(getActivity(), uri));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }else {
            Toast.makeText(getActivity(), "Select Another Image", Toast.LENGTH_SHORT).show();
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

    private void fetchEnteredData() {
        final String enteredFNane = cFirstNameEditText.getText().toString();
        final String enteredLName = cLastNameEditText.getText().toString();
        final String enteredUserName = cUserNameEditText.getText().toString();
        final String enteredEmail = cEmailIdEditText.getText().toString();
        final String enteredPhoneNo= cPhoneNoEditText.getText().toString();
        final String enteredPass = cPasswordEditText.getText().toString();
        final String enteredConPass = cConPassEditText.getText().toString();
        String imageName = cProfileImageEditText.getText().toString();

        if (enteredFNane.equals("") || enteredLName.equals("") || enteredUserName.equals("") ||
                enteredEmail.equals("") || enteredPhoneNo.equals("") ){
            Toast.makeText(getContext(), "please fill all the fields", Toast.LENGTH_SHORT).show();
        }else if (!enteredEmail.matches(URL_Helper.emailPattern)){
            cEmailIdEditText.setError("Invalid Email");
            Toast.makeText(getActivity(), "invalid email Id.", Toast.LENGTH_SHORT).show();
        }else if (enteredPhoneNo.length()<9){
            cPhoneNoEditText.setError("Invalid phone no.");
            Toast.makeText(getActivity(), "invalid phone no.", Toast.LENGTH_SHORT).show();
        }else if (imageName.equals("")){
            cProfileImageEditText.setError("Choose a Image");
            Toast.makeText(getActivity(), "Choose a profile image", Toast.LENGTH_SHORT).show();
        }else {


            Map<String, String> map = new HashMap<>();
            map.put("email",enteredEmail);
            map.put("username",enteredUserName);
            map.put("firstname",enteredFNane);
            map.put("lastname",enteredLName);
            map.put("phone",enteredPhoneNo);
            map.put("profile_image",imageName);
            if (btnClick.equals("add")) {
                if (enteredPass.equals("") || enteredConPass.equals(""))
                    Toast.makeText(getActivity(), "Enter Password", Toast.LENGTH_SHORT).show();
                else if (!enteredPass.equals(enteredConPass)) {
                    cConPassEditText.setError("Password Mismatch!");
                    Toast.makeText(getActivity(), "Password mismatch.", Toast.LENGTH_SHORT).show();
                }else {
                    map.put("password", enteredConPass);

                    if (imageClicked){
                        uploadCustomerProfileImage(file, map);
                    }else {
                        submitDataToServer(map);
                    }
                }
            }else if (btnClick.equals("edit")){

                if (imageClicked){
                    uploadEditedCustomerProfileImage(file, map);
                }else {
                    submitEditedDataToServer(map);
                }

            }

//            if (imageClicked){
//                uploadCustomerProfileImage(file, map);
//            }else {
//
//                if (btnClick.equals("add")) {
//                    submitDataToServer(map);
//                } else if (btnClick.equals("edit")) {
//                    submitEditedDataToServer(map);
//                } else {
//                }
//            }


        }
    }

    private void submitDataToServer(final Map<String, String> map) {

        Log.d("12345","map "+ map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.ADD_CUSTOMER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        Log.d("12345", "response  "+response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Customer Added Successfully.")){
                                Toast.makeText(getActivity(), "Customer Added Successfully.", Toast.LENGTH_SHORT).show();
                                newCustomerForm.setVisibility(View.GONE);
                                setCustomerList();
//                                customerListView.setVisibility(View.VISIBLE);

                            }else if (message.equals("Username already exists.")){
                                Toast.makeText(getActivity(), "Username already exists.", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("Email already exists.")){
                                Toast.makeText(getActivity(), "Email already exists.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Email already exists.", Toast.LENGTH_SHORT).show();
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
                        Log.d("12345", "error " + error.toString());
                        if (error.toString().equals("com.android.volley.TimeoutError")){
//                            newCustomerForm.setVisibility(View.GONE);
//                            setCustomerList();
                            Toast.makeText(getActivity(), "com.android.volley.TimeoutError ", Toast.LENGTH_SHORT).show();

                        }
                        else
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


    private void submitEditedDataToServer(final Map<String, String> map) {

        Log.d("12345","map "+ map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String data = "?id="+editId;
        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.EDIT_CUSTOMER_URL+data,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("Customer details updated successfully.")){
                                Toast.makeText(getActivity(), "Customer details updated Successfully.", Toast.LENGTH_SHORT).show();
                                newCustomerForm.setVisibility(View.GONE);
//                                customerListView.setVisibility(View.VISIBLE);
//                                loadCustomerList();

                            }else if (message.equals("Customer name, email, phone, company or address cannot be empty.")){
                                Toast.makeText(getActivity(), "Customer name, email, phone, company or address cannot be empty.", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("Customer username, email or phone already exists.")){
                                Toast.makeText(getActivity(), "Customer username, email or phone already exists.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "Something Wrong.", Toast.LENGTH_SHORT).show();
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


    private void clearAllFields(){
        cFirstNameEditText.setText("");
        cLastNameEditText.setText("");
        cUserNameEditText.setText("");
        cEmailIdEditText.setText("");
        cPhoneNoEditText.setText("");
        cProfileImageEditText.setText("");
        cPasswordEditText.setText("");
        cConPassEditText.setText("");
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

    private void searchByName(){
        searchByNameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (viewModel != null){
                    if (viewModel.liveDataSource.getValue()!= null)
                        viewModel.liveDataSource.getValue().invalidate();
                }
                if (getActivity() != null) {
                    if (s.length() == 0)
                        viewModel = new CustomerViewModel();
                    else
                        viewModel = new CustomerViewModel("name", s.toString().toLowerCase());

                    viewModel.itemPagedList.observe(getActivity(), new Observer<PagedList<CustomerPojo.CustomerData>>() {
                        @Override
                        public void onChanged(PagedList<CustomerPojo.CustomerData> customerData) {
                            adapter.submitList(customerData);
                            customerList = customerData;
                        }
                    });
                    customerRecyclerView.setAdapter(adapter);
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
                if (viewModel != null){
                    if (viewModel.liveDataSource.getValue()!= null)
                        viewModel.liveDataSource.getValue().invalidate();
                }
                if (getActivity() != null) {
                    if (s.length() == 0)
                        viewModel = new CustomerViewModel();
                    else
                        viewModel = new CustomerViewModel("email", s.toString().toLowerCase());

                    viewModel.itemPagedList.observe(getActivity(), new Observer<PagedList<CustomerPojo.CustomerData>>() {
                        @Override
                        public void onChanged(PagedList<CustomerPojo.CustomerData> customerData) {
                            adapter.submitList(customerData);
                            customerList = customerData;
                        }
                    });
                    customerRecyclerView.setAdapter(adapter);
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

                if (viewModel != null){
                    if (viewModel.liveDataSource.getValue()!= null)
                        viewModel.liveDataSource.getValue().invalidate();
                }

                if (getActivity() != null) {
                    if (s.length() == 0)
                        viewModel = new CustomerViewModel();
                    else
                        viewModel = new CustomerViewModel("phone", s.toString().toLowerCase());

                    viewModel.itemPagedList.observe(getActivity(), new Observer<PagedList<CustomerPojo.CustomerData>>() {
                        @Override
                        public void onChanged(PagedList<CustomerPojo.CustomerData> customerData) {
                            adapter.submitList(customerData);
                            customerList = customerData;
                        }
                    });
                    customerRecyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void uploadCustomerProfileImage(File imageFile, final Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), fileReqBody);

        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        NetworkClient.getInstance()
                .getApi()
                .uploadCustomerProfileImage(part)
                .enqueue(new Callback<UploadExpensesImagePojo>() {

                    @Override
                    public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {
                        dialog.cancel();
                        if (response.body() != null){

                            Log.d("12345", "message  "+response.body().getMessage());
                            if (response.body().getMessage().equals("Profile Image Uploaded Successfully.")){
                                Snackbar.make(getView(), "Profile Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                if (btnClick.equals("add")) {
                                    submitDataToServer(map);
                                } else if (btnClick.equals("edit")) {
                                    submitEditedDataToServer(map);
                                }

                            }else if (response.body().getMessage().equals("Image Upload Failed.")){
                                Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else if (response.body().getMessage().equals("Image Could not saved.")){
                                Snackbar.make(getView(), "Image Could not saved.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else if (response.body().getMessage().equals("Profile image can not be empty.")){
                                Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                Log.d("12345", "Profile image can not be empty.");
                            }
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

    private void uploadEditedCustomerProfileImage(File imageFile, final Map<String, String> map){

        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setMessage("Loading...");
        dialog.show();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), fileReqBody);

        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
        NetworkClient.getInstance()
                .getApi()
                .uploadEditCustomerProfileImage((currentCustomerId != 0) ?currentCustomerId :1 ,part)
                .enqueue(new Callback<UploadExpensesImagePojo>() {

                    @Override
                    public void onResponse(Call<UploadExpensesImagePojo> call, retrofit2.Response<UploadExpensesImagePojo> response) {
                        dialog.cancel();
                        if (response.body() != null){

                            Log.d("12345", "message  "+response.body().getMessage());
                            if (response.body().getMessage().equals("Profile Image Uploaded Successfully.")){
                                Snackbar.make(getView(), "Profile Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();

                                if (btnClick.equals("add")) {
                                    submitDataToServer(map);
                                } else if (btnClick.equals("edit")) {
                                    submitEditedDataToServer(map);
                                }

                            }else if (response.body().getMessage().equals("Image Upload Failed.")){
                                Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else if (response.body().getMessage().equals("Image Could not saved.")){
                                Snackbar.make(getView(), "Image Could not saved.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                            }else if (response.body().getMessage().equals("Profile image can not be empty.")){
                                Snackbar.make(getView(), "Image Upload Failed Please Try Again !", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                Log.d("12345", "Profile image can not be empty.");
                            }
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


    @Override
    public void editCustomerDetails(int position) {

        CustomerPojo.CustomerData data = customerList.get(position);

        if (data != null){
            btnClick = "edit";
            currentCustomerId = data.id;
            newCustomerForm.setVisibility(View.VISIBLE);
            cFirstNameEditText.setText(data.firstname);
            cLastNameEditText.setText(data.lastname);
            cUserNameEditText.setText(data.username);
            cEmailIdEditText.setText(data.email);
            cProfileImageEditText.setText(data.profile_image.substring(data.profile_image.lastIndexOf("/")+1));
            cPhoneNoEditText.setText(String.valueOf(data.phone));
            cPasswordEditText.setVisibility(View.GONE);
            cConPassEditText.setVisibility(View.GONE);
            conPass.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
//            fetchEnteredData();
        }
    }

    @Override
    public void deleteCustomerDetails(int position) {

    }
}
