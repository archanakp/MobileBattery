package android.app.mobilebattery.Model;

import android.app.mobilebattery.Adapter.CustomerAdapter;
import android.app.mobilebattery.DataSource.CustomerDataSourceFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.CustomerPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class CustomerViewModel extends ViewModel {

    public LiveData<PagedList<CustomerPojo.CustomerData>> itemPagedList;
    public LiveData<PageKeyedDataSource<Integer, CustomerPojo.CustomerData>> liveDataSource;

    public CustomerViewModel() {
        CustomerDataSourceFactory dataSourceFactory = new CustomerDataSourceFactory();
        liveDataSource  = dataSourceFactory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemPagedList = (new LivePagedListBuilder(dataSourceFactory, config)).build();
    }

    public CustomerViewModel(String type, String value){
        CustomerDataSourceFactory dataSourceFactory = new CustomerDataSourceFactory(type, value);
        liveDataSource  = dataSourceFactory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemPagedList = (new LivePagedListBuilder(dataSourceFactory, config)).build();
    }
}
