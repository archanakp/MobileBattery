package android.app.mobilebattery.Pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ExpenseListPojo {

    @SerializedName("status")
    public int status ;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public List<ExpenseListItem> data;

    @SerializedName("count")
    public int count;

    public int getCount() {
        return count;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<ExpenseListItem> getData() {
        return data;
    }


    public class ExpenseListItem {

        private int id;
        private int user_id;
        private Float amount;
        private String user;
        private String purpose;
        private String description;
        private String receipt_image;
        private String date;
        private String created_at;
        private String updated_at;

        public int getId() {
            return id;
        }

        public int getUser_id() {
            return user_id;
        }

        public Float getAmount() {
            return amount;
        }

        public String getUser() {
            return user;
        }

        public String getPurpose() {
            return purpose;
        }

        public String getDescription() {
            return description;
        }

        public String getReceipt_image() {
            return receipt_image;
        }

        public String getDate() {
            return date;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
