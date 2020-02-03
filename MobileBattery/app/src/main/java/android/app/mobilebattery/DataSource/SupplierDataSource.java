package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.SupplierListPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierDataSource extends PageKeyedDataSource<Integer, SupplierListPojo.SupplierItem> {


    private static int FIRST_PAGE = 1;

    private String type, value;

    public SupplierDataSource(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public SupplierDataSource() {
        type = "";
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, SupplierListPojo.SupplierItem> callback) {
        Call<SupplierListPojo> call;
        if (type.equals("name")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByName(FIRST_PAGE, value);
        }else if (type.equals("email")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByEmail(FIRST_PAGE, value);
        }else if (type.equals("phone")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByPhone(FIRST_PAGE, value);
        }else {
            call = NetworkClient.getInstance()
                    .getApi().getSupplierList(FIRST_PAGE);
        }

        call.enqueue(new Callback<SupplierListPojo>() {
            @Override
            public void onResponse(Call<SupplierListPojo> call, Response<SupplierListPojo> response) {
                if (response.body() != null) {
                    int t_p = response.body().page_count;
//                    int t_p = ((int)(Math.ceil(response.body().getCount()/10)));
                    Log.d("12345", "total Page "+t_p);
                    Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;

                    callback.onResult(response.body().getData(), null, key);
                }
            }

            @Override
            public void onFailure(Call<SupplierListPojo> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, SupplierListPojo.SupplierItem> callback) {

        Call<SupplierListPojo> call;
        if (type.equals("name")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByName(params.key, value);
        }else if (type.equals("email")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByEmail(params.key, value);
        }else if (type.equals("phone")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByPhone(params.key, value);
        }else {
            call = NetworkClient.getInstance()
                    .getApi().getSupplierList(params.key);
        }

        call.enqueue(new Callback<SupplierListPojo>() {
            @Override
            public void onResponse(Call<SupplierListPojo> call, Response<SupplierListPojo> response) {
                if (response.body() != null) {
                    Integer key = (params.key > FIRST_PAGE) ? params.key-1 : null;
                    callback.onResult(response.body().getData(), key);
                }
            }

            @Override
            public void onFailure(Call<SupplierListPojo> call, Throwable t) {

            }
        });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, SupplierListPojo.SupplierItem> callback) {

        Call<SupplierListPojo> call;
        if (type.equals("name")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByName(params.key, value);
        }else if (type.equals("email")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByEmail(params.key, value);
        }else if (type.equals("phone")){
            call = NetworkClient.getInstance()
                    .getApi().searchSupplierByPhone(params.key, value);
        }else {
            call = NetworkClient.getInstance()
                    .getApi().getSupplierList(params.key);
        }

        call.enqueue(new Callback<SupplierListPojo>() {
            @Override
            public void onResponse(Call<SupplierListPojo> call, Response<SupplierListPojo> response) {
                if (response.body() != null) {
//                    int t_p = ((int)(Math.ceil(response.body().getCount()/10)))+1;
                    int t_p = response.body().page_count;
                    Integer key = (params.key < t_p) ? params.key+1 : null;
                    callback.onResult(response.body().getData(), key);
                }
            }

            @Override
            public void onFailure(Call<SupplierListPojo> call, Throwable t) {

            }
        });

    }
}
