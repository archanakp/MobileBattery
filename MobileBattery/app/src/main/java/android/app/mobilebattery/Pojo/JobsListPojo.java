package android.app.mobilebattery.Pojo;

public class JobsListPojo {

    private String id, customer_id, vehicle_id, location, source_of_call, issues_faced, price_offered_type,
            price_offered, quick_note, job_status, cancellation_reason, receipt_no, created_at, updated_at, customer_name, vehicle_name,
            vehicle_model, allocated_to, reaschedule_reason;

    private Boolean is_reschedule, reschedule_accepted, is_warranty, push_sale, cash_received, overtime;



    public JobsListPojo(){}


    public Boolean getIs_reschedule() {
        return is_reschedule;
    }

    public void setIs_reschedule(Boolean is_reschedule) {
        this.is_reschedule = is_reschedule;
    }

    public Boolean getReschedule_accepted() {
        return reschedule_accepted;
    }

    public void setReschedule_accepted(Boolean reschedule_accepted) {
        this.reschedule_accepted = reschedule_accepted;
    }

    public Boolean getIs_warranty() {
        return is_warranty;
    }

    public void setIs_warranty(Boolean is_warranty) {
        this.is_warranty = is_warranty;
    }

    public Boolean getPush_sale() {
        return push_sale;
    }

    public void setPush_sale(Boolean push_sale) {
        this.push_sale = push_sale;
    }

    public Boolean getCash_received() {
        return cash_received;
    }

    public void setCash_received(Boolean cash_received) {
        this.cash_received = cash_received;
    }

    public Boolean getOvertime() {
        return overtime;
    }

    public void setOvertime(Boolean overtime) {
        this.overtime = overtime;
    }

    public String getAllocated_to() {
        return allocated_to;
    }

    public void setAllocated_to(String allocated_to) {
        this.allocated_to = allocated_to;
    }

    public String getReaschedule_reason() {
        return reaschedule_reason;
    }

    public void setReaschedule_reason(String reaschedule_reason) {
        this.reaschedule_reason = reaschedule_reason;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getVehicle_id() {
        return vehicle_id;
    }

    public void setVehicle_id(String vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSource_of_call() {
        return source_of_call;
    }

    public void setSource_of_call(String source_of_call) {
        this.source_of_call = source_of_call;
    }

    public String getIssues_faced() {
        return issues_faced;
    }

    public void setIssues_faced(String issues_faced) {
        this.issues_faced = issues_faced;
    }

    public String getPrice_offered_type() {
        return price_offered_type;
    }

    public void setPrice_offered_type(String price_offered_type) {
        this.price_offered_type = price_offered_type;
    }

    public String getPrice_offered() {
        return price_offered;
    }

    public void setPrice_offered(String price_offered) {
        this.price_offered = price_offered;
    }

    public String getQuick_note() {
        return quick_note;
    }

    public void setQuick_note(String quick_note) {
        this.quick_note = quick_note;
    }



    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getCancellation_reason() {
        return cancellation_reason;
    }

    public void setCancellation_reason(String cancellation_reason) {
        this.cancellation_reason = cancellation_reason;
    }

    public String getReceipt_no() {
        return receipt_no;
    }

    public void setReceipt_no(String receipt_no) {
        this.receipt_no = receipt_no;
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

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getVehicle_name() {
        return vehicle_name;
    }

    public void setVehicle_name(String vehicle_name) {
        this.vehicle_name = vehicle_name;
    }

    public String getVehicle_model() {
        return vehicle_model;
    }

    public void setVehicle_model(String vehicle_model) {
        this.vehicle_model = vehicle_model;
    }
}
