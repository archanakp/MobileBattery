package android.app.mobilebattery.Interface;

import android.app.mobilebattery.Pojo.BatteryBrandPojo;
import android.app.mobilebattery.Pojo.CallRecordingPojo;
import android.app.mobilebattery.Pojo.CashInHandPojo;
import android.app.mobilebattery.Pojo.CustomerPojo;
import android.app.mobilebattery.Pojo.ExpenseListPojo;
import android.app.mobilebattery.Pojo.ImgeUploadPojo;
import android.app.mobilebattery.Pojo.NewBatteryPojo;
import android.app.mobilebattery.Pojo.ProductPojo;
import android.app.mobilebattery.Pojo.SupplierListPojo;
import android.app.mobilebattery.Pojo.UploadExpensesImagePojo;
import android.app.mobilebattery.Pojo.UploadProductDataPojo;
import android.app.mobilebattery.Pojo.VehiclePojo;
import android.content.Intent;

import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {

//    Upload User profile Image

    @Multipart
    @POST("users/api/upload-profile-image")
    Call<ImgeUploadPojo> uploadUserImage(@Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);


    @Multipart
    @POST
    Call<ImgeUploadPojo> uploadEditedUserImage(@Url String url, @Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);


//    Brand Module
    @GET("brands/api/list")
    Call<BatteryBrandPojo> getBrandList(@Query("page")Integer page_no);

    @FormUrlEncoded
    @POST("brands/api/list")
    Call<BatteryBrandPojo> getSearchByBrandList(@Query("page")Integer page_no, @Field ("filters[brand_name]") String brand_name);

    @FormUrlEncoded
    @POST("brands/api/list")
    Call<BatteryBrandPojo> getSearchByStatusList(@Query("page")Integer page_no, @Field("filters[status]") Integer status);


    @FormUrlEncoded
    @POST("brands/api/brand-search")
    Call<BatteryBrandPojo> searchBrandList(@Field("filter") String filter);


//  Expenses Module
    @GET("expenses/api/list")
    Call<ExpenseListPojo> getExpenseList(@Query("page")Integer page_no);

    @Multipart
    @POST("expenses/api/upload-receipt-image")
    Call<UploadExpensesImagePojo> uploadExpensesImage(@Part MultipartBody.Part file );


    @Multipart
    @POST("expenses/api/upload-receipt-image")
    Call<UploadExpensesImagePojo> uploadExpensesEditedImage(@Query ("id") Integer id, @Part MultipartBody.Part file );

    @FormUrlEncoded
    @POST("expenses/api/list")
    Call<ExpenseListPojo> searchByExpenseDate(@Query("page")Integer page_no, @Field("filters[date]") String date);

    @FormUrlEncoded
    @POST("expenses/api/list")
    Call<ExpenseListPojo> searchByExpenseUser(@Query("page")Integer page_no, @Field("filters[user_id]") Integer user_id);

//  Vehicle Module

    @GET("company-vehicles/api/list")
    Call<VehiclePojo> getVehicleList(@Query("page") Integer page_no);


    @Multipart
    @POST("company-vehicles/api/upload-insurance-receipt")
    Call<UploadExpensesImagePojo> uploadVehicleInsReceipt(@Part MultipartBody.Part file );

    @Multipart
    @POST("company-vehicles/api/upload-insurance-receipt")
    Call<UploadExpensesImagePojo> editVehicleInsReceipt(@Query ("id") Integer id, @Part MultipartBody.Part file );


    @Multipart
    @POST("company-vehicles/api/upload-service-receipt")
    Call<UploadExpensesImagePojo> uploadVehicleSrvcReceipt(@Part MultipartBody.Part file );


    @Multipart
    @POST("company-vehicles/api/upload-service-receipt")
    Call<UploadExpensesImagePojo> editVehicleSrvcReceipt(@Query ("id") Integer id, @Part MultipartBody.Part file );


//    Customer Profile Module
    @GET("customers/api/list")
    Call<CustomerPojo> getCustomerList(@Query("page")Integer page_no);


    @FormUrlEncoded
    @POST("customers/api/list")
    Call<CustomerPojo> searchCustomerByName(@Query("page")Integer page_no, @Field("filters[name]") String name);


    @FormUrlEncoded
    @POST("customers/api/list")
    Call<CustomerPojo> searchCustomerByEmail(@Query("page")Integer page_no, @Field("filters[email]") String email);


    @FormUrlEncoded
    @POST("customers/api/list")
    Call<CustomerPojo> searchCustomerByPhone(@Query("page")Integer page_no, @Field("filters[phone]") String phone);


    @Multipart
    @POST("customers/api/upload-profile-image")
    Call<UploadExpensesImagePojo> uploadCustomerProfileImage(@Part MultipartBody.Part file );


    @Multipart
    @POST("customers/api/upload-profile-image")
    Call<UploadExpensesImagePojo> uploadEditCustomerProfileImage(@Query ("id") Integer id, @Part MultipartBody.Part file );


//    Call Recording Module
    @GET("call-recordings/api/list")
    Call<CallRecordingPojo> getRecordingList(@Query("page") Integer page_no);




//    Cash In Hand  Module
    @GET("cash-in-hand/api/list")
    Call<CashInHandPojo> getCashInHandList(@Query("page") Integer page_no);


    @FormUrlEncoded
    @POST("cash-in-hand/api/list")
    Call<CashInHandPojo> searchByUserId(@Query("page") Integer page_no, @Field("filters[user_id]") Integer user_id);


    @FormUrlEncoded
    @POST("cash-in-hand/api/list")
    Call<CashInHandPojo> searchBySenderId(@Query("page") Integer page_no, @Field("filters[transferred_to]") Integer transferred_to_id);


    @FormUrlEncoded
    @POST("cash-in-hand/api/list")
    Call<CashInHandPojo> searchByStatus(@Query("page") Integer page_no, @Field("filters[transferred_cash_received]") Integer bit);


//    Supplier Modules api
    @GET("suppliers/api/list")
    Call<SupplierListPojo> getSupplierList(@Query("page") Integer page_no);

    @FormUrlEncoded
    @POST("suppliers/api/list")
    Call<SupplierListPojo> searchSupplierByName(@Query("page") Integer page_no, @Field("filters[supplier_name]") String name);

    @FormUrlEncoded
    @POST("suppliers/api/list")
    Call<SupplierListPojo> searchSupplierByEmail(@Query("page") Integer page_no, @Field("filters[supplier_email]") String name);

    @FormUrlEncoded
    @POST("suppliers/api/list")
    Call<SupplierListPojo> searchSupplierByPhone(@Query("page") Integer page_no, @Field("filters[supplier_phone]") String phone);




//    Battery Stock Modules api
    @GET("stock/api/list")
    Call<NewBatteryPojo> getBatteryList(@Query("type") String b_type, @Query("page") Integer page_no);

    @FormUrlEncoded
    @POST("stock/api/list")
    Call<NewBatteryPojo> searchBatteryByBrandName(@Query("type") String b_type, @Query("page") Integer page_no, @Field("filters[brand_name]") String name);
//
    @FormUrlEncoded
    @POST("stock/api/list")
    Call<NewBatteryPojo> searchBatteryBySKU(@Query("type") String b_type, @Query("page") Integer page_no, @Field("filters[battery_sku]") String sku);

    @FormUrlEncoded
    @POST("stock/api/list")
    Call<NewBatteryPojo> searchBatteryBySource(@Query("type") String b_type, @Query("page") Integer page_no, @Field("filters[battery_source]") String source);



//   Product Module api
//    Endpoint: http://reports-mindsmetricks.com/MB/web/

    @GET("products/api/list")
    Call<ProductPojo> getProductList(@Query("page") Integer page_no);


    @Multipart
    @POST("products/api/upload-battery-image")
    Call<ImgeUploadPojo> uploadProductImage(@Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);


    @Multipart
    @POST
    Call<ImgeUploadPojo> uploadEditedProductImage(@Url String url, @Part MultipartBody.Part file/*, @Part("name") RequestBody requestBody*/);


    @FormUrlEncoded
    @POST("products/api/add")
    Call<UploadProductDataPojo> uploadProductData(@Field("battery_sku") String sku, @Field("battery_image") String image,
                                                  @Field("battery_size") String size, @Field("brand_id") Integer brand_id);



    @FormUrlEncoded
    @POST
    Call<UploadProductDataPojo> uploadEditedProductData(@Url String url, @Field("battery_sku") String sku,@Field("battery_image") String image,
                                @Field("battery_size") String size, @Field("brand_id") Integer brand_id);


    @FormUrlEncoded
    @POST("products/api/product-search")
    Call<ProductPojo> searchProductList(@Query("page") Integer page_no, @Field("filter") String filter);



    @FormUrlEncoded
    @POST("incentives/api/add")
    Call<UploadProductDataPojo> submitIncentiveData(@Field("agent_id") String id, @Field("incentive_amount") String amt, @Field("lead_id") String lead );


}
