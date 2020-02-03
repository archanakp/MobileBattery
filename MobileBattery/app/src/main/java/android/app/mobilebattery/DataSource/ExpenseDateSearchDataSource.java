package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Helper.NetworkClient;
import android.app.mobilebattery.Pojo.ExpenseListPojo;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseDateSearchDataSource extends PageKeyedDataSource<Integer, ExpenseListPojo.ExpenseListItem> {

    public static final int FIRST_PAGE = 1;
//    private String call_type, user;
    private String date;


    public ExpenseDateSearchDataSource(String date) {
        this.date = date;
        Log.d("12345", "ExpenseDateSearchDataSource date ");
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, ExpenseListPojo.ExpenseListItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .searchByExpenseDate(FIRST_PAGE, date)
                .enqueue(new Callback<ExpenseListPojo>() {
                    @Override
                    public void onResponse(Call<ExpenseListPojo> call, Response<ExpenseListPojo> response) {
                        if (response.body() != null) {
                            callback.onResult(response.body().data, null, FIRST_PAGE + 1);
                        }
                    }

                    @Override
                    public void onFailure(Call<ExpenseListPojo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ExpenseListPojo.ExpenseListItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .searchByExpenseDate(params.key, date)
                .enqueue(new Callback<ExpenseListPojo>() {
                    @Override
                    public void onResponse(Call<ExpenseListPojo> call, Response<ExpenseListPojo> response) {
                        if (response.body() != null) {

                            Integer key = (params.key > FIRST_PAGE) ? params.key - 1 : null;
                            callback.onResult(response.body().data, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<ExpenseListPojo> call, Throwable t) {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, ExpenseListPojo.ExpenseListItem> callback) {

        NetworkClient.getInstance()
                .getApi()
                .searchByExpenseDate(params.key, date)
                .enqueue(new Callback<ExpenseListPojo>() {
                    @Override
                    public void onResponse(Call<ExpenseListPojo> call, Response<ExpenseListPojo> response) {
                        if (response.body() != null) {
                            int t_p = ((int) Math.ceil(response.body().count / 10)) + 1;
                            Integer key = (params.key < t_p) ? params.key + 1 : null;
                            callback.onResult(response.body().data, key);
                        }
                    }

                    @Override
                    public void onFailure(Call<ExpenseListPojo> call, Throwable t) {

                    }
                });
    }
}
