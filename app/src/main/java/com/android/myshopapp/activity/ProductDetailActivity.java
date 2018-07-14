package com.android.myshopapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;

import com.android.myshopapp.R;
import com.android.myshopapp.util.ProductsDataDBHelper;

public class ProductDetailActivity extends AppCompatActivity {

    SwitchCompat switchCompat;
    boolean switchstate = false;
    int productId = 0;
    private ProductsDataDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        dbHelper = new ProductsDataDBHelper(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                onBackPressed();
            }
        });

        Bundle bundle = getIntent().getExtras();
        switchstate = bundle.getBoolean("STATUS");
        productId = bundle.getInt("ID");
        switchCompat=(SwitchCompat)findViewById(R.id.product_status);

        switchCompat.setChecked(switchstate);

        if(switchstate)
        {
            switchCompat.setText("Completed");
        }
        else
        {
            switchCompat.setText("Not Complete");
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    switchCompat.setText("Completed");
                }
                else
                {
                    switchCompat.setText("Not Complete");
                }
                dbHelper.updateProduct(String.valueOf(productId),isChecked);
            }
        });
    }

}
