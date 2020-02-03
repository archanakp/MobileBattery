package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.CashInHandDSFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.CashInHandPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class CashInHandViewModel extends ViewModel {

    public LiveData<PagedList<CashInHandPojo.CashInHandItem>> liveItemList;
    public LiveData<PageKeyedDataSource<Integer, CashInHandPojo.CashInHandItem>> liveDataSource;


    public CashInHandViewModel() {

        CashInHandDSFactory factory = new CashInHandDSFactory();
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        liveItemList = (new LivePagedListBuilder(factory, config)).build();
    }

    public CashInHandViewModel(String type, int value) {

        CashInHandDSFactory factory = new CashInHandDSFactory(type, value);
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        liveItemList = (new LivePagedListBuilder(factory, config)).build();
    }
}
