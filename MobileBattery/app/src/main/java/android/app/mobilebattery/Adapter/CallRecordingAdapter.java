package android.app.mobilebattery.Adapter;

import android.app.mobilebattery.Pojo.CallRecordingPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CallRecordingAdapter extends PagedListAdapter<CallRecordingPojo.CallRecordingItem, CallRecordingAdapter.ItemViewModel>{

    private Context context;

    public PlayRecording playRecording;
    public PauseRecording pauseRecording;

    public CallRecordingAdapter(Context context) {
        super(DIFF_CALLBACK);

        this.context = context;

    }

    public interface PlayRecording{
        void playCallRecording(int position, ImageView play, ImageView stop);
    }
    public interface PauseRecording{
        void pauseCallRecording(int position);
    }

    @NonNull
    @Override
    public ItemViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.call_recording_item_list, parent, false);

        return new ItemViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewModel holder, final int position) {


        CallRecordingPojo.CallRecordingItem item = getItem(position);

        if (item != null) {

            holder.agentName.setText(item.agent_name);
            holder.callRemark.setText(item.call_remarks);

            holder.callTime.setText(item.call_time);

            holder.playAudioIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.playAudioIV.setVisibility(View.GONE);
                    holder.pauseAudioIV.setVisibility(View.VISIBLE);
                    if (playRecording != null){
                        playRecording.playCallRecording(position, holder.playAudioIV,
                                holder.pauseAudioIV);
                    }
                }
            });
            holder.pauseAudioIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.pauseAudioIV.setVisibility(View.GONE);
                    holder.playAudioIV.setVisibility(View.VISIBLE);
                    if (pauseRecording != null){
                        pauseRecording.pauseCallRecording(position);
                    }

                }
            });
        }

    }


    private static DiffUtil.ItemCallback<CallRecordingPojo.CallRecordingItem> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CallRecordingPojo.CallRecordingItem>() {
                @Override
                public boolean areItemsTheSame(@NonNull CallRecordingPojo.CallRecordingItem oldItem, @NonNull CallRecordingPojo.CallRecordingItem newItem) {
                    return oldItem.id == newItem.id;
                }

                @Override
                public boolean areContentsTheSame(@NonNull CallRecordingPojo.CallRecordingItem oldItem, @NonNull CallRecordingPojo.CallRecordingItem newItem) {
                    return oldItem.agent_id == newItem.agent_id;
                }
            };

    class ItemViewModel extends RecyclerView.ViewHolder{

        TextView agentName, callTime, callRemark;
        ImageView playAudioIV, pauseAudioIV;

        public ItemViewModel(@NonNull View itemView) {
            super(itemView);

            agentName = itemView.findViewById(R.id.agentName);
            callTime = itemView.findViewById(R.id.callTime);
            callRemark = itemView.findViewById(R.id.callRemark);
            playAudioIV = itemView.findViewById(R.id.playAudioIV);
            pauseAudioIV = itemView.findViewById(R.id.pauseAudioIV);
        }
    }

}