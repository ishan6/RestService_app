package com.example.restservice_app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;

import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private Context mtx;
    private List<Cart> cartList;
    private CartAdapter.OnItemClickListner mListner;

    public interface OnItemClickListner{
        void  onItemClick(int position);
    }

    public void setOnItemCliclListener(OnItemClickListner listener){
        mListner = listener;
    }

    public CartAdapter(Context mtx, List<Cart> cartList) {
        this.mtx = mtx;
        this.cartList = cartList;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mtx);
        View view = inflater.inflate(R.layout.cart_item, null);
        // ProductViewHolder holder = new ProductViewHolder(view);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Cart cart = cartList.get(position);

        String PizzaName = cart.getPizzaname();
        Double PizzaPrice = cart.getTotal();
        String PizzaDescription = cart.getDescription();
        String PizzaImage = cart.getImg_url();
        String size = cart.getSize();
        int item = cart.getItem();

        holder.textname.setText(PizzaName);
        holder.textViewtotal.setText("Rs." + PizzaPrice);
        holder.textViewdescription.setText(PizzaDescription);
        holder.textsize.setText(size);
        holder.textitem.setText(item+" Items");

        Glide.with(mtx).load(PizzaImage).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        public TextView textname, textViewdescription, textViewtotal, textsize, textitem;
        public ImageView imageView;

        public CartViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textname = itemView.findViewById(R.id.name);
            textViewdescription = itemView.findViewById(R.id.description);
            textsize = itemView.findViewById(R.id.size);
            textitem = itemView.findViewById(R.id.item);
            textViewtotal = itemView.findViewById(R.id.price);

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
