package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.CallRecDataSourceFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.CallRecordingPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class CallRecViewModel extends ViewModel {

    public LiveData<PagedList<CallRecordingPojo.CallRecordingItem>> itemListData;
    public LiveData<PageKeyedDataSource<Integer, CallRecordingPojo.CallRecordingItem>> liveDataSource;

    public CallRecViewModel() {

        CallRecDataSourceFactory dataFactory = new CallRecDataSourceFactory();
        liveDataSource = dataFactory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(dataFactory, config)).build();


    }
}
