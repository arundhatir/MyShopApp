package com.android.myshopapp.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.myshopapp.R;
import com.android.myshopapp.activity.ProductDetailActivity;
import com.android.myshopapp.model.ProductsData;

import java.util.List;


public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {
    private List<ProductsData> mProductsList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView productsNameTxtV;
        public TextView productsIdTxtV;
        public TextView productStatusTxtV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            productsNameTxtV = (TextView) v.findViewById(R.id.name);
            productStatusTxtV = (TextView) v.findViewById(R.id.status);
            productsIdTxtV = (TextView) v.findViewById(R.id.product_id);



        }
    }

    public void add(int position, ProductsData product) {
        mProductsList.add(position, product);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mProductsList.remove(position);
        notifyItemRemoved(position);
    }


   public ProductsAdapter(List<ProductsData> myDataset, Context context, RecyclerView recyclerView) {
        mProductsList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ProductsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.row_product_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final ProductsData productData = mProductsList.get(position);
        holder.productsNameTxtV.setText("Name: " + productData.getTitle());
        String prodStatus = "";
        if(productData.isCompleted())
        {
            prodStatus = "Yes";
        }
        else prodStatus = "No";
        holder.productStatusTxtV.setText("Completed : " + prodStatus);
        holder.productsIdTxtV.setText("Product Id: "+productData.getId());
        //listen to single view layout click
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToUpdate = new Intent(mContext, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("STATUS",productData.isCompleted());
                bundle.putInt("ID",productData.getId());
                goToUpdate.putExtras(bundle);
                mContext.startActivity(goToUpdate);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mProductsList.size();
    }



}