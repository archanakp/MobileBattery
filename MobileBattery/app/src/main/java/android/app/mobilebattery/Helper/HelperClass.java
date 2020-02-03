package android.app.mobilebattery.Helper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class HelperClass {

    private Activity activity;

    public HelperClass(Activity activity){
        this.activity = activity;
    }

    public void getDate (final EditText editText) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(activity, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                String mm;
                String dd;
                if ((month+1) < 10) mm = "0"+(month+1);
                else mm = ""+(month+1);

                if (dayOfMonth < 10 ) dd = "0"+dayOfMonth;
                else dd = String.valueOf(dayOfMonth);
                editText.setText(year+"-"+mm+"-"+dd);
            }
        }, year, month, day);
        datePickerDialog.show();

    }


}
