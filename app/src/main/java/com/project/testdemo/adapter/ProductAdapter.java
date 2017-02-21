package com.project.testdemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.project.testdemo.R;
import com.project.testdemo.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 2017/2/14.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{

    public Context mContext;
    private List<Product> mProductList;

    static class ViewHolder extends RecyclerView.ViewHolder{

        View productView;
        ImageView productImage;
        TextView productName;
        TextView productDesc;
        public ViewHolder(View itemView) {
            super(itemView);
            productView = itemView;
            productImage = (ImageView) itemView.findViewById(R.id.fruit_image);
            productName = (TextView) itemView.findViewById(R.id.product_name);
            productDesc = (TextView) itemView.findViewById(R.id.product_desc);
        }
    }

    public ProductAdapter(Context context, List<Product> productList) {
        mContext = context;
        mProductList = productList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.prudout_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.productView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Product product = mProductList.get(position);
                Toast.makeText(v.getContext(),"You clicked view" +product.getName(),Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = mProductList.get(position);
        Picasso.with(mContext).load(product.getPic()).resize(120,60).into(holder.productImage);
        holder.productName.setText(product.getName());
        holder.productDesc.setText(product.getDesc());
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

}
