package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.ExpenseDSFactory;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.ExpenseListPojo;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.Date;

public class ExpenseViewModel extends ViewModel {

    public LiveData<PagedList<ExpenseListPojo.ExpenseListItem>> itemDataList;
    public LiveData<PageKeyedDataSource<Integer, ExpenseListPojo.ExpenseListItem>> itemDataSource;

    public ExpenseViewModel() {

        ExpenseDSFactory dsFactory = new ExpenseDSFactory();
        itemDataSource = dsFactory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemDataList = (new LivePagedListBuilder(dsFactory, config)).build();
    }

    public ExpenseViewModel(String type, String value) {

        Log.d("12345", "ExpenseViewModel  ");
        ExpenseDSFactory dsFactory = new ExpenseDSFactory(type, value);
        itemDataSource = dsFactory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemDataList = (new LivePagedListBuilder(dsFactory, config)).build();
    }
}
