package android.app.mobilebattery.DataSource;

import android.app.mobilebattery.Pojo.CallRecordingPojo;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

public class CallRecDataSourceFactory extends DataSource.Factory {


    private MutableLiveData<PageKeyedDataSource<Integer, CallRecordingPojo.CallRecordingItem>> liveData
            =new MutableLiveData<>();

    @NonNull
    @Override
    public DataSource create() {

        CallRecDataSource source = new CallRecDataSource();
        liveData.postValue(source);

        return source;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, CallRecordingPojo.CallRecordingItem>> getLiveData() {
        return liveData;
    }
}
