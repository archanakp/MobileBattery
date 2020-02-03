package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.ExpenseListPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class ExpenseDSFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, ExpenseListPojo.ExpenseListItem>> liveData =
            new MutableLiveData<>();

    private String type ;
    private String value;

    public ExpenseDSFactory(String type, String value) {
        this.type = type;
        this.value = value;
        Log.d("12345", "ExpenseDSFactory  ");
    }

    public ExpenseDSFactory() {
        type = "";
    }

    @NonNull
    @Override
    public DataSource create() {
        Log.d("12345", "ExpenseDSFactory create ");

        if (type.equals("date")){
            Log.d("12345", type);
            ExpenseDateSearchDataSource dataSource = new ExpenseDateSearchDataSource(value);
            liveData.postValue(dataSource);
            return dataSource;
        }else if (type.equals("user")){
            Log.d("12345", type);
            ExpenseUserSearchDataSource dataSource = new ExpenseUserSearchDataSource(value);
            liveData.postValue(dataSource);
            return dataSource;
        }else {
            ExpenseDataSource dataSource = new ExpenseDataSource();
            liveData.postValue(dataSource);
            return dataSource;
        }

//        liveData.postValue(dataSource);
//        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, ExpenseListPojo.ExpenseListItem>> getLiveData() {
        return liveData;
    }
}
