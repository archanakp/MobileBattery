package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.CustomerPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerDataSource extends PageKeyedDataSource<Integer, CustomerPojo.CustomerData> {

    public static final int PAGE_SIZE = 10;
    public static final int FIRST_PAGE = 1;

    private String type;
    private String value;

    public CustomerDataSource(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public CustomerDataSource() {
        type = "";
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, CustomerPojo.CustomerData> callback) {
        Call<CustomerPojo> call;
        if (type.equals("name")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByName(FIRST_PAGE, value);
        }else if (type.equals("email")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByEmail(FIRST_PAGE, value);
        }else if (type.equals("phone")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByPhone(FIRST_PAGE, value);
        }else {
            call = NetworkClient.getInstance().getApi()
                    .getCustomerList(FIRST_PAGE);
        }
        call.enqueue(new Callback<CustomerPojo>() {
                    @Override
                    public void onResponse(Call<CustomerPojo> call, Response<CustomerPojo> response) {

                        if (response.body() != null){
                            int t_p = response.body().page_count;
                            Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;
                            callback.onResult(response.body().data, null, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<CustomerPojo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CustomerPojo.CustomerData> callback) {
        Call<CustomerPojo> call;
        if (type.equals("name")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByName(params.key, value);
        }else if (type.equals("email")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByEmail(params.key, value);
        }else if (type.equals("phone")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByPhone(params.key, value);
        }else {
            call = NetworkClient.getInstance().getApi()
                    .getCustomerList(params.key);
        }
        call.enqueue(new Callback<CustomerPojo>() {
                    @Override
                    public void onResponse(Call<CustomerPojo> call, Response<CustomerPojo> response) {


                        if (response.body() != null){
                            Integer key = (params.key > 1)? params.key - 1 : null;

                            callback.onResult(response.body().data, key);
                        }
                    }
                    @Override
                    public void onFailure(Call<CustomerPojo> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CustomerPojo.CustomerData> callback) {
        Call<CustomerPojo> call;
        if (type.equals("name")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByName(params.key, value);
        }else if (type.equals("email")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByEmail(params.key, value);
        }else if (type.equals("phone")){
            call = NetworkClient.getInstance().getApi()
                    .searchCustomerByPhone(params.key, value);
        }else {
            call = NetworkClient.getInstance().getApi()
                    .getCustomerList(params.key);
        }
        call.enqueue(new Callback<CustomerPojo>() {
                    @Override
                    public void onResponse(Call<CustomerPojo> call, Response<CustomerPojo> response) {

                        if (response.body() != null){
                            int total_page = response.body().page_count;
                            Integer key = (params.key < total_page)? params.key + 1 : null;

                            callback.onResult(response.body().data, key);
                        }
                    }
                    @Override
                    public void onFailure(Call<CustomerPojo> call, Throwable t) {

                    }
                });

    }
}
