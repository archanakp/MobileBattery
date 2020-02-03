package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.SupplierListPojo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class SupplierDSFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, SupplierListPojo.SupplierItem>> liveData=
            new MutableLiveData<>();

    private String type, value;

    public SupplierDSFactory(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public SupplierDSFactory() {
        type = "";
    }

    @NonNull
    @Override
    public DataSource create() {

        SupplierDataSource dataSource;
        if (!type.equals("")){
            dataSource = new SupplierDataSource(type, value);
        }else {
            dataSource = new SupplierDataSource();
        }

        liveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, SupplierListPojo.SupplierItem>> getLiveData() {
        return liveData;
    }
}
