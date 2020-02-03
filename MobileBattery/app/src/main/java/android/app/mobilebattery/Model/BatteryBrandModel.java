package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.BatteryBrandDSFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.BatteryBrandPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class BatteryBrandModel extends ViewModel {

    public LiveData<PagedList<BatteryBrandPojo.BatteryBrandItem>> itemListData;
    public LiveData<PageKeyedDataSource<Integer, BatteryBrandPojo.BatteryBrandItem>> liveDataSource;

    public BatteryBrandModel() {
        BatteryBrandDSFactory factory = new BatteryBrandDSFactory();
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(factory, config)).build();

    }

    public BatteryBrandModel(String type, String brand, int bit) {
        BatteryBrandDSFactory factory = new BatteryBrandDSFactory(type, brand, bit);
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(factory, config)).build();

    }


}
