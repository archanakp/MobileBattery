package android.app.mobilebattery.Helper;

import android.app.mobilebattery.Interface.ApiInterface;
import android.content.Context;

import com.google.android.gms.common.api.Api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

//    private static final String BASE_URL = "http://www.reports-mindsmetricks.com/MB/web/";
    private static final String BASE_URL = URL_Helper.COMMON_URL;
    private static  NetworkClient mInstence;

    private static Retrofit retrofit;

    private NetworkClient (){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    public static synchronized NetworkClient getInstance(){

        if (mInstence == null){
            mInstence = new NetworkClient();
        }
        return mInstence;
    }

    public static Retrofit getRetrofitClient(Context context) {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public ApiInterface getApi(){
        return retrofit.create(ApiInterface.class);
    }
}
