package android.app.mobilebattery.Pojo;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CustomerPojo {

    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public ArrayList<CustomerData> data;

    @SerializedName("count")
    public int count;

    @SerializedName("total_records")
    public int total_records;

    @SerializedName("page_count")
    public int page_count;

    @SerializedName("error")
    public Boolean error;

    @SerializedName("message")
    public String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ArrayList<CustomerData> getData() {
        return data;
    }

    public void setData(ArrayList<CustomerData> data) {
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


    public class CustomerData {

        @SerializedName("id")
        public int id;

        @SerializedName("firstname")
        public String firstname;

        @SerializedName("lastname")
        public String lastname;

        @SerializedName("email")
        public String email;

        @SerializedName("username")
        public String username;

        @SerializedName("phone")
        public Long phone;

        @SerializedName("profile_image")
        public String profile_image;

        @SerializedName("status")
        public Boolean status;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("updated_at")
        public String updated_at;

        @SerializedName("name")
        public String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstname() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname = firstname;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Long getPhone() {
            return phone;
        }

        public void setPhone(Long phone) {
            this.phone = phone;
        }

        public String getProfile_image() {
            return profile_image;
        }

        public void setProfile_image(String profile_image) {
            this.profile_image = profile_image;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}


