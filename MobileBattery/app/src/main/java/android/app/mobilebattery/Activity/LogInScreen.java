package android.app.mobilebattery.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogInScreen extends AppCompatActivity {

    EditText userNameEditText, passwordEditText, forgetEmailEditText;

    ImageView visibilityOff, visibilityOn, closeForgotPasswordFormBtn;
    ScrollView loginForm, forgotPassForm;
    RelativeLayout mainLayout;

    SharedPreferences appPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_screen);

        init();

//            InstanceID instanceID = InstanceID.getInstance(this);
//
//            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
//                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
//
//            Log.i(TAG, "GCM Registration Token: " + token);


        getSignature(LogInScreen.this);

        setupUI(mainLayout);
    }

    private void init() {
        mainLayout = findViewById(R.id.mainLayout);
        forgotPassForm = findViewById(R.id.forgotPassForm);
        loginForm = findViewById(R.id.loginForm);
        closeForgotPasswordFormBtn = findViewById(R.id.closeForgotPasswordFormBtn);
        visibilityOn = findViewById(R.id.visibilityOn);
        visibilityOff = findViewById(R.id.visibilityOff);
        userNameEditText = findViewById(R.id.userNameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        forgetEmailEditText = findViewById(R.id.forgetEmailEditText);
    }

    public void forgetPassword(View view) {
        closeForgotPasswordFormBtn.setVisibility(View.VISIBLE);
        loginForm.setVisibility(View.GONE);
        forgotPassForm.setVisibility(View.VISIBLE);
    }

    public void signInBtn(View view) {
        String enteredUserName = userNameEditText.getText().toString();
        String enteredPass = passwordEditText.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (enteredUserName.equals("") || enteredPass.equals("")) {
            Toast.makeText(LogInScreen.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (!enteredUserName.matches(emailPattern)) {
            userNameEditText.setError("Invalid email id..");
        } else {
//            startActivity(new Intent(LogInScreen.this, Adminhome.class));
            makeLoginRequest(enteredUserName, enteredPass);
        }
    }

    private void makeLoginRequest(final String enteredUserName, final String enteredPass) {

        appPreferences = getSharedPreferences("loginDetails", MODE_PRIVATE);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.USER_LOGIN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        Log.d("12345", "response "+response);
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            JSONObject array = object.getJSONObject("data");
                            if (message.equals("Logged in Successfully.")){
                                Toast.makeText(LogInScreen.this, "Login Successful", Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = appPreferences.edit();
                                editor.putString("loginId", enteredUserName);
                                editor.apply();
                                startActivity(new Intent(LogInScreen.this, Adminhome.class));
                                finish();

                            }else if (message.equals("Email or Password is invalid.")){
                                Toast.makeText(LogInScreen.this, "Email or Password is invalid.", Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(LogInScreen.this, "Invalid details", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LogInScreen.this, "error in network ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", enteredUserName);
                params.put("password", enteredPass);
                params.put("type", "Superadmin");
                params.put("device_token", "571176417871687");
                params.put("device_name", "Android");
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    public static String getSignature(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = packageInfo.signatures;
            Log.d("12345", signatures[0].toCharsString());
            Log.d("12345", android.os.Build.MODEL);

            return signatures[0].toCharsString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showPassword(View view) {
        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        visibilityOn.setVisibility(View.VISIBLE);
        visibilityOff.setVisibility(View.GONE);

    }

    public void hidePassword(View view) {
        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        visibilityOn.setVisibility(View.GONE);
        visibilityOff.setVisibility(View.VISIBLE);
    }

    public void resetPassBtn(final View view) {
        final String email = forgetEmailEditText.getText().toString();
        if (email.equals("")) {
            forgetEmailEditText.setError("Field Require..");
            Toast.makeText(this, "Please Enter Email Id", Toast.LENGTH_SHORT).show();
        } else if (!email.matches(URL_Helper.emailPattern)) {
            forgetEmailEditText.setError("Invalid EmailId..");
            Toast.makeText(this, "Please Enter Valid Email Id", Toast.LENGTH_SHORT).show();
        } else {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Reset Password Alert!");
            builder.setMessage("are You sure to reset your password? ");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.setCancelable(true);
                    sentMail(email);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.setCancelable(true);
                    closeForgotPasswordForm(view);
                }
            });
            builder.show();

        }
    }

    private void sentMail(final String email) {

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, URL_Helper.FORGOT_PASS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.cancel();
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            Log.d("123456", response);
                            if (message.equals("A mail sent to your email id.")){
                                final AlertDialog.Builder builder = new AlertDialog.Builder(LogInScreen.this);
                                builder.setTitle("Verification Mail Send");
                                builder.setMessage("We had send verification mail on your email as " + email + " !");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        builder.setCancelable(true);
                                        closeForgotPasswordFormBtn.setVisibility(View.GONE);
                                        loginForm.setVisibility(View.VISIBLE);
                                        forgotPassForm.setVisibility(View.GONE);
                                    }
                                });
                                builder.show();

                            }else if (message.equals("Email does not exist.")){
                                forgetEmailEditText.setError("Email doesn't exist");
                                Toast.makeText(LogInScreen.this, "Entered Email doesn't exist.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(LogInScreen.this, "error in network ", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);

    }

    public void closeForgotPasswordForm(View view) {

        closeForgotPasswordFormBtn.setVisibility(View.GONE);
        loginForm.setVisibility(View.VISIBLE);
        forgotPassForm.setVisibility(View.GONE);
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LogInScreen.this);
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
}
