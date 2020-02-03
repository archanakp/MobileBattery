package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;



public class BatteryBrandPojo {


    @SerializedName("status")
    private int status ;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<BatteryBrandItem> data;

    @SerializedName("count")
    private int count;

    @SerializedName("total_records")
    private int total_records;

    @SerializedName("page_count")
    private int page_count;

    @SerializedName("error")
    private Boolean error;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<BatteryBrandItem> getData() {
        return data;
    }

    public void setData(List<BatteryBrandItem> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public class BatteryBrandItem {

        @SerializedName("id")
        private int id;

        @SerializedName("brand_name")
        private String brand_name;

        @SerializedName("status")
        private Boolean status;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("updated_at")
        private String updated_at;

        public int getId() {
            return id;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public Boolean getStatus() {
            return status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
