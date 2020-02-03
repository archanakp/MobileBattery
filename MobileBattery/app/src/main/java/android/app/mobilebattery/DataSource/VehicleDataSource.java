package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.VehiclePojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleDataSource extends PageKeyedDataSource<Integer, VehiclePojo.VehicleItem> {

    public static final int FIRST_PAGE = 1;

    @Override
    public void loadInitial(@NonNull final LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, VehiclePojo.VehicleItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .getVehicleList(FIRST_PAGE)
                .enqueue(new Callback<VehiclePojo>() {
                    @Override
                    public void onResponse(Call<VehiclePojo> call, Response<VehiclePojo> response) {
                        if (response.body() != null){

                            int t_p = response.body().getPage_count();
//                    int t_p = ((int)(Math.ceil(response.body().getCount()/10)));
                            Log.d("12345", "total Page "+t_p);
                            Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;

                            callback.onResult(response.body().data, null, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<VehiclePojo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, VehiclePojo.VehicleItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .getVehicleList(params.key)
                .enqueue(new Callback<VehiclePojo>() {
                    @Override
                    public void onResponse(Call<VehiclePojo> call, Response<VehiclePojo> response) {

                        if (response.body() != null){

                            Integer key = (params.key > 1)? params.key - 1 :null;
                            callback.onResult(response.body().data, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<VehiclePojo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, VehiclePojo.VehicleItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .getVehicleList(params.key)
                .enqueue(new Callback<VehiclePojo>() {
                    @Override
                    public void onResponse(Call<VehiclePojo> call, Response<VehiclePojo> response) {

                        if (response.body() != null){
                            int t_p_no = ((int) Math.ceil(response.body().count/10))+1;

                            Integer key = (params.key < t_p_no)? params.key+1 : null;

                            callback.onResult(response.body().data,  key);
                        }
                    }

                    @Override
                    public void onFailure(Call<VehiclePojo> call, Throwable t) {

                    }
                });

    }
}
