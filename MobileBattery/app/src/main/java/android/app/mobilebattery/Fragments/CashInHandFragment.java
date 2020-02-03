package android.app.mobilebattery.Fragments;

import android.app.ProgressDialog;
import android.app.mobilebattery.Adapter.CashInHandAdapter;
import android.app.mobilebattery.Helper.RecyclerViewListener;
import android.app.mobilebattery.Helper.URL_Helper;
import android.app.mobilebattery.Model.CashInHandViewModel;
import android.app.mobilebattery.Pojo.CashInHandPojo;
import android.app.mobilebattery.R;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CashInHandFragment extends Fragment implements CashInHandAdapter.CashInHandAction{

    private Spinner transferredSpinner, receivedSpinner, statusSpinner;
    private TextView noResultFoundTextView;
    private RecyclerView cashInHandRecyclerView;
    private ImageButton refreshFilter;

    private ArrayList<String> userNameList;
    private ArrayList<String> secondUserNameList;
    private ArrayList<String> userIdList;
    private ArrayAdapter<String> spinnerAdapter;
    private ArrayAdapter<String> spinnerTwoAdapter;
    private ArrayAdapter<String> statusArrayAdapter;

    private String tranferredId, receivedId;

    private CashInHandViewModel viewModel;
    private CashInHandAdapter adapter;
    private PagedList<CashInHandPojo.CashInHandItem> cashInHandItemList;

    public CashInHandFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cash_in_hand_fragment, container, false);

        init(view);
        methodListener();
        loadUserData();
        loadCashList();

        ArrayList<String> list = new ArrayList<>();
        list.add("status");
        list.add("pending");
        list.add("received");
        statusArrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, list);
        statusArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(statusArrayAdapter);

        return view;
    }

    private void init(View view) {

        statusSpinner = view.findViewById(R.id.statusSpinner);
        refreshFilter = view.findViewById(R.id.refreshFilter);
        cashInHandRecyclerView = view.findViewById(R.id.cashInHandRecyclerView);
        noResultFoundTextView = view.findViewById(R.id.noResultFoundTextView);
        transferredSpinner = view.findViewById(R.id.transferredSpinner);
        receivedSpinner = view.findViewById(R.id.receivedSpinner);

    }

    private void methodListener() {
        transferredSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (userNameList.get(position).equals("Transfer from")){
                    loadCashList();
                }else {
                    tranferredId = userIdList.get(position);
                    searchList("user", Integer.valueOf(tranferredId));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        receivedSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (secondUserNameList.get(position).equals("Transfer to")){
                    loadCashList();
                }else {
                    receivedId = userIdList.get(position);
                    searchList("trans_to", Integer.valueOf(receivedId));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (statusArrayAdapter.getItem(position) != null) {
                    if (statusArrayAdapter.getItem(position).equals("pending")) {
                        searchList("status",0);
                    } else if (statusArrayAdapter.getItem(position).equals("received")) {
//                        receivedId = userIdList.get(position);
                        searchList("status",1);
                    }else {
                        loadCashList();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        refreshFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadCashList();
                transferredSpinner.setSelection(0);
                receivedSpinner.setSelection(0);
                statusSpinner.setSelection(0);
            }
        });

//        cashInHandRecyclerView.addOnItemTouchListener(new RecyclerViewListener.RecyclerTouchListener(
//                getActivity(), cashInHandRecyclerView, new RecyclerViewListener.ClickListener() {
//            @Override
//            public void onClick(View view, int position) {
//
//                Toast.makeText(getContext(), "cashInHandRecyclerView clicked  "+position, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }
//        ));
    }

    private void searchList(String type, int id) {

        if (viewModel != null){
            if (viewModel.liveDataSource.getValue() != null)
                viewModel.liveDataSource.getValue().invalidate();
        }

        if (type.equals("status")){
            viewModel = new CashInHandViewModel(type, id);
            Log.d("12345", type+"   "+id);
        }else if (type.equals("user")){
            viewModel = new CashInHandViewModel(type, id);
            Log.d("12345", type+"  "+id);

        }else if (type.equals("trans_to")){
            viewModel = new CashInHandViewModel(type, id);
            Log.d("12345", type+"  " +id);

        }else {
            viewModel = new CashInHandViewModel();
        }

        if (getActivity()!= null)
        viewModel.liveItemList.observe(getActivity(), new Observer<PagedList<CashInHandPojo.CashInHandItem>>() {
            @Override
            public void onChanged(PagedList<CashInHandPojo.CashInHandItem> cashInHandItems) {
                adapter.submitList(cashInHandItems);
            }
        });

        cashInHandRecyclerView.setAdapter(adapter);
    }

    private void loadCashList() {

        if (getActivity() != null) {
            cashInHandRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            cashInHandRecyclerView.setHasFixedSize(true);
            if (viewModel != null){
                if (viewModel.liveDataSource.getValue() != null)
                    viewModel.liveDataSource.getValue().invalidate();
            }

            viewModel = new CashInHandViewModel();

            adapter = new CashInHandAdapter(getActivity());

            viewModel.liveItemList.observe(getActivity(), new Observer<PagedList<CashInHandPojo.CashInHandItem>>() {
                @Override
                public void onChanged(PagedList<CashInHandPojo.CashInHandItem> cashInHandItems) {
                    adapter.submitList(cashInHandItems);
                    cashInHandItemList = cashInHandItems;
                }
            });

            cashInHandRecyclerView.setAdapter(adapter);
        }
    }

    private void loadUserData(){
        String URL = URL_Helper.USER_LIST_URL;
        userNameList = new ArrayList<>();
        secondUserNameList = new ArrayList<>();
        userNameList.add("Transfer from");
        secondUserNameList.add("Transfer to");
        userIdList = new ArrayList<>();
        userIdList.add("");

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading...");
        dialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.cancel();
                        try {
                            JSONArray array = response.getJSONArray("data");

                            for (int i=0; i<array.length(); i++ ){
                                JSONObject object = array.getJSONObject(i);
                                userIdList.add(object.getString("id"));
                                userNameList.add(object.getString("name"));
                                secondUserNameList.add(object.getString("name"));
                            }

                            if (getActivity()!=null) {
                                spinnerAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, userNameList);
                                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                receivedSpinner.setAdapter(spinnerAdapter);

                                spinnerTwoAdapter = new ArrayAdapter<>(getActivity(), R.layout.simple_spinner_item, secondUserNameList);
                                spinnerTwoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                transferredSpinner.setAdapter(spinnerTwoAdapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.cancel();
                        Log.d("12345", error.toString());
                        Snackbar.make(getView(), "error in network ", Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();

                    }
                });

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);

    }


    @Override
    public void deleteCashList(int position) {

    }
}
