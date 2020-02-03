package android.app.mobilebattery.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.mobilebattery.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class RegistrationScreen extends AppCompatActivity {

    EditText firstNameEditText, lastNameEditText, emailIdEditText, phoneNoEditText, passwordEditText, conPasswordEditText;
    Spinner userTypeSpinner;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_screen);
        
        init();
    }

    private void init() {
        userTypeSpinner = findViewById(R.id.userTypeSpinner);
        conPasswordEditText = findViewById(R.id.conPasswordEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        phoneNoEditText = findViewById(R.id.phoneNoEditText);
        emailIdEditText = findViewById(R.id.emailIdEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        firstNameEditText = findViewById(R.id.firstNameEditText);
    }

    public void goBack(View view) {
        super.onBackPressed();
    }

    public void signIn(View view) {

        startActivity(new Intent(RegistrationScreen.this, LogInScreen.class));
        finish();
    }

    public void registerBtn(View view) {

        String enteredFName = firstNameEditText.getText().toString();
        String enteredLName = lastNameEditText.getText().toString();
        String enteredEmailId = emailIdEditText.getText().toString();
        String enteredPhoneNo = phoneNoEditText.getText().toString();
        String enteredPass = passwordEditText.getText().toString();
        String enteredConPass = conPasswordEditText.getText().toString();
        String enteredUserType = userTypeSpinner.getSelectedItem().toString();

        if(enteredFName.equals("") || enteredLName.equals("") || enteredEmailId.equals("") ||
                enteredPhoneNo.equals("") ||enteredPass.equals("") ||enteredConPass.equals("") ||
                enteredUserType.equals("") ){

        }

    }
}
