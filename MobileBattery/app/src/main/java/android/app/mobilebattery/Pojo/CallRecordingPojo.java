package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CallRecordingPojo {

    @SerializedName("data")
    public List<CallRecordingItem> data;

    @SerializedName("status")
    public int status;

    @SerializedName("error")
    public Boolean error;

    @SerializedName("message")
    public String message;

    @SerializedName("count")
    public int count;

    @SerializedName("total_records")
    public int total_records;

    @SerializedName("page_count")
    public int page_count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CallRecordingItem> getData() {
        return data;
    }

    public void setData(List<CallRecordingItem> data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotal_records() {
        return total_records;
    }

    public void setTotal_records(int total_records) {
        this.total_records = total_records;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public class CallRecordingItem {

        @SerializedName("id")
        public int id;

        @SerializedName("agent_id")
        public int agent_id;

        @SerializedName("recording_file")
        public String recording_file;

        @SerializedName("call_remarks")
        public String call_remarks;

        @SerializedName("call_time")
        public String call_time;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("updated_at")
        public String updated_at;

        @SerializedName("agent_name")
        public String agent_name;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(int agent_id) {
            this.agent_id = agent_id;
        }

        public String getRecording_file() {
            return recording_file;
        }

        public void setRecording_file(String recording_file) {
            this.recording_file = recording_file;
        }

        public String getCall_remarks() {
            return call_remarks;
        }

        public void setCall_remarks(String call_remarks) {
            this.call_remarks = call_remarks;
        }

        public String getCall_time() {
            return call_time;
        }

        public void setCall_time(String call_time) {
            this.call_time = call_time;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getAgent_name() {
            return agent_name;
        }

        public void setAgent_name(String agent_name) {
            this.agent_name = agent_name;
        }
    }

}
