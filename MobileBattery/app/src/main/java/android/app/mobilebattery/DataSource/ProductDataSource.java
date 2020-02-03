package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.Pojo.ProductPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDataSource extends PageKeyedDataSource<Integer, ProductPojo.ProductItem> {

    public static final int FIRST_PAGE = 1;

    private String searchType, value;

    public ProductDataSource( String searchType, String value) {
        this.searchType = searchType;
        this.value = value;
    }

    public ProductDataSource() {
        searchType = "";
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ProductPojo.ProductItem> callback) {

        Call<ProductPojo> call;

        if (!searchType.equals("")){

            if (value.length() == 0)
            call = NetworkClient.getInstance()
                    .getApi()
                    .getProductList( FIRST_PAGE);
            else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchProductList(FIRST_PAGE, value);
            }

        }else {

            call = NetworkClient.getInstance()
                    .getApi()
                    .getProductList(FIRST_PAGE);
        }

        call.enqueue(new Callback<ProductPojo>() {
            @Override
            public void onResponse(Call<ProductPojo> call, Response<ProductPojo> response) {
                if (response.body() != null){

                    int t_p = response.body().getPage_count();
//                    int t_p = ((int)(Math.ceil(response.body().getCount()/10)));
                    Log.d("12345", "total Page "+t_p);
                    Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;

                    callback.onResult(response.body().getData(), null, key);
                }
            }

            @Override
            public void onFailure(Call<ProductPojo> call, Throwable t) {
                if (t.getMessage() != null)
                Log.d("12345", t.getMessage());
            }
        });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ProductPojo.ProductItem> callback) {

        Call<ProductPojo> call;
        if (!searchType.equals("")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getProductList( params.key);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchProductList(params.key, value);
            }

        }else {

            call = NetworkClient.getInstance()
                    .getApi()
                    .getProductList( params.key);
        }

        call.enqueue(new Callback<ProductPojo>() {
            @Override
            public void onResponse(Call<ProductPojo> call, Response<ProductPojo> response) {
                if (response.body() != null){

                    Integer key = (params.key > FIRST_PAGE) ? params.key-1 : null;
                    callback.onResult(response.body().getData(), key);
                }
            }

            @Override
            public void onFailure(Call<ProductPojo> call, Throwable t) {
                if (t.getMessage() != null)
                    Log.d("12345", t.getMessage());
            }
        });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ProductPojo.ProductItem> callback) {

        Call<ProductPojo> call;

       if (!searchType.equals("")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchProductList( params.key, value);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .getProductList(params.key);
            }

        }else {

            call = NetworkClient.getInstance()
                    .getApi()
                    .getProductList(params.key);
        }

        call.enqueue(new Callback<ProductPojo>() {
            @Override
            public void onResponse(Call<ProductPojo> call, Response<ProductPojo> response) {
                if (response.body()!= null){
                    int t_p = response.body().getPage_count();
                    Log.d("12345", "total page  "+response.body().getPage_count());
                    Integer key = (params.key < t_p) ? params.key+1 : null;
                    callback.onResult(response.body().getData(), key);
                }
            }


            @Override
            public void onFailure(Call<ProductPojo> call, Throwable t) {
                if (t.getMessage() != null)
                    Log.d("12345", t.getMessage());
            }
        });

    }
}
