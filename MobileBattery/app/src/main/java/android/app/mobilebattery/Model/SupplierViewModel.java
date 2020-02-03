package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.SupplierDSFactory;
import android.app.mobilebattery.Pojo.SupplierListPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class SupplierViewModel extends ViewModel {


    public LiveData<PagedList<SupplierListPojo.SupplierItem>> liveDataList;
    public LiveData<PageKeyedDataSource<Integer, SupplierListPojo.SupplierItem>> liveDataSource;


    public SupplierViewModel() {
        SupplierDSFactory factory = new SupplierDSFactory();
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        liveDataList = (new LivePagedListBuilder(factory, config)).build();
    }

    public SupplierViewModel( String type, String value) {
        SupplierDSFactory factory = new SupplierDSFactory(type, value);
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(10)
                .build();

        liveDataList = (new LivePagedListBuilder(factory, config)).build();
    }

}
