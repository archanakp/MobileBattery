package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewBatteryDataSource extends PageKeyedDataSource<Integer, NewBatteryPojo.NewBatteryItem> {

    public static final int FIRST_PAGE = 1;

    private String batteryType, searchType, value;

    public NewBatteryDataSource(String batteryType, String searchType, String value) {
        this.batteryType = batteryType;
        this.searchType = searchType;
        this.value = value;
    }

    public NewBatteryDataSource(String batteryType) {
        this.batteryType = batteryType;
        searchType = "";
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, NewBatteryPojo.NewBatteryItem> callback) {

        Call<NewBatteryPojo> call;

        if (searchType.equals("source")){

            if (value.length() == 0)
            call = NetworkClient.getInstance()
                    .getApi()
                    .getBatteryList(batteryType, FIRST_PAGE);
            else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryBySource(batteryType, FIRST_PAGE, value);
            }

        }else if (searchType.equals("brand")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, FIRST_PAGE);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryByBrandName(batteryType, FIRST_PAGE, value);
            }

        }else if (searchType.equals("sku")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, FIRST_PAGE);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryBySKU(batteryType, FIRST_PAGE, value);
            }
        }else {

            call = NetworkClient.getInstance()
                    .getApi()
                    .getBatteryList(batteryType, FIRST_PAGE);
        }

        call.enqueue(new Callback<NewBatteryPojo>() {
            @Override
            public void onResponse(Call<NewBatteryPojo> call, Response<NewBatteryPojo> response) {
                if (response.body() != null){

                    int t_p = response.body().getPage_count();
//                    int t_p = ((int)(Math.ceil(response.body().getCount()/10)));
                    Log.d("12345", "total Page "+t_p);
                    Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;

                    callback.onResult(response.body().getData(), null, key);
                }
            }

            @Override
            public void onFailure(Call<NewBatteryPojo> call, Throwable t) {
                if (t.getMessage() != null)
                Log.d("12345", t.getMessage());
            }
        });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, NewBatteryPojo.NewBatteryItem> callback) {

        Call<NewBatteryPojo> call;

        if (searchType.equals("source")){

            if (value.length() == 0)
            call = NetworkClient.getInstance()
                    .getApi()
                    .getBatteryList(batteryType, params.key);
            else
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryBySource(batteryType, params.key, value);

        }else if (searchType.equals("brand")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, params.key);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryByBrandName(batteryType, params.key, value);
            }

        }else if (searchType.equals("sku")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, params.key);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryBySKU(batteryType, params.key, value);
            }

        }else {

            call = NetworkClient.getInstance()
                    .getApi()
                    .getBatteryList(batteryType, params.key);
        }

        call.enqueue(new Callback<NewBatteryPojo>() {
            @Override
            public void onResponse(Call<NewBatteryPojo> call, Response<NewBatteryPojo> response) {
                if (response.body() != null){

                    Integer key = (params.key > FIRST_PAGE) ? params.key-1 : null;
                    callback.onResult(response.body().getData(), key);
                }
            }

            @Override
            public void onFailure(Call<NewBatteryPojo> call, Throwable t) {
                if (t.getMessage() != null)
                    Log.d("12345", t.getMessage());
            }
        });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, NewBatteryPojo.NewBatteryItem> callback) {

        Call<NewBatteryPojo> call;

        if (searchType.equals("source")){
            if (value.length() == 0)
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, params.key);
            else
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryBySource(batteryType, params.key, value);

        }else if (searchType.equals("brand")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, params.key);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryByBrandName(batteryType, params.key, value);
            }

        }else if (searchType.equals("sku")){
            if (value.length() == 0){
                call = NetworkClient.getInstance()
                        .getApi()
                        .getBatteryList(batteryType, params.key);
            }else {
                call = NetworkClient.getInstance()
                        .getApi()
                        .searchBatteryBySKU(batteryType, params.key, value);
            }

        }else {

            call = NetworkClient.getInstance()
                    .getApi()
                    .getBatteryList(batteryType, params.key);
        }

        call.enqueue(new Callback<NewBatteryPojo>() {
            @Override
            public void onResponse(Call<NewBatteryPojo> call, Response<NewBatteryPojo> response) {
                if (response.body()!= null){
                    int t_p = response.body().getPage_count();
                    Log.d("12345", "total page  "+response.body().getPage_count());
                    Integer key = (params.key < t_p) ? params.key+1 : null;
                    callback.onResult(response.body().getData(), key);
                }
            }


            @Override
            public void onFailure(Call<NewBatteryPojo> call, Throwable t) {
                if (t.getMessage() != null)
                    Log.d("12345", t.getMessage());
            }
        });

    }
}
