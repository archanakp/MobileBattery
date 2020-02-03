package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.BatteryBrandPojo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class BatteryBrandDSFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, BatteryBrandPojo.BatteryBrandItem>> liveData =
            new MutableLiveData<>();

    private String type , brand;
    private int bit;

    public BatteryBrandDSFactory(String type, String brand, int bit) {
        this.type = type;
        this.brand = brand;
        this.bit = bit;

    }

    public BatteryBrandDSFactory() {
        type = "";
    }

    @NonNull
    @Override
    public DataSource create() {
        BatteryBrandDataSource source;
        if (type.equals("brand")){
            source = new BatteryBrandDataSource(type, brand, bit);

        }else if (type.equals("status")){
            source = new BatteryBrandDataSource(type, brand, bit);

        }else {
            source = new BatteryBrandDataSource();
        }

        liveData.postValue(source);
        return source;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, BatteryBrandPojo.BatteryBrandItem>> getLiveData() {
        return liveData;
    }
}
