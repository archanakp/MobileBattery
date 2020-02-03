package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.Pojo.ProductPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class ProductDSFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, ProductPojo.ProductItem>> liveData
            = new MutableLiveData<>();


    private String searchType, value;

    public ProductDSFactory(String searchType, String value) {
        this.searchType = searchType;
        this.value = value;
    }

    public ProductDSFactory() {
        searchType = "";
    }

    @NonNull
    @Override
    public DataSource create() {

        ProductDataSource dataSource;
        if (!searchType.equals("")){
            Log.d("12345", "searchType  "+searchType);
            dataSource = new ProductDataSource(searchType, value);
        }else {
            dataSource = new ProductDataSource();
        }

        liveData.postValue(dataSource);

        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, ProductPojo.ProductItem>> getLiveData() {
        return liveData;
    }
}
