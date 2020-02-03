package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.CustomerPojo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class CustomerDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, CustomerPojo.CustomerData>> liveData =
            new MutableLiveData<>();

    private String type;
    private String value;

    public CustomerDataSourceFactory(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public CustomerDataSourceFactory() {
        type = "";
    }

    @NonNull
    @Override
    public DataSource create() {
        CustomerDataSource source;
        if (type.equals("")) {
            source = new CustomerDataSource();
            liveData.postValue(source);
            return source;
        }else {
            source = new CustomerDataSource(type, value);
            liveData.postValue(source);
            return source;
        }
    }

    public MutableLiveData<PageKeyedDataSource<Integer, CustomerPojo.CustomerData>> getLiveData() {
        return liveData;
    }
}
