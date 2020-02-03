package android.app.mobilebattery.Activity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.mobilebattery.Fragments.BatteryBrandFragment;
import android.app.mobilebattery.Fragments.BatterySupplierFragment;
import android.app.mobilebattery.Fragments.CallRecordingFragment;
import android.app.mobilebattery.Fragments.CashInHandFragment;
import android.app.mobilebattery.Fragments.ChatFragment;
import android.app.mobilebattery.Fragments.CustomerFragment;
import android.app.mobilebattery.Fragments.ExpenseFragment;
import android.app.mobilebattery.Fragments.IncentiveFragment;
import android.app.mobilebattery.Fragments.InventoryAllocation;
import android.app.mobilebattery.Fragments.InvoiceFragment;
import android.app.mobilebattery.Fragments.JobsFragment;
import android.app.mobilebattery.Fragments.ManageUser;
import android.app.mobilebattery.Fragments.NewBatteryFragment;
import android.app.mobilebattery.Fragments.OverTimeFragment;
import android.app.mobilebattery.Fragments.ProductFragment;
import android.app.mobilebattery.Fragments.ScrapBatteryFragment;
import android.app.mobilebattery.Fragments.VehicleFragment;
import android.app.mobilebattery.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adminhome extends AppCompatActivity {

    private TextView usersTab, newBatteryTab, inventoryAllocationTab, vehicleDetails;
    private TextView customerProfileTab;
    private TextView tabTitleText, supplierTab;
    private TextView currentSelectedTab, myAccountText;

    private CircleImageView myAccountIcon;
    private Toolbar toolbar;

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private RecyclerView drawerRecyclerView;
    private adapter recyclerAdapter;

    private ArrayList<String> tabId = new ArrayList<>();

    private ArrayList<String> tabTitle = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhome);
        init();
        setSupportActionBar(toolbar);
        setArrayList();
        setUpDrawer();

//        Intent intent = getIntent();
//        setDataFromIntent(intent);

        currentSelectedTab = newBatteryTab;
//        default fragment
        ManageUser fragment = new ManageUser();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();

        methodListener();
    }

    private void setArrayList() {
        tabTitle.add("Manage Users");
        tabTitle.add("New Batteries");
        tabTitle.add("Inventory Allocation");
        tabTitle.add("Supplier");
        tabTitle.add("Scrap Battery");
        tabTitle.add("Vehicle Details");
        tabTitle.add("Customer Profile");
        tabTitle.add("Jobs");
        tabTitle.add("Overtime");
        tabTitle.add("Incentives");
        tabTitle.add("Invoice");
        tabTitle.add("Cash In Hand");
        tabTitle.add("Call Recording");
        tabTitle.add("Battery Brand");
        tabTitle.add("Expense");
        tabTitle.add("Chat");
        tabTitle.add("Product");

        tabId.add("usersTab");
        tabId.add("newBatteryTab");
        tabId.add("inventoryAllocationTab");
        tabId.add("supplierTab");
        tabId.add("scrapBatteryTab");
        tabId.add("vehicleDetails");
        tabId.add("customerProfileTab");
        tabId.add("jobs");
        tabId.add("overTime");
        tabId.add("incentives");
        tabId.add("invoice");
        tabId.add("cashInHand");
        tabId.add("callRecording");
        tabId.add("batteryBrand");
        tabId.add("expense");
        tabId.add("chat");
        tabId.add("product");


    }


    private void setUpDrawer() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        drawerRecyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new adapter(this, tabTitle, tabId);
        drawerRecyclerView.setAdapter(recyclerAdapter);

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.open_dr);

//        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.nav_drwr_fragment);
//        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
//        drawerFragment.setUpDrawer(R.id.nav_drwr_fragment, drawerLayout, toolbar);
    }

//    private void setDataFromIntent(Intent intent) {
//        intent.getStringExtra("id");
//        myAccountText.setText(intent.getStringExtra("name"));
//        intent.getStringExtra("profile_image");
//        Picasso.get()
//                .load(intent.getStringExtra("profile_image"))
//                .into(myAccountIcon);
//    }

    private void init() {
        drawerRecyclerView = findViewById(R.id.drawerRecyclerView);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);
        myAccountText = findViewById(R.id.myAccountText);
        myAccountIcon = findViewById(R.id.myAccountIcon);
        tabTitleText = findViewById(R.id.tabTitleText);
        supplierTab = findViewById(R.id.supplierTab);
        usersTab = findViewById(R.id.usersTab);
        customerProfileTab = findViewById(R.id.customerProfileTab);
        vehicleDetails = findViewById(R.id.vehicleDetails);
        newBatteryTab = findViewById(R.id.newBatteryTab);
        inventoryAllocationTab = findViewById(R.id.inventoryAllocationTab);
    }

    @Override
    public void onBackPressed() {
        drawerLayout = findViewById(R.id.drawerLayout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void methodListener() {



    }

    public void goBack(View view) {
        super.onBackPressed();
    }



    private void changeFragment(String title, String id){

        tabTitleText.setText(title);
        drawerLayout.closeDrawer(GravityCompat.START);
        if (id.equals("usersTab")){
            ManageUser fragment = new ManageUser();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("newBatteryTab")){
            NewBatteryFragment fragment = new NewBatteryFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("inventoryAllocationTab")){
            InventoryAllocation fragment = new InventoryAllocation();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("supplierTab")){
            BatterySupplierFragment fragment = new BatterySupplierFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("customerProfileTab")){
            CustomerFragment fragment = new CustomerFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("vehicleDetails")){
            VehicleFragment fragment = new VehicleFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("scrapBatteryTab")){
            ScrapBatteryFragment fragment = new ScrapBatteryFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("jobs")){
            JobsFragment fragment = new JobsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("overTime")){
            OverTimeFragment fragment = new OverTimeFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("incentives")){
            IncentiveFragment fragment = new IncentiveFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("invoice")){
            InvoiceFragment fragment = new InvoiceFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("cashInHand")){
            CashInHandFragment fragment = new CashInHandFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("callRecording")){
            CallRecordingFragment fragment = new CallRecordingFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("batteryBrand")){
            BatteryBrandFragment fragment = new BatteryBrandFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("expense")){
            ExpenseFragment fragment = new ExpenseFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("chat")){
            ChatFragment fragment = new ChatFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else if (id.equals("product")){
            ProductFragment fragment = new ProductFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frameLayout, fragment);
            transaction.commit();
        }else {
            Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
        }
    }

    private class adapter extends RecyclerView.Adapter<adapter.myViewHolder> {
        Context context;
        List<String> mData;
        List<String> tabId;

        public adapter(Context context, List<String> data, List<String> tabId) {
            this.context = context;
            this.mData = data;
            this.tabId =tabId;
        }

        @Override
        public adapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_for_drawerlist, parent, false);
            return new myViewHolder(view);
        }

        @Override
        public void onBindViewHolder(adapter.myViewHolder holder, final int position) {
            holder.nav.setText(mData.get(position));

            holder.nav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeFragment(mData.get(position),tabId.get(position) );
                }
            });
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
        public class myViewHolder extends RecyclerView.ViewHolder {
            TextView nav;

            public myViewHolder(View itemView) {
                super(itemView);
                nav =  itemView.findViewById(R.id.newBatteryTab);
            }
        }
    }




}
