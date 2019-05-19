package com.example.restservice_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CartAdapter extends  RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    int id;

//    int item_count1 = getItemCount();

    public Context mtx;
    private List<Cart> cartList;
    private CartAdapter.OnItemClickListner mListner;


    public interface OnItemClickListner{
        void  onItemClick( int position);
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
    public void onBindViewHolder(final CartViewHolder holder, final int position) {
        final Cart cart = cartList.get(position);

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


        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartActivity cartActivity = new CartActivity();
                int xx = cart.getId();

                System.out.println(xx+"dddddddddddddddddddddddddddddddddddddddddddddddddddddddddd");

                RequestQueue x = cartActivity.queue1;
                System.out.println(x+"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaawwwwwwwwa");

                String url1 ="http://"+MyIpAddress.MyIpAddress+":8080/demo/deleteByCartId?id="+xx;

                JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1,
                        null, new CartAdapter.HTTPResponseListner(), new CartAdapter.HTTPErrorListner());
                x.add(request1);

                System.out.println("GetItemOutnccccccccccccccccccccccccccccccccccccccccccccccccccccccccc+"+getItemCount());

                if(getItemCount() == 1){

                    Intent intent = new Intent(mtx,EmptyActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity)mtx).finish();
                    mtx.startActivity(intent);
                }else {

                    Intent intent = new Intent(mtx, CartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ((Activity) mtx).finish();
                    mtx.startActivity(intent);
                }

            }
        });

        holder.edit_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Ohhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");

                Intent intent = new Intent(mtx, EditCartActivity.class);

                intent.putExtra("ITEM_ID", cart.getId());
                intent.putExtra("PIZZA_ID",cart.getPizza_id());
                intent.putExtra("PIZZA_NAME", cart.getPizzaname());
                intent.putExtra("PIZZA_DESCRIPTION", cart.getDescription());
                intent.putExtra("PIZZA_SIZE", cart.getSize());
                System.out.println(cart.getSize()+"...............................................................................");
                intent.putExtra("ITEM_COUNT", cart.getItem());
                intent.putExtra("PRICE", cart.getTotal());
                intent.putExtra("IMG_URL", cart.getImg_url());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ((Activity)mtx).finish();
                mtx.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        public TextView textname, textViewdescription, textViewtotal, textsize, textitem, remove, edit_cart;
        public ImageView imageView;

        public CartViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            textname = itemView.findViewById(R.id.name);
            textViewdescription = itemView.findViewById(R.id.description);
            textsize = itemView.findViewById(R.id.size);
            textitem = itemView.findViewById(R.id.item);
            textViewtotal = itemView.findViewById(R.id.price);

            remove = itemView.findViewById(R.id.remove);
            edit_cart = itemView.findViewById(R.id.editCart);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListner != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListner.onItemClick(position);
                        //    System.out.println(position+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                        }
                    }
                }
            });
        }
    }

    class HTTPResponseListner implements Response.Listener<JSONArray> {
        @Override
        public void onResponse(JSONArray jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject object = jsonArray.getJSONObject(i);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class HTTPErrorListner implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    }


}
