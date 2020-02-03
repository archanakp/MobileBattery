package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SupplierListPojo {

    @SerializedName("status")
    public int status;

    @SerializedName("error")
    public Boolean error;

    @SerializedName("count")
    public int count;

    @SerializedName("total_records")
    public int total_records;

    @SerializedName("page_count")
    public int page_count;


    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<SupplierItem> data;


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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<SupplierItem> getData() {
        return data;
    }

    public void setData(List<SupplierItem> data) {
        this.data = data;
    }

    public class SupplierItem{

       public int id;
       public String supplier_name;
       public String supplier_email;
       public String supplier_phone;
       public String supplier_company;
       public String supplier_address;
       public String created_at;
       public String updated_at;

       public int getId() {
           return id;
       }

       public void setId(int id) {
           this.id = id;
       }

       public String getSupplier_name() {
           return supplier_name;
       }

       public void setSupplier_name(String supplier_name) {
           this.supplier_name = supplier_name;
       }

       public String getSupplier_email() {
           return supplier_email;
       }

       public void setSupplier_email(String supplier_email) {
           this.supplier_email = supplier_email;
       }

       public String getSupplier_phone() {
           return supplier_phone;
       }

       public void setSupplier_phone(String supplier_phone) {
           this.supplier_phone = supplier_phone;
       }

       public String getSupplier_company() {
           return supplier_company;
       }

       public void setSupplier_company(String supplier_company) {
           this.supplier_company = supplier_company;
       }

       public String getSupplier_address() {
           return supplier_address;
       }

       public void setSupplier_address(String supplier_address) {
           this.supplier_address = supplier_address;
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

   }
}
