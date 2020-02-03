package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.CallRecordingAdapter;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Model.CallRecViewModel;
import android.app.mobilebattery.Pojo.CallRecordingPojo;
import android.app.mobilebattery.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class CallRecordingFragment extends Fragment implements CallRecordingAdapter.PlayRecording,
        CallRecordingAdapter.PauseRecording{


    private TextView noResultFoundTextView;
    private EditText searchByNameET, searchByTimeET;
    private RecyclerView callRecordingRecyclerView;


    private CallRecViewModel viewModel;
    private PagedList<CallRecordingPojo.CallRecordingItem> callRecordingList;

    private MediaPlayer mediaPlayer;
    private String audioURL;

    private ImageView audio_play, audio_stop;
    private Boolean played = false;
    private int p_position;


    public CallRecordingFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.call_recording_fragment, container, false);

        init(view);
        methodListener();
        populateRecordingList();
        searchByName();
        searchByTime();



        return view;
    }

    private void init(View view) {

        callRecordingRecyclerView = view.findViewById(R.id.callRecordingRecyclerView);
        searchByNameET = view.findViewById(R.id.searchByNameET);
        searchByTimeET = view.findViewById(R.id.searchByTimeET);
        noResultFoundTextView = view.findViewById(R.id.noResultFoundTextView);
    }

    private void methodListener() {

    }

    private void populateRecordingList() {

        if (getActivity() != null){

            callRecordingRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            callRecordingRecyclerView.setHasFixedSize(true);

            if (viewModel != null)
                if (viewModel.liveDataSource.getValue() != null)
                    viewModel.liveDataSource.getValue().invalidate();

            viewModel = new CallRecViewModel();
            final CallRecordingAdapter recordingAdapter = new CallRecordingAdapter(getActivity());
            recordingAdapter.playRecording = this;
            recordingAdapter.pauseRecording = this;
            viewModel.itemListData.observe(getActivity(), new Observer<PagedList<CallRecordingPojo.CallRecordingItem>>() {
                @Override
                public void onChanged(PagedList<CallRecordingPojo.CallRecordingItem> callRecordingItems) {
                    callRecordingList = callRecordingItems;
                    recordingAdapter.submitList(callRecordingItems);
                }
            });

            callRecordingRecyclerView.setAdapter(recordingAdapter);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        stopPlayer();

    }

    private void searchByName(){

        searchByNameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void searchByTime(){

        searchByTimeET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    @Override
    public void playCallRecording(int position, final ImageView play, final ImageView stop) {

//        audioURL = "https://a.tumblr.com/tumblr_m229z4r5UO1qf57hdo1.mp3";
        if (played) {
            if (!(p_position == position)) {
                audio_play.setVisibility(View.VISIBLE);
                audio_stop.setVisibility(View.GONE);
                stopPlayer();
            }
        }

        CallRecordingPojo.CallRecordingItem item = callRecordingList.get(position);
        if (item != null) {
            audioURL = item.recording_file;
            audio_play = play;
            audio_stop = stop;
            play.setVisibility(View.GONE);
            stop.setVisibility(View.VISIBLE);
            played =true;
            p_position = position;
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(audioURL);
            mediaPlayer.prepare(); // might take long! (for buffering, etc)

        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                play.setVisibility(View.VISIBLE);
                stop.setVisibility(View.GONE);
                mediaPlayer.release();
                mediaPlayer = null;
            }
        });
    }

    @Override
    public void pauseCallRecording(int position) {
        stopPlayer();
    }

    private void stopPlayer(){
        try {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }else {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        } catch (Exception e) {
            // NOTE: isPlaying() can potentially throw an exception and crash the application
            e.printStackTrace();
        }

    }
}
