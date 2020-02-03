package android.app.mobilebattery.Pojo;

public class BatteryAllocatedListPojo {

    private String id;
    private String battery_id;
    private String technician_id;
    private String assigned_quantity;
    private String sold_quantity;
    private String remaining_quantity;
    private String created_at;
    private String updated_at;
    private String technician_name;
    private String battery_sku;
    private String battery_brand;
    private String battery_size;
    private String battery_image;
    private String cost_price;

    public BatteryAllocatedListPojo() {
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBattery_id() {
        return battery_id;
    }

    public void setBattery_id(String battery_id) {
        this.battery_id = battery_id;
    }

    public String getTechnician_id() {
        return technician_id;
    }

    public void setTechnician_id(String technician_id) {
        this.technician_id = technician_id;
    }

    public String getAssigned_quantity() {
        return assigned_quantity;
    }

    public void setAssigned_quantity(String assigned_quantity) {
        this.assigned_quantity = assigned_quantity;
    }

    public String getSold_quantity() {
        return sold_quantity;
    }

    public void setSold_quantity(String sold_quantity) {
        this.sold_quantity = sold_quantity;
    }

    public String getRemaining_quantity() {
        return remaining_quantity;
    }

    public void setRemaining_quantity(String remaining_quantity) {
        this.remaining_quantity = remaining_quantity;
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

    public String getBattery_sku() {
        return battery_sku;
    }

    public void setBattery_sku(String battery_sku) {
        this.battery_sku = battery_sku;
    }

    public String getBattery_brand() {
        return battery_brand;
    }

    public void setBattery_brand(String battery_brand) {
        this.battery_brand = battery_brand;
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
}
