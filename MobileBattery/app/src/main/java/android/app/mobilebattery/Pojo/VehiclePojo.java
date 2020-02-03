package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VehiclePojo {


    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public ArrayList<VehicleItem> data;

    @SerializedName("count")
    public int count;

    @SerializedName("page_count")
    public int page_count;

    @SerializedName("error")
    public Boolean error;

    @SerializedName("message")
    public String message;

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<VehicleItem> getData() {
        return data;
    }

    public void setData(ArrayList<VehicleItem> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

    public class VehicleItem {

        public int id;
        public String number_plate;
        public String registration_date;
        public String renewal_date;
        public String km_completed;
        public String assigned_technician;
        public String created_at;
        public String updated_at;
        public String technician_name;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumber_plate() {
            return number_plate;
        }

        public void setNumber_plate(String number_plate) {
            this.number_plate = number_plate;
        }

        public String getRegistration_date() {
            return registration_date;
        }

        public void setRegistration_date(String registration_date) {
            this.registration_date = registration_date;
        }

        public String getRenewal_date() {
            return renewal_date;
        }

        public void setRenewal_date(String renewal_date) {
            this.renewal_date = renewal_date;
        }

        public String getKm_completed() {
            return km_completed;
        }

        public void setKm_completed(String km_completed) {
            this.km_completed = km_completed;
        }

        public String getAssigned_technician() {
            return assigned_technician;
        }

        public void setAssigned_technician(String assigned_technician) {
            this.assigned_technician = assigned_technician;
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

        public String getTechnician_name() {
            return technician_name;
        }

        public void setTechnician_name(String technician_name) {
            this.technician_name = technician_name;
        }
    }

}
