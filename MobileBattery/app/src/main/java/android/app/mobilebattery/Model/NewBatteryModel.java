package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.BatteryBrandDSFactory;
import android.app.mobilebattery.DataSource.NewBatteryDSFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.BatteryBrandPojo;
import android.app.mobilebattery.Pojo.NewBatteryPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class NewBatteryModel extends ViewModel {

    public LiveData<PagedList<NewBatteryPojo.NewBatteryItem>> itemListData;
    public LiveData<PageKeyedDataSource<Integer, NewBatteryPojo.NewBatteryItem>> liveDataSource;

    public NewBatteryModel(String batteryType) {
        NewBatteryDSFactory factory = new NewBatteryDSFactory(batteryType);
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(factory, config)).build();

    }

    public NewBatteryModel(String batteryType, String searchType, String value) {
        NewBatteryDSFactory factory = new NewBatteryDSFactory(batteryType, searchType, value);
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(factory, config)).build();

    }


}
