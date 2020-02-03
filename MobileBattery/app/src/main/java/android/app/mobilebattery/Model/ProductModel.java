package android.app.mobilebattery.Model;

import android.app.mobilebattery.DataSource.NewBatteryDSFactory;
import android.app.mobilebattery.DataSource.ProductDSFactory;
import android.app.mobilebattery.DataSource.ProductDataSource;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.Pojo.ProductPojo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

public class ProductModel extends ViewModel {

    public LiveData<PagedList<ProductPojo.ProductItem>> itemListData;
    public LiveData<PageKeyedDataSource<Integer, ProductPojo.ProductItem>> liveDataSource;

    public ProductModel() {
        ProductDSFactory factory = new ProductDSFactory();
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(factory, config)).build();

    }

    public ProductModel(String searchType, String value) {
        ProductDSFactory factory = new ProductDSFactory( searchType, value);
        liveDataSource = factory.getLiveData();

        PagedList.Config config = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(URL_Helper.LIST_PAGE_SIZE)
                .build();

        itemListData = (new LivePagedListBuilder(factory, config)).build();

    }


}
