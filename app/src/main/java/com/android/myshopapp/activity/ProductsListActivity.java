package com.android.myshopapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.myshopapp.R;
import com.android.myshopapp.model.ProductsData;
import com.android.myshopapp.util.HttpHandler;
import com.android.myshopapp.util.ProductsAdapter;
import com.android.myshopapp.util.ProductsDataDBHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ProductsListActivity extends AppCompatActivity {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProductsDataDBHelper dbHelper;
    private ProductsAdapter adapter;
    private String filter = "";
    private String TAG = ProductsListActivity.class.getSimpleName();
    private Parcelable recyclerViewState;
    private ProgressDialog pDialog;
    int position = 0;
    public Context mCtx;
    public List<ProductsData> productList = new LinkedList<ProductsData>();
    // URL to get prodcuts JSON
    private static String url = "https://jsonplaceholder.typicode.com/todos";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        mCtx = this;

        //initialize the variables
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        dbHelper = new ProductsDataDBHelper(this);

        new GetProductsdata().execute();
    }
    private void populaterecyclerView(String filter){

        productList = dbHelper.productsList(filter);
        adapter = new ProductsAdapter(productList, this, mRecyclerView);
        mRecyclerView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter!=null)
        {
            productList.clear();
            productList.addAll(dbHelper.productsList(filter));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    public class GetProductsdata extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(mCtx);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray productslist = new JSONArray(jsonStr);


                    for (int i = 0; i < productslist.length(); i++) {
                        JSONObject jObj = productslist.getJSONObject(i);

                        int id = jObj.getInt("id");
                        int userid = jObj.getInt("userId");
                        String title = jObj.getString("title");
                        boolean completed = jObj.getBoolean("completed");

                        ProductsData pData = new ProductsData(id,userid,title,completed);
                        dbHelper.saveProduct(pData);

                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //populate recyclerview
            populaterecyclerView(filter);
        }

    }
}
