package android.app.mobilebattery.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.UserListAdapter;
import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.ImgeUploadPojo;
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

import de.hdodenhof.circleimageview.CircleImageView;
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

public class ManageUser extends Fragment implements  android.app.mobilebattery.Adapter.UserListAdapter.EditDetails,
        android.app.mobilebattery.Adapter.UserListAdapter.DeleteDetails{

    private EditText firstNameEditText, lastNameEditText, userNameEditText, emailIdEditText, phoneNoEditText, passwordEditText, conPasswordEditText;
    private Spinner userTypeSpinner, filterUserTypeSpinner, filterByNameSpinner, filterByEmailSpinner;
    private ListView userList;
    private Button addNewUserBtn, registerUser;
    private EditText searchByNameET, searchByEmailET;
    private ImageButton resetFilter;
    private ImageView closeNewUserFormBtn;
    private TextView noUserFoundTV, profileImageSelectedName, cPassTV, passTV, selectProfileImage;
    private ScrollView newUserForm;
    private RelativeLayout relativeLayout;
    private CircleImageView userImage;

    private Boolean value = false;
    private Bitmap bitmap;
    private int CODE_GALLERY_REQUEST = 999;
    private String formType = "New";
    private static String currentUserId = null;
    private String listType = "default";
    private String userType = "Technician";
    private UserListAdapter userAdapter;
    private ArrayList<UserListPojo> arrayList ;
    private ArrayList<String> user;
    private ArrayList<String> workers;
    private ArrayAdapter<String> userTypeSpinnerAdapter;

    private File file;
    private String editImageUploadURL;
    private ImgeUploadPojo imgeUploadPojo;
    private Boolean selectImg = false;

    public ManageUser(){/* Required empty public constructor*/}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.manage_user_fragment, container, false);

        init(view);
        methodListener();
        loadUserList();
        searchByName();
        searchByEmail();
        setUserTypeSpinnerData();

        setupUI(relativeLayout);

        return view;
    }
    private void init(View view) {

        userImage = view.findViewById(R.id.userImage);
        selectProfileImage = view.findViewById(R.id.selectProfileImage);
        profileImageSelectedName = view.findViewById(R.id.profileImageSelectedName);

        cPassTV = view.findViewById(R.id.cPassTV);
        passTV = view.findViewById(R.id.passTV);
        relativeLayout = view.findViewById(R.id.relativeLayout);
        noUserFoundTV = view.findViewById(R.id.noUserFoundTV);
        searchByNameET = view.findViewById(R.id.searchByNameET);
        searchByEmailET = view.findViewById(R.id.searchByEmailET);
        firstNameEditText = view.findViewById(R.id.firstNameEditText);
        lastNameEditText = view.findViewById(R.id.lastNameEditText);
        userNameEditText = view.findViewById(R.id.userNameEditText);
        emailIdEditText = view.findViewById(R.id.emailIdEditText);
        phoneNoEditText = view.findViewById(R.id.phoneNoEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        conPasswordEditText = view.findViewById(R.id.conPasswordEditText);

        filterByEmailSpinner = view.findViewById(R.id.filterByEmailSpinner);
        filterByNameSpinner = view.findViewById(R.id.filterByNameSpinner);
        filterUserTypeSpinner = view.findViewById(R.id.filterUserTypeSpinner);
        userTypeSpinner = view.findViewById(R.id.userTypeSpinner);
        userList = view.findViewById(R.id.userList);

        resetFilter = view.findViewById(R.id.resetFilter);
        registerUser = view.findViewById(R.id.registerUser);
        addNewUserBtn = view.findViewById(R.id.addNewUserBtn);
        closeNewUserFormBtn = view.findViewById(R.id.closeNewUserFormBtn);

        newUserForm = view.findViewById(R.id.newUserForm);

    }

    private void methodListener() {

        selectProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImg = true;
                if (ActivityCompat.checkSelfPermission(getActivity(),
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

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatchEnteredData();
            }
        });

        addNewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUserForm.setVisibility(View.VISIBLE);
                userList.setVisibility(View.GONE);

                profileImageSelectedName.setText("");
                firstNameEditText.setText("");
                lastNameEditText.setText("");
                userNameEditText.setText("");
                emailIdEditText.setText("");
                phoneNoEditText.setText("");
                userTypeSpinner.setSelection(0);

                userImage.setVisibility(View.GONE);
                passwordEditText.setVisibility(View.VISIBLE);
                conPasswordEditText.setVisibility(View.VISIBLE);
                cPassTV.setVisibility(View.VISIBLE);
                passTV.setVisibility(View.VISIBLE);

            }
        });
        closeNewUserFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newUserForm.setVisibility(View.GONE);
                userList.setVisibility(View.VISIBLE);
            }
        });

        filterUserTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String name = workers.get(position);
                if (name.equals("User Type")){
//                    Toast.makeText(getActivity(), "Please select user type", Toast.LENGTH_SHORT).show();
                }else {
                    userType = name;
                    Log.d("12345", userType);
                    listType = "selected";
                    loadUserList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        resetFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showList(arrayList);
                searchByNameET.setText("");
                searchByNameET.clearFocus();
                searchByEmailET.setText("");
                searchByEmailET.clearFocus();
                filterUserTypeSpinner.setSelection(0);
                noUserFoundTV.setVisibility(View.GONE);
                userList.setVisibility(View.VISIBLE);
                listType = "default";
                loadUserList();
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
            profileImageSelectedName.setText(imageFileName);
            try {
                InputStream inputStream = getActivity().getContentResolver().openInputStream(filePath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                userImage.setImageBitmap(bitmap);
                userImage.setVisibility(View.VISIBLE);

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

    private Boolean uploadImageToServer(final Bitmap bitmap, String URL ) {

        Log.d("12345", "at entry");
        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("12345", "in success response ");
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            Log.d("12345", "response  "+response);
                            Log.d("12345", "message  "+message);
                            if (message.equals("Profile image can not be empty.")){

                                Snackbar.make(getView(), "Profile image can not be empty.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                value = false;
                            }else if (message.equals("Image Upload Failed.")){

                                Snackbar.make(getView(), "Image Upload Failed.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                value = false;
                            }else if (message.equals("Profile Image Uploaded Successfully.")){
                                Snackbar.make(getView(), "Profile Image Uploaded Successfully.", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                value =true;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("12345", "in Error response ");

                        value =false;
                        Snackbar.make(getView(), "Error In Image Upload Try Again.", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String imgData = getStringImage(bitmap);
                Log.d("12345", "image bitmap    "+bitmap);
                Log.d("12345", "image String    "+imgData);

                Map<String, String> map = new HashMap<>();
                map.put("profile_image", bitmap.toString());

                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

        return false;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void fatchEnteredData() {
        final String enteredFNane = firstNameEditText.getText().toString();
        final String enteredLName = lastNameEditText.getText().toString();
        final String enteredUserName = userNameEditText.getText().toString();
        final String enteredEmail = emailIdEditText.getText().toString();
        final String enteredPhoneNo= phoneNoEditText.getText().toString();
        final String enteredPass = passwordEditText.getText().toString();
        final String enteredConPass = conPasswordEditText.getText().toString();
        final String enteredUserType= userTypeSpinner.getSelectedItem().toString();
        final String profileImageName = profileImageSelectedName.getText().toString();

        if (formType.equals("New")) {
            if (enteredFNane.equals("") || enteredLName.equals("") || enteredUserName.equals("") ||
                    enteredEmail.equals("") || enteredPhoneNo.equals("") || enteredPass.equals("") ||
                    enteredConPass.equals("") || enteredUserType.equals("")) {
                Snackbar.make(getView(), "please fill all the fields!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else if (!enteredEmail.matches(URL_Helper.emailPattern)) {
                emailIdEditText.setError("Invalid Email");
                Snackbar.make(getView(), "Invalid Email!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else if (enteredPhoneNo.length() < 9) {
                phoneNoEditText.setError("Invalid phone no.");
                Snackbar.make(getView(), "Invalid phone no.!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else if (enteredUserType.equals(" User Type")) {

                Snackbar.make(getView(), "Select User Type!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else if (!enteredPass.equals(enteredConPass)) {
                conPasswordEditText.setError("Password Mismatch!");
                Snackbar.make(getView(), "Confirm Password Mismatch!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else if (profileImageName.equals("")) {
                Snackbar.make(getView(), "Please Choose a profile Image!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else {

                    String url = URL_Helper.ADD_USER_URL;
                    Map<String, String> map = new HashMap<>();
                    map.put("email", enteredEmail);
                    map.put("password", enteredConPass);
                    map.put("usertype", enteredUserType);
                    map.put("username", enteredUserName);
                    map.put("firstname", enteredFNane);
                    map.put("lastname", enteredLName);
                    map.put("profile_image", profileImageName);
                    map.put("phone", enteredPhoneNo);

                    Log.d("12345", "click on save");
                uploadProfileImgToServer(file,map, url);

//                    submitDataToServer(map, url);


            }
        }else if (formType.equals("Edit")){

            if (enteredFNane.equals("") || enteredLName.equals("") || enteredUserName.equals("") ||
                    enteredEmail.equals("") || enteredPhoneNo.equals("") || enteredUserType.equals("")) {
                Toast.makeText(getContext(), "please fill all the fields", Toast.LENGTH_SHORT).show();
            } else if (!enteredEmail.matches(URL_Helper.emailPattern)) {
                emailIdEditText.setError("Invalid Email");
                Toast.makeText(getActivity(), "invalid email Id.", Toast.LENGTH_SHORT).show();
            } else if (enteredPhoneNo.length() < 9) {
                phoneNoEditText.setError("Invalid phone no.");
                Toast.makeText(getActivity(), "invalid phone no.", Toast.LENGTH_SHORT).show();
            } else if (profileImageName.equals("")) {
                Snackbar.make(getView(), "Please Choose a profile Image!", Snackbar.LENGTH_SHORT )
                        .setAction("Action", null).show();
            } else if (enteredUserType.equals(" User Type")) {
                Toast.makeText(getActivity(), "Please select user type.", Toast.LENGTH_SHORT).show();
            } else {


                String url = URL_Helper.EDIT_USER_URL;
                Map<String, String> map = new HashMap<>();
                map.put("user_id", currentUserId);
                map.put("email", enteredEmail);
                map.put("usertype", enteredUserType);
                map.put("username", enteredUserName);
                map.put("firstname", enteredFNane);
                map.put("lastname", enteredLName);
                map.put("profile_image", enteredLName);
                map.put("phone", enteredPhoneNo);

                if (selectImg){
                    editUploadProfileImgToServer(file, map, url);
                }else {
                    submitDataToServer(map, url);
                }




            }

        }
    }

    private void submitDataToServer(final Map<String, String> map, String URL) {

        Log.d("12345","map "+ map);
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            if (message.equals("User Added Successfully.")){
                                Toast.makeText(getActivity(), "User Register Successfully", Toast.LENGTH_SHORT).show();
                                loadUserList();
                                newUserForm.setVisibility(View.GONE);
                                userList.setVisibility(View.VISIBLE);
                                firstNameEditText.setText("");
                                lastNameEditText.setText("");
                                userNameEditText.setText("");
                                emailIdEditText.setText("");
                                phoneNoEditText.setText("");
                                passwordEditText.setText("");
                                conPasswordEditText.setText("");

                            }else if (message.equals("User Details Updated Successfully.")){
                                Toast.makeText(getActivity(), "User Details Updated Successfully.", Toast.LENGTH_SHORT).show();
                                loadUserList();
                                newUserForm.setVisibility(View.GONE);
                                userList.setVisibility(View.VISIBLE);
                                firstNameEditText.setText("");
                                lastNameEditText.setText("");
                                userNameEditText.setText("");
                                emailIdEditText.setText("");
                                phoneNoEditText.setText("");
                                passwordEditText.setText("");
                                conPasswordEditText.setText("");

                            }else if (message.equals("Username already exists.")){
                                Toast.makeText(getActivity(), "Username already exists.", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("Email already exists.")){
                                Toast.makeText(getActivity(), "Email already exists.", Toast.LENGTH_SHORT).show();
                            }else if (message.equals("User type is invalid.")){
                                Toast.makeText(getActivity(), "User type is invalid.", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "already exists.", Toast.LENGTH_SHORT).show();
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

    private void loadUserList() {
        arrayList = new ArrayList<>();
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        String URL = null;
        if (listType.equals("default")){
            URL = URL_Helper.USER_LIST_URL;
        }else if (listType.equals("selected")){
            String url_data = "?type="+userType;
            URL = URL_Helper.USER_LIST_URL+url_data;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");
                            Log.d("12345", "array "+array.toString());
                            Log.d("12345", "array length "+array.length());
                            for (int i=0; i<array.length(); i++ ){
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

    private void showList(ArrayList<UserListPojo> arrayList) {

        if (getActivity()!=null) {

            userAdapter =new UserListAdapter(getActivity(), R.layout.user_list_item, arrayList);
            userAdapter.editDetails = this;
            userAdapter.deleteDetails = this;
            userList.setAdapter(userAdapter);
        }

    }

    @Override
    public void editUserDetails(int position) {
        UserListPojo pojo = arrayList.get(position);

        String editImageFileName = pojo.getProfile_image().substring(pojo.getProfile_image().lastIndexOf('/') + 1);

        profileImageSelectedName.setText(editImageFileName);
        firstNameEditText.setText(pojo.getFirstname());
        lastNameEditText.setText(pojo.getLastname());
        userNameEditText.setText(pojo.getUsername());
        emailIdEditText.setText(pojo.getEmail());
        phoneNoEditText.setText(pojo.getPhone());
        String userType = pojo.getUsertype();
        Log.d("12345", userType);
        if (!userType.equals("")){
            int spinnerPosition = userTypeSpinnerAdapter.getPosition(userType);
            userTypeSpinner.setSelection(spinnerPosition+1);
        }
        userImage.setVisibility(View.VISIBLE);
        passwordEditText.setVisibility(View.GONE);
        conPasswordEditText.setVisibility(View.GONE);
        cPassTV.setVisibility(View.GONE);
        passTV.setVisibility(View.GONE);
        currentUserId = pojo.getId();
        formType = "Edit";
        editImageUploadURL = "users/api/upload-profile-image?id="+currentUserId;

        Picasso.get()
                .load(pojo.getProfile_image())
                .placeholder(R.drawable.person)
                .into(userImage);

        newUserForm.setVisibility(View.VISIBLE);
        userList.setVisibility(View.GONE);

    }

    @Override
    public void deleteUserDetails(int position) {
    }


    private void setUserTypeSpinnerData(){
        user = new ArrayList<>();
        user.add("User Type");
        user.add("Customer");
        user.add("Technician");
        user.add("Warehouse Admin");
        user.add("Warehouse Agent");
        user.add("Customer Care Admin");
        user.add("Customer Care Agent");

        userTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.simple_spinner_item, user);
        userTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeSpinnerAdapter);

        workers = new ArrayList<>();
        workers.add("User Type");
        workers.add("Technician");
        workers.add("Warehouse Admin");
        workers.add("Warehouse Agent");
        workers.add("Customer Care Admin");
        workers.add("Customer Care Agent");

        userTypeSpinnerAdapter = new ArrayAdapter<>(getActivity(),
                R.layout.simple_spinner_item, workers);
        userTypeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterUserTypeSpinner.setAdapter(userTypeSpinnerAdapter);

    }

    private void searchByName(){
        searchByNameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final ArrayList<UserListPojo> user = new ArrayList<>();
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                ManageUser.this.listAdapter.getFilter().filter(s);
                for (int i=0; i<arrayList.size(); i++){
                    UserListPojo listPojo= arrayList.get(i);
                    if (listPojo.getName().toLowerCase().contains(s.toString().toLowerCase())){
                        UserListPojo pojo = new UserListPojo();
                        pojo.setId(listPojo.getId());
                        pojo.setUsername(listPojo.getUsername());
                        pojo.setEmail(listPojo.getEmail());
                        pojo.setPhone(listPojo.getPhone());
                        pojo.setFirstname(listPojo.getFirstname());
                        pojo.setLastname(listPojo.getLastname());
                        pojo.setProfile_image(listPojo.getProfile_image());
                        pojo.setUsertype(listPojo.getUsertype());
                        pojo.setCreated_at(listPojo.getCreated_at());
                        pojo.setUpdated_at(listPojo.getUpdated_at());
                        pojo.setName(listPojo.getName());
                        user.add(pojo);

                    }
                }
                if (user.size()<1){
                    noUserFoundTV.setVisibility(View.VISIBLE);
                    userList.setVisibility(View.GONE);
                }else {
                    noUserFoundTV.setVisibility(View.GONE);
                    userList.setVisibility(View.VISIBLE);
                    showList(user);}
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
                final ArrayList<UserListPojo> user = new ArrayList<>();
//                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
//                ManageUser.this.listAdapter.getFilter().filter(s);
                for (int i=0; i<arrayList.size(); i++){
                    UserListPojo listPojo= arrayList.get(i);
                    if (listPojo.getEmail().toLowerCase().contains(s.toString().toLowerCase())){
                        UserListPojo pojo = new UserListPojo();
                        pojo.setId(listPojo.getId());
                        pojo.setUsername(listPojo.getUsername());
                        pojo.setEmail(listPojo.getEmail());
                        pojo.setPhone(listPojo.getPhone());
                        pojo.setFirstname(listPojo.getFirstname());
                        pojo.setLastname(listPojo.getLastname());
                        pojo.setProfile_image(listPojo.getProfile_image());
                        pojo.setUsertype(listPojo.getUsertype());
                        pojo.setCreated_at(listPojo.getCreated_at());
                        pojo.setUpdated_at(listPojo.getUpdated_at());
                        pojo.setName(listPojo.getName());
                        user.add(pojo);

                    }
                }
                if (user.size()<1){
                    noUserFoundTV.setVisibility(View.VISIBLE);
                    userList.setVisibility(View.GONE);
                }else {
                    noUserFoundTV.setVisibility(View.GONE);
                    userList.setVisibility(View.VISIBLE);
                    showList(user);}

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupUI(View view) {

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

    private static void hideSoftKeyboard(Activity activity) {

        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }


    }

//    private interface UploadAPIs {
//        @Multipart
//        @POST("users/api/upload-profile-image")
//        Call<ImgeUploadPojo> uploadImage(@Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);
//
//
//
//        @Multipart
//        @POST
//        Call<ImgeUploadPojo> uploadEditedImage(@Url String url, @Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);
//
//    }

    private void uploadProfileImgToServer(File imageFile, final Map<String, String> map, final String url) {


//        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
//        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        //Create a file object using file path
//        File imageFile = new File(imgPath);

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), fileReqBody);

        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
//        Call<ImgeUploadPojo> call = uploadAPIs.uploadImage(part/*, description*/);
        NetworkClient.getInstance()
                .getApi()
                .uploadUserImage(part)
                .enqueue(new Callback<ImgeUploadPojo>() {

            @Override
            public void onResponse(Call<ImgeUploadPojo> call, retrofit2.Response<ImgeUploadPojo> response) {

                Log.d("12345", "retrofit2 Response  "+response.body());
                submitDataToServer(map, url);

            }


            @Override
            public void onFailure(Call<ImgeUploadPojo> call, Throwable t) {
                Log.d("12345", "retrofit2 Error  "+t.getMessage());

            }
        });


    }

    private void editUploadProfileImgToServer(File imageFile, final Map<String, String> map, final String url) {


//        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity());
//        UploadAPIs uploadAPIs = retrofit.create(UploadAPIs.class);

        //Create a file object using file path
//        File imageFile = new File(imgPath);

        // Create a request body with file and image media type
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), imageFile);

        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("profile_image", imageFile.getName(), fileReqBody);

        //Create request body with text description and text media type
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
//        Call<ImgeUploadPojo> call = uploadAPIs.uploadEditedImage(editImageUploadURL, part/*, description*/);

        NetworkClient.getInstance()
                .getApi()
                .uploadEditedUserImage(url, part)
                .enqueue(new Callback<ImgeUploadPojo>() {

            @Override
            public void onResponse(Call<ImgeUploadPojo> call, retrofit2.Response<ImgeUploadPojo> response) {

                Log.d("12345", "editUploadProfileImgToServer  retrofit2 Response  "+response.body());
                Log.d("12345", "editUploadProfileImgToServer  "+response.body().getMessage());
//                submitDataToServer(map, url);

            }


            @Override
            public void onFailure(Call<ImgeUploadPojo> call, Throwable t) {
                Log.d("12345", "retrofit2 Error  "+t.getMessage());

            }
        });


    }


}
