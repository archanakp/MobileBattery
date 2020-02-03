package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.VehiclePojo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class VehicleDSFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, VehiclePojo.VehicleItem>> liveData =
                new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource create() {

        VehicleDataSource dataSource = new VehicleDataSource();
        liveData.postValue(dataSource);
        return dataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, VehiclePojo.VehicleItem>> getLiveData() {
        return liveData;
    }
}
