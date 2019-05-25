package com.example.restservice_app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.restservice_app.DataSource.Product;
import com.example.restservice_app.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context mtx;
    private List<Product> productList;
    private OnItemClickListner mListner;

    public interface OnItemClickListner{
        void  onItemClick(int position);
    }

    public void setOnItemCliclListener(OnItemClickListner listener){
        mListner = listener;
    }

    public ProductAdapter(Context mtx, List<Product> productList) {
        this.mtx = mtx;
        this.productList = productList;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mtx);
        View view = inflater.inflate(R.layout.list_item, null);
        // ProductViewHolder holder = new ProductViewHolder(view);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        String PizzaName = product.getName();
        Double PizzaPrice = product.getPrice();
        String PizzaDescription = product.getDescription();
        String PizzaImage = product.getImgurl();

        holder.textViewname.setText(PizzaName);
        holder.textViewprice.setText("Rs." + PizzaPrice);
        holder.textViewdescription.setText(PizzaDescription);

        Glide.with(mtx).load(PizzaImage).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{

        public TextView textviewid, textViewname, textViewdescription, textViewprice;
        public ImageView imageView;

        public ProductViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            //   textviewid = itemView.findViewById(R.id.id);
            textViewname = itemView.findViewById(R.id.name);
            textViewdescription = itemView.findViewById(R.id.description);
            textViewprice = itemView.findViewById(R.id.price);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListner.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}

