package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.CashInHandPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CashInHandDataSource extends PageKeyedDataSource<Integer, CashInHandPojo.CashInHandItem> {


    public static final int FIRST_PAGE = 1;

    private String type;
    private int value;

    public CashInHandDataSource(String type, int value) {
        this.type = type;
        this.value = value;
    }

    public CashInHandDataSource() {
        type = "";
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, CashInHandPojo.CashInHandItem> callback) {

        Call<CashInHandPojo> call;
        if (type.equals("user")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchByUserId(FIRST_PAGE, value);
        }else if (type.equals("trans_to")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchBySenderId(FIRST_PAGE, value);
        }else if (type.equals("status")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchByStatus(FIRST_PAGE, value);
        }else {
            Log.d("12345", "default Cash In Hand ");

            call = NetworkClient.getInstance()
                    .getApi()
                    .getCashInHandList(FIRST_PAGE);
        }
        call.enqueue(new Callback<CashInHandPojo>() {
                    @Override
                    public void onResponse(Call<CashInHandPojo> call, Response<CashInHandPojo> response) {

                        if (response.body() != null){
                            int t_p = response.body().getPage_count();
                            Log.d("12345", "Cash In Hand total Page "+t_p);
                            Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;
                            callback.onResult(response.body().data, null, key);

                        }
                    }

                    @Override
                    public void onFailure(Call<CashInHandPojo> call, Throwable t) {
                        Log.d("12345", "error in  Cash In Hand "+t.getMessage());
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CashInHandPojo.CashInHandItem> callback) {
        Call<CashInHandPojo> call;
        if (type.equals("user")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchByUserId(params.key, value);
        }else if (type.equals("trans_to")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchBySenderId(params.key, value);
        }else if (type.equals("status")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchByStatus(params.key, value);
        }else {
            call = NetworkClient.getInstance()
                    .getApi()
                    .getCashInHandList(params.key);
        }
        call.enqueue(new Callback<CashInHandPojo>() {
                    @Override
                    public void onResponse(Call<CashInHandPojo> call, Response<CashInHandPojo> response) {

                        if (response.body() != null){
                            Integer key = (params.key > 1) ? params.key-1 : null;

                            callback.onResult(response.body().data, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<CashInHandPojo> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CashInHandPojo.CashInHandItem> callback) {

        Call<CashInHandPojo> call;
        if (type.equals("user")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchByUserId(params.key, value);
        }else if (type.equals("trans_to")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchBySenderId(params.key, value);
        }else if (type.equals("status")){
            call = NetworkClient.getInstance()
                    .getApi()
                    .searchByStatus(params.key, value);
        }else {
            call = NetworkClient.getInstance()
                    .getApi()
                    .getCashInHandList(params.key);
        }
        call.enqueue(new Callback<CashInHandPojo>() {
                    @Override
                    public void onResponse(Call<CashInHandPojo> call, Response<CashInHandPojo> response) {

                        if (response.body() != null){

                            int t_p_no = response.body().page_count;
                            Integer key = (params.key < t_p_no)? params.key+1 : null;

                            callback.onResult(response.body().data, key);

                        }
                    }

                    @Override
                    public void onFailure(Call<CashInHandPojo> call, Throwable t) {

                    }
                });

    }
}
