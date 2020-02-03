package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewBatteryPojo {

    @SerializedName("status")
    private String status;

    @SerializedName("data")
    private List<NewBatteryItem> data;

    @SerializedName("error")
    private Boolean error;

    @SerializedName("count")
    private int count;

    @SerializedName("total_records")
    private int total_records;

    @SerializedName("page_count")
    private int page_count;

    @SerializedName("message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NewBatteryItem> getData() {
        return data;
    }

    public void setData(List<NewBatteryItem> data) {
        this.data = data;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class NewBatteryItem{

        @SerializedName("id")
        private int id;

        @SerializedName("supplier_id")
        private int supplier_id;

        @SerializedName("technician_id")
        private int technician_id;

        @SerializedName("battery_type")
        private String battery_type;

        @SerializedName("battery_source")
        private String battery_source;

        @SerializedName("product_id")
        private int product_id;

        @SerializedName("quantity")
        private int quantity;

        @SerializedName("cost_price")
        private long cost_price;

        @SerializedName("sale_price_type")
        private String sale_price_type;

        @SerializedName("retail_sale_price")
        private long retail_sale_price;

        @SerializedName("lower_retail_sale_price")
        private long lower_retail_sale_price;

        @SerializedName("higher_retail_sale_price")
        private long higher_retail_sale_price;

        @SerializedName("created_at")
        private String created_at;

        @SerializedName("updated_at")
        private String updated_at;

        @SerializedName("battery_image")
        private String battery_image;

        @SerializedName("supplier_name")
        private String supplier_name;

        @SerializedName("technician_name")
        private String technician_name;

        @SerializedName("battery_sku")
        private String battery_sku;

        @SerializedName("battery_size")
        private String battery_size;

        @SerializedName("brand_name")
        private String brand_name;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(int supplier_id) {
            this.supplier_id = supplier_id;
        }

        public int getTechnician_id() {
            return technician_id;
        }

        public void setTechnician_id(int technician_id) {
            this.technician_id = technician_id;
        }

        public String getBattery_type() {
            return battery_type;
        }

        public void setBattery_type(String battery_type) {
            this.battery_type = battery_type;
        }

        public String getBattery_source() {
            return battery_source;
        }

        public void setBattery_source(String battery_source) {
            this.battery_source = battery_source;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public long getCost_price() {
            return cost_price;
        }

        public void setCost_price(long cost_price) {
            this.cost_price = cost_price;
        }

        public String getSale_price_type() {
            return sale_price_type;
        }

        public void setSale_price_type(String sale_price_type) {
            this.sale_price_type = sale_price_type;
        }

        public long getRetail_sale_price() {
            return retail_sale_price;
        }

        public void setRetail_sale_price(long retail_sale_price) {
            this.retail_sale_price = retail_sale_price;
        }

        public long getLower_retail_sale_price() {
            return lower_retail_sale_price;
        }

        public void setLower_retail_sale_price(long lower_retail_sale_price) {
            this.lower_retail_sale_price = lower_retail_sale_price;
        }

        public long getHigher_retail_sale_price() {
            return higher_retail_sale_price;
        }

        public void setHigher_retail_sale_price(long higher_retail_sale_price) {
            this.higher_retail_sale_price = higher_retail_sale_price;
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

        public String getBattery_image() {
            return battery_image;
        }

        public void setBattery_image(String battery_image) {
            this.battery_image = battery_image;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public String getTechnician_name() {
            return technician_name;
        }

        public void setTechnician_name(String technician_name) {
            this.technician_name = technician_name;
        }

        public String getBattery_sku() {
            return battery_sku;
        }

        public void setBattery_sku(String battery_sku) {
            this.battery_sku = battery_sku;
        }

        public String getBattery_size() {
            return battery_size;
        }

        public void setBattery_size(String battery_size) {
            this.battery_size = battery_size;
        }

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }
    }

}
