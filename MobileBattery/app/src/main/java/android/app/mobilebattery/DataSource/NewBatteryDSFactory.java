package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.NewBatteryPojo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class NewBatteryDSFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, NewBatteryPojo.NewBatteryItem>> liveData
            = new MutableLiveData<>();


    private String batteryType, searchType, value;

    public NewBatteryDSFactory(String batteryType, String searchType, String value) {
        this.batteryType = batteryType;
        this.searchType = searchType;
        this.value = value;
    }

    public NewBatteryDSFactory(String batteryType) {
        this.batteryType = batteryType;
        searchType = "";
    }

    @NonNull
    @Override
    public DataSource create() {

        NewBatteryDataSource dataSource;
        if (!searchType.equals("")){
            dataSource = new NewBatteryDataSource(batteryType, searchType, value);
        }else {
            dataSource = new NewBatteryDataSource(batteryType);
        }

        liveData.postValue(dataSource);

        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, NewBatteryPojo.NewBatteryItem>> getLiveData() {
        return liveData;
    }
}
