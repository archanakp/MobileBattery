package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CashInHandPojo {

    @SerializedName("status")
    public int status;

    @SerializedName("data")
    public List<CashInHandItem> data;

    @SerializedName("count")
    public int count;

    @SerializedName("error")
    public Boolean error;

    @SerializedName("message")
    public String message;

    @SerializedName("total_records")
    public int total_records;

    @SerializedName("page_count")
    public int page_count;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CashInHandItem> getData() {
        return data;
    }

    public void setData(List<CashInHandItem> data) {
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

    public class CashInHandItem {

        @SerializedName("id")
        public int id;

        @SerializedName("user_id")
        public int user_id;

        @SerializedName("amount")
        public Long amount;

        @SerializedName("transferred_to")
        public int transferred_to;

        @SerializedName("transferred_cash_received")
        public Boolean transferred_cash_received;

        @SerializedName("created_at")
        public String created_at;

        @SerializedName("updated_at")
        public String updated_at;

        @SerializedName("agent_name")
        public String agent_name;

        @SerializedName("tranferred_to_agent")
        public String tranferred_to_agent;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public Long getAmount() {
            return amount;
        }

        public void setAmount(Long amount) {
            this.amount = amount;
        }

        public int getTransferred_to() {
            return transferred_to;
        }

        public void setTransferred_to(int transferred_to) {
            this.transferred_to = transferred_to;
        }

        public Boolean getTransferred_cash_received() {
            return transferred_cash_received;
        }

        public void setTransferred_cash_received(Boolean transferred_cash_received) {
            this.transferred_cash_received = transferred_cash_received;
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

        public String getTranferred_to_agent() {
            return tranferred_to_agent;
        }

        public void setTranferred_to_agent(String tranferred_to_agent) {
            this.tranferred_to_agent = tranferred_to_agent;
        }
    }

}
