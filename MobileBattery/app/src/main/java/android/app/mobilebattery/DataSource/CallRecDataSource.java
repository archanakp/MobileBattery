package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.CallRecordingPojo;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallRecDataSource extends PageKeyedDataSource<Integer, CallRecordingPojo.CallRecordingItem> {

    public static final int PAGE_SIZE = 10;
    public static final int FIRST_PAGE = 1;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, CallRecordingPojo.CallRecordingItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .getRecordingList(FIRST_PAGE)
                .enqueue(new Callback<CallRecordingPojo>() {
                    @Override
                    public void onResponse(Call<CallRecordingPojo> call, Response<CallRecordingPojo> response) {
                        if (response.body() != null){
                            int t_p = response.body().getPage_count();
                            Integer key = (FIRST_PAGE< t_p) ? FIRST_PAGE+1 : null;
                            callback.onResult(response.body().data, null, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<CallRecordingPojo> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CallRecordingPojo.CallRecordingItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .getRecordingList(params.key)
                .enqueue(new Callback<CallRecordingPojo>() {
                    @Override
                    public void onResponse(Call<CallRecordingPojo> call, Response<CallRecordingPojo> response) {

                        if (response.body() != null){

                            Integer key = (params.key >1) ? params.key -1 : null;

                            callback.onResult(response.body().data, key);

                        }
                    }

                    @Override
                    public void onFailure(Call<CallRecordingPojo> call, Throwable t) {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, CallRecordingPojo.CallRecordingItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .getRecordingList(params.key)
                .enqueue(new Callback<CallRecordingPojo>() {
                    @Override
                    public void onResponse(Call<CallRecordingPojo> call, Response<CallRecordingPojo> response) {

                        if (response.body() != null){

                            int t_page = response.body().page_count;
                            Integer p_no = (params.key < t_page) ? params.key+1 : null;

                            callback.onResult(response.body().data, p_no);

                        }
                    }

                    @Override
                    public void onFailure(Call<CallRecordingPojo> call, Throwable t) {

                    }
                });

    }
}
