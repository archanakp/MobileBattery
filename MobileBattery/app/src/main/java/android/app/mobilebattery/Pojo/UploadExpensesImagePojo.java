package android.app.mobilebattery.Pojo;

public class UploadExpensesImagePojo {


    String status;
    String message;
    String error;
    Data data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public Data getData() {
        return data;
    }

    public class Data{
        String receipt_image;
        String name;

        public String getReceipt_image() {
            return receipt_image;
        }

        public void setReceipt_image(String receipt_image) {
            this.receipt_image = receipt_image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
