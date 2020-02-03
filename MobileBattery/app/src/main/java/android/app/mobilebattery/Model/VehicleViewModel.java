package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.VehicleDSFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.VehiclePojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class VehicleViewModel extends ViewModel {

    public LiveData<PagedList<VehiclePojo.VehicleItem>> itemPagedList;
    public LiveData<PageKeyedDataSource<Integer, VehiclePojo.VehicleItem>> liveDataSource;

    public VehicleViewModel() {

        VehicleDSFactory dsFactory = new VehicleDSFactory();
        liveDataSource = dsFactory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemPagedList = (new LivePagedListBuilder(dsFactory, config)).build();


    }
}
