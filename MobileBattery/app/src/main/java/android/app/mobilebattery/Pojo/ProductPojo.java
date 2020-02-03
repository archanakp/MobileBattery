package android.app.mobilebattery.Pojo;

import java.util.List;

public class ProductPojo{

    private int status;
    private List<ProductItem> data;
    private Boolean error;
    private String message;
    private int count;
    private int page_count;
    private int total_records;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ProductItem> getData() {
        return data;
    }

    public void setData(List<ProductItem> data) {
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

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getTotal_records() {
        return total_records;
    }

    public void setTotal_records(int total_records) {
        this.total_records = total_records;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class ProductItem {

        private int id;
        private String battery_sku;
        private int brand_id;
        private String battery_size;
        private String battery_image;
        private String created_at;
        private String updated_at;
        private String brand_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getBattery_sku() {
            return battery_sku;
        }

        public void setBattery_sku(String battery_sku) {
            this.battery_sku = battery_sku;
        }

        public int getBrand_id() {
            return brand_id;
        }

        public void setBrand_id(int brand_id) {
            this.brand_id = brand_id;
        }

        public String getBattery_size() {
            return battery_size;
        }

        public void setBattery_size(String battery_size) {
            this.battery_size = battery_size;
        }

        public String getBattery_image() {
            return battery_image;
        }

        public void setBattery_image(String battery_image) {
            this.battery_image = battery_image;
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

        public String getBrand_name() {
            return brand_name;
        }

        public void setBrand_name(String brand_name) {
            this.brand_name = brand_name;
        }
    }


}
