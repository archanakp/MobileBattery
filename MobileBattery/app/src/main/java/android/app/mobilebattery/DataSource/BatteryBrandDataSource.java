package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.BatteryBrandPojo;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BatteryBrandDataSource extends PageKeyedDataSource<Integer, BatteryBrandPojo.BatteryBrandItem> {

    public static final int FIRST_PAGE = 1;

    private String type , brand;
    private int bit;

    public BatteryBrandDataSource(String type, String brand, int bit) {
        this.type = type;
        this.brand = brand;
        this.bit = bit;
    }

    public BatteryBrandDataSource() {
        type = "";
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, BatteryBrandPojo.BatteryBrandItem> callback) {
        Call<BatteryBrandPojo> call;
        if (type.equals("brand")){
            if (brand.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBrandList(FIRST_PAGE);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .getSearchByBrandList(FIRST_PAGE, brand);
            }
        }else if (type.equals("status")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .getSearchByStatusList(FIRST_PAGE, bit);
        }else {
            call = NetworkClient.getInstance()
                    .getApi()
                    .getBrandList(FIRST_PAGE);
        }
        call.enqueue(new Callback<BatteryBrandPojo>() {
                    @Override
                    public void onResponse(Call<BatteryBrandPojo> call, Response<BatteryBrandPojo> response) {
                        if (response.body() != null){

                            int t_p = response.body().getPage_count();
                            Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;
                            callback.onResult(response.body().getData(), null, key);
                        }
                    }
                    @Override
                    public void onFailure(Call<BatteryBrandPojo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, BatteryBrandPojo.BatteryBrandItem> callback) {
        Call<BatteryBrandPojo> call;
        if (type.equals("brand")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .getSearchByBrandList(params.key, brand);
        }else if (type.equals("status")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .getSearchByStatusList(params.key, bit);
        }else {
            call = NetworkClient.getInstance()
                    .getApi()
                    .getBrandList(params.key);
        }
        call.enqueue(new Callback<BatteryBrandPojo>() {
                    @Override
                    public void onResponse(Call<BatteryBrandPojo> call, Response<BatteryBrandPojo> response) {

                        if (response.body() != null){
                            Integer key = (params.key > FIRST_PAGE) ? params.key-1 : null;
                            callback.onResult(response.body().getData(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<BatteryBrandPojo> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, BatteryBrandPojo.BatteryBrandItem> callback) {

        Call<BatteryBrandPojo> call;
        if (type.equals("brand")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .getSearchByBrandList(params.key, brand);
        }else if (type.equals("status")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .getSearchByStatusList(params.key, bit);
        }else {
            call = NetworkClient.getInstance()
                    .getApi()
                    .getBrandList(params.key);
        }
        call.enqueue(new Callback<BatteryBrandPojo>() {
                    @Override
                    public void onResponse(Call<BatteryBrandPojo> call, Response<BatteryBrandPojo> response) {
                        if (response.body() != null){
                            int t_p = response.body().getPage_count();
                            Integer key = (params.key < t_p) ? params.key+1 : null;

                            callback.onResult(response.body().getData(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<BatteryBrandPojo> call, Throwable t) {

                    }
                });

    }
}
