package android.app.mobilebattery.Helper;

public class URL_Helper {

    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

//    private static final String COMMON_URL = "http://www.reports-mindsmetricks.com/MB/web/";
    public static final String COMMON_URL = "http://mindsmetricksdemo.com/MB/web/";
    public static final String USER_LOGIN_URL = COMMON_URL +"users/api/login";
    public static final String FORGOT_PASS_URL = COMMON_URL +"users/api/forgot-password";
    public static final String ADD_USER_URL = COMMON_URL +"users/api/add-user";
    public static final String EDIT_USER_URL = COMMON_URL +"users/api/edit-user";
    public static final String USER_LIST_URL = COMMON_URL +"users/api/users-list";
    public static final String UPLOAD_PROFILE_IMAGE_URL = COMMON_URL +"users/api/upload-profile-image";
    public static final String NEW_BATTERY_LIST_URL = COMMON_URL +"stock/api/list";
    public static final String ADD_BATTERY_RECORD_URL = COMMON_URL +"stock/api/add";
    public static final String EDIT_BATTERY_RECORD_URL = COMMON_URL +"stock/api/edit?id=";
    public static final String BATTERY_DETAILS_URL = COMMON_URL +"stock/api/view?id=";
    public static final String UPLOAD_BATTERY_IMAGE_URL = COMMON_URL +"stock/api/upload-battery-image";
    public static final String ALLOCATE_STOCK_URL = COMMON_URL +"stock/api/allocate-stock";
    public static final String GET_ALLOCATED_STOCK_URL = COMMON_URL +"stock/api/get-allocated-stock";
    public static final String SUPPLIER_LIST_URL = COMMON_URL +"suppliers/api/list";
    public static final String ADD_SUPPLIER_URL = COMMON_URL +"suppliers/api/add";
    public static final String EDIT_SUPPLIER_URL = COMMON_URL +"suppliers/api/edit?id=";
    public static final String ADD_CUSTOMER_URL = COMMON_URL +"customers/api/add";
    public static final String CUSTOMER_list_URL = COMMON_URL +"customers/api/list";
    public static final String CUSTOMER_DETAILS_URL = COMMON_URL +"customers/api/view";
    public static final String EDIT_CUSTOMER_URL = COMMON_URL +"customers/api/edit";
    public static final String VEHICLE_list_URL = COMMON_URL +"company-vehicles/api/list";
    public static final String ADD_VEHICLE_URL = COMMON_URL +"company-vehicles/api/add";
    public static final String EDIT_VEHICLE_URL = COMMON_URL +"company-vehicles/api/edit?id=";
    public static final String VEHICLE_DETAILS_URL = COMMON_URL +"company-vehicles/api/view";
    public static final String ASSIGN_VEHICLE_URL = COMMON_URL +"company-vehicles/api/assign-vehicle";
    public static final String ADD_VEHICLE_INSURANCE_URL = COMMON_URL +"company-vehicles/api/add-insurance-history";
    public static final String EDIT_VEHICLE_INSURANCE_URL = COMMON_URL +"company-vehicles/api/edit-insurance-history?id=2";
    public static final String ADD_VEHICLE_SERVICE_URL = COMMON_URL +"company-vehicles/api/add-service-history";
    public static final String EDIT_VEHICLE_SERVICE_URL = COMMON_URL +"company-vehicles/api/edit-service-history?id=2";
    public static final String JOBS_LIST_URL = COMMON_URL +"jobs/api/list";
    public static final String ADD_JOBS_URL = COMMON_URL +"jobs/api/add";
    public static final String EDIT_JOBS_URL = COMMON_URL +"jobs/api/edit?id=";
    public static final String ASSIGN_JOBS_URL = COMMON_URL +"jobs/api/assign-job";
    public static final String JOB_DETAILS_URL = COMMON_URL +"jobs/api/view?id=";
    public static final String OVERTIME_LIST_URL = COMMON_URL +"overtimes/api/list";
    public static final String OVERTIME_REQUEST_URL = COMMON_URL +"overtimes/api/request";
    public static final String OVERTIME_APPROVE_URL = COMMON_URL +"overtimes/api/approve?id=";
    public static final String OVERTIME_DETAILS_URL = COMMON_URL +"overtimes/api/view?id=";
    public static final String INCENTIVE_LIST_URL = COMMON_URL +"incentives/api/list";
    public static final String ADD_INCENTIVE_URL = COMMON_URL +"incentives/api/add";
    public static final String INVOICE_LIST_URL = COMMON_URL +"invoices/api/list";
    public static final String CASH_IN_HAND_LIST_URL = COMMON_URL +"cash-in-hand/api/list";
    public static final String TRANSFERRED_CASH_URL = COMMON_URL +"cash-in-hand/api/transferred-cash?id=";
    public static final String RECEIVED_CASH_URL = COMMON_URL +"cash-in-hand/api/received-cash?id=";
    public static final String CALL_RECORDING_LIST_URL = COMMON_URL +"call-recordings/api/list";
    public static final String ADD_BATTERY_BRAND_URL = COMMON_URL +"brands/api/add";
    public static final String EDIT_BATTERY_BRAND_URL = COMMON_URL +"brands/api/edit?id=";
    public static final String BATTERY_BRAND_SEARCH_URL = COMMON_URL +"brands/api/brand-search?filter=";
    public static final String ADD_EXPENSE_URL = COMMON_URL +"expenses/api/add";
    public static final String EDIT_EXPENSE_URL = COMMON_URL +"expenses/api/edit?id=";
    public static final String VIEW_EXPENSE_URL = COMMON_URL +"expenses/api/view?id=";
    public static final String PRODUCT_SEARCH_URL = COMMON_URL +"products/api/product-search";
    public static final String PRODUCT_IMAGE_UPLOAD_URL = COMMON_URL +"products/api/upload-battery-image?id=";
    public static final String ADD_PRODUCT_URL = COMMON_URL +"products/api/add";
    public static final String EDIT_PRODUCT_URL = COMMON_URL +"products/api/edit?id=";


    public static final int LIST_PAGE_SIZE = 10;
    public URL_Helper() {
    }
}
